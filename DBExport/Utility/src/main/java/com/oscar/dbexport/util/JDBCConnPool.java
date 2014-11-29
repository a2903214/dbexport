package com.oscar.dbexport.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCConnPool {
	public static final STLogger logger = LogHelper.jdbcLogger;

	private String url;
	private int port;
	private String username;
	private String passwd;

	private int maxPoolSize = 10;
	private int maxFreePoolSize = 10;
	private int minPoolSize = 5;
	
	private Long sequence = 0l;
	private int mod = 10000;
	
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMaxFreePoolSize() {
		return maxFreePoolSize;
	}

	public void setMaxFreePoolSize(int maxFreePoolSize) {
		this.maxFreePoolSize = maxFreePoolSize;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}
	
	public int getFreePoolSize() {
		return ConnQueue.size();
	}
	
	public int getUsingPoolSize() {
		return UsingConnectionsMap.size();
	}

	private Queue<Connection> ConnQueue = new LinkedList<Connection>();
	
	private Map<Long, Connection> UsingConnectionsMap= new ConcurrentHashMap<Long, Connection>();

	public JDBCConnPool(int maxPoolSize, int maxFreePoolSize, int minPoolSize) {
		this.maxPoolSize = maxPoolSize;
		this.maxFreePoolSize = maxFreePoolSize;
		this.minPoolSize = minPoolSize;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public synchronized Connection getConnection() throws SQLException {
		Connection conn = ConnQueue.poll();
		if (conn == null) {
			conn = DriverManager.getConnection(url, username, passwd);
		} else {
			if (conn.isClosed()) {
				try {
					conn.close();
				} catch (Throwable tr) {
				}
				conn = getConnection();
			} else {
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					stmt.execute("select 1");
					stmt.close();
				} catch (Throwable tr) {
					logger.info("Exec server connect ping failed, reconnect!");
					if (null != stmt) {
						try {
							stmt.close();
						} catch (Throwable t) {
						}
					}
					try {
						conn.close();
					} catch (Throwable t) {
					}
					conn = getConnection();
				}
			}
		}
		do{
			sequence++;
		}while(UsingConnectionsMap.containsKey(sequence % mod));
		ConnectionWrapper connWrapper = new ConnectionWrapper(conn, this);
		UsingConnectionsMap.put(connWrapper.connId, conn);
		return connWrapper.connection;
	}

	public synchronized void pushConnectionBackToPool(Connection conn) {
		if (ConnQueue.size() < maxFreePoolSize) {
			ConnQueue.offer(conn);
		} else {
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	class ConnectionWrapper implements InvocationHandler {
		private final static String CLOSE_METHOD_NAME = "close";
		private final static String CREATE_STMT_METHOD_NAME = "createStatement";
		private Long connId = 0l;
		public Connection connection = null;
		private Connection m_originConnection = null;
		private JDBCConnPool execConnPool = null;
		public long lastAccessTime = System.currentTimeMillis();
		Throwable debugInfo = new Throwable("Connection initial statement");
		private boolean isClosed = false;

		void getInterfaces(Class<?> clazz, HashSet<Class<?>> interfaces) {
			if (!clazz.equals(Object.class)) {
				Class<?>[] superInterfaces = clazz.getInterfaces();
				if (null != superInterfaces) {
					for (Class<?> superInterface : superInterfaces) {
						interfaces.add(superInterface);
					}
				}
				getInterfaces(clazz.getSuperclass(), interfaces);
			}

		}

		ConnectionWrapper(Connection originConnection, JDBCConnPool execConnPool) {
			HashSet<Class<?>> interfaces = new HashSet<Class<?>>();
			getInterfaces(originConnection.getClass(), interfaces);
			if (logger.isTraceEnabled()) {
				logger.trace(interfaces);
			}
			this.connection = (Connection) Proxy.newProxyInstance(
					originConnection.getClass().getClassLoader(),
					interfaces.toArray(new Class<?>[0]), this);
			m_originConnection = originConnection;
			connId = execConnPool.sequence;
			this.execConnPool = execConnPool;
		}

		@Override
		public Object invoke(Object proxy, Method m, Object[] args)
				throws Throwable {
			Object obj = null;
			if (CLOSE_METHOD_NAME.equals(m.getName())) {
				if (!this.isClosed) {
					this.isClosed = true;
					UsingConnectionsMap.remove(connId);
					execConnPool.pushConnectionBackToPool(m_originConnection);
				} else {
					logger.error("close connection duplicate!");
				}
			} else if (CREATE_STMT_METHOD_NAME.equals(m.getName())) {
				final Statement tmpStmt = (Statement) m.invoke(
						m_originConnection, args);
				HashSet<Class<?>> stmtInterfaces = new HashSet<Class<?>>();
				getInterfaces(tmpStmt.getClass(), stmtInterfaces);
				obj = (Statement) Proxy.newProxyInstance(m_originConnection
						.getClass().getClassLoader(), stmtInterfaces
						.toArray(new Class<?>[0]), new InvocationHandler() {
					Statement orgStmt = tmpStmt;

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						Object obj = null;
						try {
							if ("executeQuery".equals(method.getName())
									&& args != null && args.length > 0) {
								logger.info("execute sql :" + args[0]);
							}
							obj = method.invoke(orgStmt, args);
						} catch (InvocationTargetException e) {
							logger.error("execute in connect error : "
									+ e.getCause().getMessage(), e.getCause());
							throw e.getCause();
						}
						return obj;
					}

				});
			} else {
				if (!this.isClosed) {
					try {
						obj = m.invoke(m_originConnection, args);
					} catch (InvocationTargetException e) {
						logger.error("execute in connect error : "
								+ e.getCause().getMessage(), e.getCause());
						throw e.getCause();
					}
				} else {
					throw new SQLException("connection has been closed");
				}
			}
			lastAccessTime = System.currentTimeMillis();
			return obj;
		}

	}
	
	public int getConnCount() {
		return this.ConnQueue.size();
	}

}

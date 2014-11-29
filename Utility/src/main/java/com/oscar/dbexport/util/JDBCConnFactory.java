package com.oscar.dbexport.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCConnFactory {

	private static Map<String, JDBCConnPool> JdbcConnPools = new ConcurrentHashMap<String, JDBCConnPool>();
	private static int maxPoolSize = 10;
	private static int maxFreePoolSize = 10;
	private static int minPoolSize = 5;

	public static int getMaxPoolSize() {
		return maxPoolSize;
	}

	public static int getMaxFreePoolSize() {
		return maxFreePoolSize;
	}

	public static int getMinPoolSize() {
		return minPoolSize;
	}
	
	public static Map<String, JDBCConnPool> getJdbcConnPools() {
		return JdbcConnPools;
	}

	public static void initPoolProperty(int maxPoolSize, int maxFreePoolSize,
			int minPoolSize) {
		JDBCConnFactory.maxPoolSize = maxPoolSize;
		JDBCConnFactory.maxFreePoolSize = maxFreePoolSize;
		JDBCConnFactory.minPoolSize = minPoolSize;
	}

	public static Connection getConnection(String driver, String url,
			String username, String password) throws SQLException {
		Connection conn = null;
		JDBCConnPool connPool = null;
		String key = url + username + password;
		connPool = JdbcConnPools.get(key);
		if (null == connPool) {
			synchronized (JDBCConnFactory.class) {
				connPool = JdbcConnPools.get(key);
				if (connPool == null) {
					try {
						Class.forName(driver);
					} catch (ClassNotFoundException e) {
						throw new SQLException(e);
					}
					connPool = new JDBCConnPool(maxPoolSize, maxFreePoolSize,
							minPoolSize);
					connPool.setUrl(url);
					connPool.setUsername(username);
					connPool.setPasswd(password);
					JdbcConnPools.put(key, connPool);
				}
			}
		}
		conn = connPool.getConnection();
		return conn;
	}
	
}

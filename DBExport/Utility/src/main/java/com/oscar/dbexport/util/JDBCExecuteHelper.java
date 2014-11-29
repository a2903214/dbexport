package com.oscar.dbexport.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecuteHelper {
	public static final STLogger logger = LogHelper.jdbcLogger;

	public interface IConnectionCallback<T> {
		T process(Connection conn) throws SQLException;
	}

	public interface IDbMetaDataCallback<T> {
		ResultSet getResultSet(DatabaseMetaData dbmd) throws SQLException;

		T process(ResultSet rs) throws SQLException;
	}

	public interface ISqlCallback<T> {
		boolean needPrepare();

		String getSql();

		void bindParam(PreparedStatement stmt);

		void registerResource(ICancelableResource resource);

		T process(ResultSet rs, int updateCount);

	}

	public interface ICancelableResource {
		void cancel();
	}

	public abstract class SimpleSqlCallback<T> implements ISqlCallback<T> {

		@Override
		public boolean needPrepare() {
			return false;
		}

		@Override
		public void bindParam(PreparedStatement stmt) {
		}

	}

	public abstract class PrepareSqlCallback<T> implements ISqlCallback<T> {

		@Override
		public boolean needPrepare() {
			return true;
		}
	}

	public static <T> T execute(Connection conn,
			final IDbMetaDataCallback<T> callback) throws SQLException {
		return execute(conn, false, new IConnectionCallback<T>() {

			@Override
			public T process(Connection conn) throws SQLException {
				ResultSet rs = null;
				T ret = null;
				try {
					DatabaseMetaData dbmd = conn.getMetaData();
					rs = callback.getResultSet(dbmd);
					ret = callback.process(rs);
				} finally {
					if (null != rs) {
						try {
							rs.close();
						} catch (Throwable tr1) {
							logger.error(
									"closs result set failed: "
											+ tr1.getMessage(), tr1);
						}
					}
				}
				return ret;
			}

		});
	}

	public static <T> T execute(Connection conn, boolean withinTxn,
			IConnectionCallback<T> callback) throws SQLException {
		T ret = null;
		try {
			if (withinTxn) {
				conn.setAutoCommit(false);
			}
			ret = callback.process(conn);
			if (withinTxn) {
				conn.commit();
			}
		} catch (Throwable tr) {
			if (withinTxn) {
				try {
					conn.rollback();
				} catch (Throwable tr1) {
					logger.error(
							"rollback transaction failed: " + tr1.getMessage(),
							tr1);
				}
			}
			throw tr;
		}
		return ret;
	}

	public static <T> T execute(Connection conn, boolean withinTxn,
			ISqlCallback<T> callback) throws SQLException {

		class BaseCancelableResource implements ICancelableResource {
			Statement cancelableStmt;

			public BaseCancelableResource(Statement cancelableStmt) {
				this.cancelableStmt = cancelableStmt;
			}

			@Override
			public void cancel() {
				try {
					if (cancelableStmt != null) {
						cancelableStmt.cancel();
					}
				} catch (Throwable tr) {
					logger.error(
							"cancel statement  failed: " + tr.getMessage(), tr);
				}
			}

			public void clear() {
				this.cancelableStmt = null;
			}
		}

		T ret = null;
		Statement stmt = null;
		ResultSet rs = null;
		BaseCancelableResource cancelableStmt = null;
		try {
			if (withinTxn) {
				conn.setAutoCommit(false);
			}
			if (callback.needPrepare()) {
				stmt = conn.prepareStatement(callback.getSql());
				callback.bindParam((PreparedStatement) stmt);
			} else {
				stmt = conn.createStatement();
			}
			cancelableStmt = new BaseCancelableResource(stmt);
			callback.registerResource(cancelableStmt);

			if (callback.needPrepare()) {
				((PreparedStatement) stmt).execute();
			} else {
				stmt.execute(callback.getSql());
			}

			rs = stmt.getResultSet();

			ret = callback.process(rs, stmt.getUpdateCount());

			cancelableStmt.clear();
			if (withinTxn) {
				conn.commit();
			}
		} catch (Throwable tr) {
			if (withinTxn) {
				conn.rollback();
			}
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (Throwable tr1) {
					logger.error(
							"closs result set failed: " + tr1.getMessage(), tr1);
				}
			}
			if (null != stmt) {
				try {
					stmt.close();
				} catch (Throwable tr1) {
					logger.error("closs statement failed: " + tr1.getMessage(),
							tr1);
				}
			}
			if (null != cancelableStmt) {
				cancelableStmt.clear();
			}
		}
		return ret;
	}
}

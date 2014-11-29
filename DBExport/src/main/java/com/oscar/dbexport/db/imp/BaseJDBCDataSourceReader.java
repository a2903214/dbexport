package com.oscar.dbexport.db.imp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oscar.dbexport.db.IDbReader;
import com.oscar.dbexport.meta.Database;
import com.oscar.dbexport.meta.Schema;
import com.oscar.dbexport.meta.Table;
import com.oscar.dbexport.util.JDBCExecuteHelper;
import com.oscar.dbexport.util.JDBCExecuteHelper.IDbMetaDataCallback;
import com.oscar.dbexport.util.STErrorCode;
import com.oscar.dbexport.util.STException;

public class BaseJDBCDataSourceReader implements IDbReader {
	Connection conn;

	BaseJDBCDataSourceReader(Connection conn) {
		this.conn = conn;
	}

	public List<Database> getAllDataBase() throws STException {
		List<Database> databases = null;
		try {
			databases = JDBCExecuteHelper.execute(conn,
					new IDbMetaDataCallback<List<Database>>() {

						public ResultSet getResultSet(DatabaseMetaData dbmd)
								throws SQLException {
							return dbmd.getCatalogs();
						}

						public List<Database> process(ResultSet rs)
								throws SQLException {
							List<Database> databases = new ArrayList<Database>();
							while (rs.next()) {
								Database database = new Database();
								database.setName(rs.getString("TABLE_CAT"));
								databases.add(database);
							}
							return databases;
						}
					});
		} catch (SQLException e) {
			throw new STException(STErrorCode.UNKNOW_ERROR,
					"get database meta data error: " + e.getMessage(), e);
		}
		return databases;
	}

	public Database getDataBase(String dbName) throws STException {
		Database database = null;
		if (null == dbName) {
			return null;
		}
		List<Database> databases = getAllDataBase();
		for (Database tmpdDatabase : databases) {
			if (dbName.equals(tmpdDatabase.getName())) {
				database = tmpdDatabase;
			}
		}
		return database;
	}

	public List<Schema> getAllSchema(final Database database)
			throws STException {
		List<Schema> schemas = null;
		final String dbName = ((database == null) ? null : database.getName());
		try {
			schemas = JDBCExecuteHelper.execute(conn,
					new IDbMetaDataCallback<List<Schema>>() {

						public ResultSet getResultSet(DatabaseMetaData dbmd)
								throws SQLException {
							return dbmd.getSchemas(dbName, "%");
						}

						public List<Schema> process(ResultSet rs)
								throws SQLException {
							List<Schema> schemas = new ArrayList<Schema>();
							while (rs.next()) {
								Schema schema = new Schema();
								schema.setDbName(rs.getString("TABLE_CATALOG"));
								schema.setName(rs.getString("TABLE_SCHEM"));
								schemas.add(schema);
							}
							return schemas;
						}
					});
		} catch (SQLException e) {
			throw new STException(STErrorCode.UNKNOW_ERROR,
					"get database meta data error: " + e.getMessage(), e);
		}
		return schemas;
	}

	public Schema getSchema(final Database database, final String schemaName)
			throws STException {
		final String dbName = ((database == null) ? null : database.getName());

		return getSchema(dbName, schemaName);
	}

	public Schema getSchema(final String dbName, final String schemaName)
			throws STException {
		Schema schema = null;
		try {
			schema = JDBCExecuteHelper.execute(conn,
					new IDbMetaDataCallback<Schema>() {

						public ResultSet getResultSet(DatabaseMetaData dbmd)
								throws SQLException {
							return dbmd.getSchemas(dbName, schemaName);
						}

						public Schema process(ResultSet rs) throws SQLException {
							Schema schema = null;
							if (rs.next()) {
								schema = new Schema();
								schema.setDbName(rs.getString("TABLE_CATALOG"));
								schema.setName(rs.getString("TABLE_SCHEM"));
							}
							return schema;
						}
					});
		} catch (SQLException e) {
			throw new STException(STErrorCode.UNKNOW_ERROR,
					"get database meta data error: " + e.getMessage(), e);
		}
		return schema;
	}

	public List<Table> getAllTable(Schema schema) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Table> getAllTable(String dbName, String schemaName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Table getTable(Schema schema, String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Table getTable(final String dbName, final String schemaName,
			final String tableName) throws STException {
		Table table = null;
		/**
		 * <pre>
		 * TABLE_CAT String => table catalog (may be null) 
		 * TABLE_SCHEM String => table schema (may be null) 
		 * TABLE_NAME String => table name 
		 * TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM". 
		 * REMARKS String => explanatory comment on the table 
		 * TYPE_CAT String => the types catalog (may be null) 
		 * TYPE_SCHEM String => the types schema (may be null) 
		 * TYPE_NAME String => type name (may be null) 
		 * SELF_REFERENCING_COL_NAME String => name of the designated "identifier" column of a typed table (may be null) 
		 * REF_GENERATION String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
		 * </pre>
		 */
		try {
			table = JDBCExecuteHelper.execute(conn,
					new IDbMetaDataCallback<Table>() {

						public ResultSet getResultSet(DatabaseMetaData dbmd)
								throws SQLException {
							return dbmd.getTables(dbName, schemaName,
									tableName, new String[] { "TABLE" });
						}

						public Table process(ResultSet rs) throws SQLException {
							Table table = null;
							if (rs.next()) {
								table = new Table();
								table.setDbName(rs.getString("TABLE_CAT"));
								table.setSchemaName(rs.getString("TABLE_SCHEM"));
								table.setName(rs.getString("TABLE_NAME"));
								table.setDesc(rs.getString("REMARKS"));
							}
							return table;
						}
					});
		} catch (SQLException e) {
			throw new STException(STErrorCode.UNKNOW_ERROR,
					"get database meta data error: " + e.getMessage(), e);
		}
		return table;
	}

}

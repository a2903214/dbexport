package com.oscar.dbexport.meta;

import java.util.List;

import com.oscar.dbexport.util.STException;

public class CachedMetaManager implements IMetaReader {
	private IMetaReader reader;

	public List<Database> getAllDataBase() throws STException {
		return reader.getAllDataBase();
	}

	public Database getDataBase(String dbName) throws STException {
		return reader.getDataBase(dbName);
	}

	public List<Schema> getAllSchema(Database database) throws STException {
		return reader.getAllSchema(database);
	}

	public Schema getSchema(Database database, String schemaName) throws STException {
		return reader.getSchema(database, schemaName);
	}

	public Schema getSchema(String dbName, String schemaName) throws STException {
		return reader.getSchema(dbName, schemaName);
	}

	public List<Table> getAllTable(Schema schema) throws STException {
		return reader.getAllTable(schema);
	}

	public List<Table> getAllTable(String dbName, String schemaName) throws STException {
		return reader.getAllTable(dbName, schemaName);
	}

	public Table getTable(Schema schema, String tableName) throws STException {
		return reader.getTable(schema, tableName);
	}

	public Table getTable(String dbName, String schemaName, String tableName) throws STException {
		return reader.getTable(dbName, schemaName, tableName);
	}

}

package com.oscar.dbexport.meta;

import java.util.List;

public class CachedMetaManager implements IMetaReader {
	private IMetaReader reader;

	public List<Database> getAllDataBase() {
		return reader.getAllDataBase();
	}

	public Database getDataBase(String dbName) {
		return reader.getDataBase(dbName);
	}

	public List<Schema> getAllSchema(Database database) {
		return reader.getAllSchema(database);
	}

	public Schema getSchema(Database database, String schemaName) {
		return reader.getSchema(database, schemaName);
	}

	public Schema getSchema(String dbName, String schemaName) {
		return reader.getSchema(dbName, schemaName);
	}

	public List<Table> getAllTable(Schema schema) {
		return reader.getAllTable(schema);
	}

	public List<Table> getAllTable(String dbName, String schemaName) {
		return reader.getAllTable(dbName, schemaName);
	}

	public Table getTable(Schema schema, String tableName) {
		return reader.getTable(schema, tableName);
	}

	public Table getTable(String dbName, String schemaName, String tableName) {
		return reader.getTable(dbName, schemaName, tableName);
	}

}

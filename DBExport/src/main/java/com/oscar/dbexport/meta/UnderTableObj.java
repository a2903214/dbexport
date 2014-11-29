package com.oscar.dbexport.meta;

public class UnderTableObj extends UnderSchemaObj {
	private String tableName;

	public static String makeUniqueName(String dbName, String schemaName,
			String tableName, String name) {
		return dbName + "." + schemaName + "." + tableName + "." + name;
	}

	public String getUniqueName() {
		return makeUniqueName(this.getDbName(), this.getSchemaName(),
				this.getTableName(), this.getName());
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}

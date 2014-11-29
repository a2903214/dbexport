package com.oscar.dbexport.meta;

public class UnderSchemaObj extends UnderDbObj {
	private String schemaName;

	public static String makeUniqueName(String dbName, String schemaName,
			String name) {
		return ((dbName == null || dbName.isEmpty()) ? (dbName + ".") : dbName)
				+ ((schemaName == null || schemaName.isEmpty()) ? (schemaName + ".")
						: schemaName) + "." + name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getUniqueName() {
		return makeUniqueName(this.getDbName(), this.getSchemaName(),
				this.getName());
	}
}

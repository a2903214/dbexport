package com.oscar.dbexport.meta;

public class UnderDbObj extends BaseMetaObj {
	private String dbName;

	public static String makeUniqueName(String dbName, String name) {
		return ((dbName == null || dbName.isEmpty()) ? (dbName + ".") : dbName)
				+ name;
	}

	public String getUniqueName() {
		return makeUniqueName(this.getDbName(), this.getName());
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}

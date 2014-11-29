package com.oscar.dbexport.meta;

public class Database extends BaseMetaObj {
	String desc;
	String define;

	public static String makeUniqueName(String name) {
		return name;
	}

	public String getUniqueName() {
		return makeUniqueName(this.getName());
	}
}

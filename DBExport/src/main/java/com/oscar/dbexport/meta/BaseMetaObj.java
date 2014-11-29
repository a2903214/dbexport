package com.oscar.dbexport.meta;

public abstract class BaseMetaObj implements IMetaObj{
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

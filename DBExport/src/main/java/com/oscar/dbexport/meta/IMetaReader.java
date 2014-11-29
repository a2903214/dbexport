package com.oscar.dbexport.meta;

import java.util.List;

public interface IMetaReader {
	/**
	 * 获取所有的数据库定义信息
	 * @return
	 */
	List<Database> getAllDataBase();
	
	/**
	 * 获取指定名称的数据库定义信息
	 * @param dbName
	 * @return
	 */
	Database getDataBase(String dbName);
	
	/**
	 * 获取数据库中的所有模式定义信息
	 * @param database
	 * @return
	 */
	List<Schema> getAllSchema(Database database);
	
	/**
	 * 获取数据库中指定名称模式的定义信息
	 * @param database
	 * @param schemaName
	 * @return
	 */
	Schema getSchema(Database database, String schemaName);
	
	/**
	 * 获取指定名称数据库中指定名称的模式定义信息
	 * @param dbName
	 * @param schemaName
	 * @return
	 */
	Schema getSchema(String dbName, String schemaName);
	
	/**
	 * 获取指定模式下的所有表定义信息
	 * @param schema
	 * @return
	 */
	List<Table> getAllTable(Schema schema);
	/**
	 * 获取指定数据库名，模式名下的所有表定义信息
	 * @param dbName
	 * @param schemaName
	 * @return
	 */
	List<Table> getAllTable(String dbName, String schemaName);
	Table getTable(Schema schema, String tableName);
	Table getTable(String dbName, String schemaName, String tableName);
}

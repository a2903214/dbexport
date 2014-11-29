package com.oscar.dbexport.meta;

import java.sql.SQLException;
import java.util.List;

import com.oscar.dbexport.util.STException;

public interface IMetaReader {
	/**
	 * 获取所有的数据库定义信息
	 * @return
	 * @throws SQLException 
	 */
	List<Database> getAllDataBase() throws STException;
	
	/**
	 * 获取指定名称的数据库定义信息
	 * @param dbName
	 * @return
	 * @throws STException 
	 */
	Database getDataBase(String dbName) throws STException;
	
	/**
	 * 获取数据库中的所有模式定义信息
	 * @param database
	 * @return
	 * @throws STException 
	 */
	List<Schema> getAllSchema(Database database) throws STException;
	
	/**
	 * 获取数据库中指定名称模式的定义信息
	 * @param database
	 * @param schemaName
	 * @return 如果指定名称的schema不存在则返回NULL
	 * @throws STException 
	 */
	Schema getSchema(Database database, String schemaName) throws STException;
	
	/**
	 * 获取指定名称数据库中指定名称的模式定义信息
	 * @param dbName
	 * @param schemaName
	 * @return
	 * @throws STException 
	 */
	Schema getSchema(String dbName, String schemaName) throws STException;
	
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
	
	/**
	 * 获取指定模式下的指定名称的表
	 * @param schema
	 * @param tableName
	 * @return
	 */
	Table getTable(Schema schema, String tableName);
	
	/**
	 * 获取指定数据库名、模式名、表名的表定义
	 * @param dbName
	 * @param schemaName
	 * @param tableName
	 * @return
	 * @throws STException 
	 */
	Table getTable(String dbName, String schemaName, String tableName) throws STException;
}

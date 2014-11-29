package com.oscar.dbexport.meta;

import java.util.List;

public interface IMetaReader {
	/**
	 * ��ȡ���е����ݿⶨ����Ϣ
	 * @return
	 */
	List<Database> getAllDataBase();
	
	/**
	 * ��ȡָ�����Ƶ����ݿⶨ����Ϣ
	 * @param dbName
	 * @return
	 */
	Database getDataBase(String dbName);
	
	/**
	 * ��ȡ���ݿ��е�����ģʽ������Ϣ
	 * @param database
	 * @return
	 */
	List<Schema> getAllSchema(Database database);
	
	/**
	 * ��ȡ���ݿ���ָ������ģʽ�Ķ�����Ϣ
	 * @param database
	 * @param schemaName
	 * @return
	 */
	Schema getSchema(Database database, String schemaName);
	
	/**
	 * ��ȡָ���������ݿ���ָ�����Ƶ�ģʽ������Ϣ
	 * @param dbName
	 * @param schemaName
	 * @return
	 */
	Schema getSchema(String dbName, String schemaName);
	
	/**
	 * ��ȡָ��ģʽ�µ����б�����Ϣ
	 * @param schema
	 * @return
	 */
	List<Table> getAllTable(Schema schema);
	/**
	 * ��ȡָ�����ݿ�����ģʽ���µ����б�����Ϣ
	 * @param dbName
	 * @param schemaName
	 * @return
	 */
	List<Table> getAllTable(String dbName, String schemaName);
	Table getTable(Schema schema, String tableName);
	Table getTable(String dbName, String schemaName, String tableName);
}

package com.oscar.dbexport.db.imp;

import com.oscar.dbexport.util.STException;

public class OracleDataSourceReader extends BaseJDBCDataSourceReader {

	OracleDataSourceReader(String driver, String url, String username,
			String password) throws STException {
		super(driver, url, username, password);
	}

}

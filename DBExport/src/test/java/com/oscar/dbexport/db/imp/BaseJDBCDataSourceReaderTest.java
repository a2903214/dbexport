package com.oscar.dbexport.db.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oscar.dbexport.db.IDbReader;
import com.oscar.dbexport.meta.Database;
import com.oscar.dbexport.util.STException;

public class BaseJDBCDataSourceReaderTest {

	final static Logger logger = Logger.getLogger(BaseJDBCDataSourceReaderTest.class);
	static IDbReader reader;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			PropertyConfigurator.configure("./log4j.properties");
			reader = new BaseJDBCDataSourceReader("com.oscar.Driver",
					"jdbc:oscar://127.0.0.1:2003/metadb", "SYSDBA", "szoscar55");
		} catch (Exception ex) {
			logger.fatal("inital datasource failed: " + ex.getMessage(), ex);
			Assert.fail("inital datasource failed: " + ex.getMessage());
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetAllDatabase() throws STException {
		List<Database> databases = reader.getAllDataBase();
		for (Database database : databases) {
			logger.info(database.getUniqueName());
		}
	}

}

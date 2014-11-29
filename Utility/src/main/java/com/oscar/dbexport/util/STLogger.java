package com.oscar.dbexport.util;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class STLogger {

	/**
	 * 当前Log的配置文件
	 */
	private static Properties configure;

	private Logger logger;

	static STLogger getLogger(Class<?> clazz) {
		return new STLogger(clazz);
	}

	static STLogger getLogger(String name) {
		return new STLogger(name);
	}

	private STLogger(Class<?> clazz) {
		logger = (Logger.getLogger(clazz));
	}

	private STLogger(String name) {
		logger = (Logger.getLogger(name));
	}

	/**
	 * 初始化Logger配置
	 * 
	 * @param configFilename
	 *            Logger配置文件
	 */
	public static void init(String configFilename) {
		FileInputStream fis = null;
		try {
			File configFile = new File(configFilename);
			Properties p = new Properties();
			fis = new FileInputStream(configFile);
			p.load(fis);
			init(p);
		} catch (FileNotFoundException e) {
			System.err.println("Log config file [" + configFilename
					+ "] is not exist!");
		} catch (IOException e) {
			System.err.println("Log config file [" + configFilename
					+ "] can't be opened!");
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public static void init(Properties p) {
		configure = p;
		PropertyConfigurator.configure(configure);
	}

	public void assertLog(boolean assertion, String msg) {
		getLogger().assertLog(assertion, msg);
	}

	public void debug(Object message, Throwable t) {
		getLogger().debug(message, t);
	}

	public void debug(Object message) {
		getLogger().debug(message);
	}

	public void error(Object message, Throwable t) {
		getLogger().error(message, t);
	}

	public void error(Object message) {
		getLogger().error(message);
	}

	public void fatal(Object message, Throwable t) {
		getLogger().fatal(message, t);
	}

	public void fatal(Object message) {
		getLogger().fatal(message);
	}

	public void info(Object message, Throwable t) {
		getLogger().info(message, t);
	}

	public void info(Object message) {
		getLogger().info(message);
	}

	public void trace(Object message, Throwable t) {
		getLogger().trace(message, t);
	}

	public void trace(Object message) {
		getLogger().trace(message);
	}

	public void warn(Object message, Throwable t) {
		getLogger().warn(message, t);
	}

	public void warn(Object message) {
		getLogger().warn(message);
	}

	protected Logger getLogger() {
		return logger;
	}

	public static Properties getConfigure() {
		return configure;
	}

	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isEnabledFor(Priority level) {
		return logger.isEnabledFor(level);
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

}
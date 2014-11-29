package com.oscar.dbexport.util.cancelable;

import com.oscar.dbexport.util.STException;

public interface INotCancelWorker {
	
	void doWork() throws STException;

}

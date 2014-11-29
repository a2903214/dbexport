package com.oscar.dbexport.util.cancelable;

import com.oscar.dbexport.util.STException;

public interface ICancelable {

	/**
	 * cancel cancel当前正在执行的动作
	 * 
	 * @throws STException
	 */
	void cancel() throws STException;

	/**
	 * 当前是否正处于Cancel过程中
	 * 
	 * @return
	 * @throws STException
	 */
	boolean checkCanceling() throws STException;

	/**
	 * 当前是否正处于Cancel过程中
	 * 
	 * @param cancelProcesser
	 * @param notCancelWorker
	 * @return
	 * @throws STException
	 */
	boolean checkCanceling(ICancelProcesser cancelProcesser,
			INotCancelWorker notCancelWorker) throws STException;

	/**
	 * 当前是否已经成功Cancel
	 * 
	 * @return
	 * @throws STException
	 */
	boolean waitCancelFinished() throws STException;

	/**
	 * 是否已经被成功cancel
	 * 
	 * @return
	 */
	boolean isCanceled();

	/**
	 * 是否在cancel过程中
	 * 
	 * @return
	 */
	boolean isCanceling();

}

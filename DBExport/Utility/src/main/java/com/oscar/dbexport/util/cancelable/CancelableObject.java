package com.oscar.dbexport.util.cancelable;

import com.oscar.dbexport.util.STException;

public abstract class CancelableObject implements ICancelable {
	private boolean isBeginCancel = false;
	private boolean isCanceled = false;
	private Integer cancelLock = new Integer(0);

	@Override
	public final void cancel() throws STException {
		if (!this.isBeginCancel && !isCanceled) {
			CancelableObject.this.isBeginCancel = true;
			doCancel();
		}
	}

	@Override
	public final boolean checkCanceling() throws STException {
		return this.checkCanceling(null, null);
	}

	@Override
	public final boolean checkCanceling(ICancelProcesser cancelProcesser,
			INotCancelWorker notCancelWorker) throws STException {
		if (this.isBeginCancel || isCanceled) {
			if (null != cancelProcesser) {
				cancelProcesser.doCancelProcesser();
			}
		} else {
			synchronized (cancelLock) {
				if (this.isBeginCancel || isCanceled) {
					try {
						if (null != cancelProcesser) {
							cancelProcesser.doCancelProcesser();
						}
					} finally {
						isCanceled = true;
					}
				} else {
					if (null != notCancelWorker) {
						notCancelWorker.doWork();
					}
				}
			}
		}
		return isBeginCancel;
	}

	@Override
	public final boolean waitCancelFinished() throws STException {
		if (!isCanceled) {
			if (!isBeginCancel) {
				this.cancel();
			}
			synchronized (cancelLock) {
				if (!isCanceled && isBeginCancel) {
					doWaitForCanceled();
					isCanceled = true;
					isBeginCancel = false;
				}
			}
		}
		return isCanceled;
	}

	public abstract void doCancel() throws STException;

	public abstract void doWaitForCanceled() throws STException;

	@Override
	public boolean isCanceled() {
		return this.isCanceled;
	}

	@Override
	public boolean isCanceling() {
		return this.isBeginCancel;
	}

}

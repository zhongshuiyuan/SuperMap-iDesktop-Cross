package com.supermap.desktop.process.enums;

/**
 * Created by highsad on 2017/6/15.
 * 警告状态不合适，警告不是错误，不影响运行，也就是说警告状态和其他状态是可以共存的
 * 后续再重新优化 Workflow 和 Process 的警告实现
 */
public enum RunningStatus {
	NORMAL(0), RUNNING(1), COMPLETED(2), EXCEPTION(3), CANCELLING(4), CANCELLED(5), READY(6), WARNING(7);

	private int value;

	RunningStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

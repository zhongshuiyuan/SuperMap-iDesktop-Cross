package com.supermap.desktop.process.events;

import com.supermap.desktop.event.CancellationEvent;
import com.supermap.desktop.process.core.NodeMatrix;

/**
 * Created by highsad on 2017/6/21.
 */
public class MatrixNodeRemovingEvent<T extends Object> extends CancellationEvent {
	private NodeMatrix matrix;
	private T node;

	public MatrixNodeRemovingEvent(NodeMatrix matrix, T removingNode, boolean isCancel) {
		super(matrix, isCancel);
		this.matrix = matrix;
		this.node = removingNode;
	}

	public NodeMatrix getMatrix() {
		return matrix;
	}

	public T getNode() {
		return node;
	}
}

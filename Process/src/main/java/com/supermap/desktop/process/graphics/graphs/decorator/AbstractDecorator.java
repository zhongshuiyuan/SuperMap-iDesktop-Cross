package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/2/23.
 */
public abstract class AbstractDecorator extends AbstractGraph {

	private AbstractGraph graph;

	public AbstractDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	public AbstractGraph getGraph() {
		return graph;
	}

	public void decorate(AbstractGraph graph) {
		this.graph = graph;
	}

	public void undecorate() {
		this.graph = null;
	}
}
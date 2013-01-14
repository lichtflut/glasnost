/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.model.logic;

import java.util.List;

import org.arastreju.sge.model.nodes.ResourceNode;

import de.lichtflut.rb.core.common.SerialNumberOrderedNodesContainer;


/**
 * <p>
 * Re-arrange Perception order.
 * </p>
 * Created: Jan 14, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionOrder extends SerialNumberOrderedNodesContainer {

	private final List<Perception> perceptions;

	// ---------------- Constructor -------------------------

	public PerceptionOrder(final List<Perception> perceptions){
		this.perceptions = perceptions;
	}

	// ------------------------------------------------------

	public void moveUp(final Perception perception, final int positions){
		super.moveUp(perception.asResource(), positions);
	}

	public void moveDown(final Perception perception, final int positions){
		super.moveDown(perception, positions);
	}

	public void swap(final Perception a, final Perception b){
		new SerialNumberOrderedNodesContainer() {
			@Override
			protected List<? extends ResourceNode> getList() {
				return perceptions;
			}
		}.swap(a, b);
	}

	// ------------------------------------------------------

	@Override
	protected List<? extends ResourceNode> getList() {
		return perceptions;
	}

}

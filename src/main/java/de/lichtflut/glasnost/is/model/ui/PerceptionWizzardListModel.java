/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.model.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.glasnost.is.model.logic.Perception;

/**
 * <p>
 * Listmodel for Perceptions wizzard
 * </p>
 * Created: Jan 11, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionWizzardListModel extends LoadableDetachableModel<List<List<Perception>>> {

	private final IModel<List<ResourceNode>> categories;
	private List<List<Perception>> perceptionsList;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * @param categories Specifies the types of perceptions
	 */
	public PerceptionWizzardListModel(final IModel<List<ResourceNode>> categories){
		this.categories = categories;
	}

	// ------------------------------------------------------

	@Override
	public List<List<Perception>> load() {
		if(null == perceptionsList){
			perceptionsList = new ArrayList<List<Perception>>();
			for (ResourceNode category : categories.getObject()) {
				ArrayList<Perception> perceptions = new ArrayList<Perception>();
				perceptions.add(createPerceptionFor(category));
				perceptionsList.add(perceptions);
			}}
		return perceptionsList;
	}

	public Perception createPerceptionFor(final ResourceNode category) {
		Perception perception = new Perception();
		perception.setType(category);
		perception.setColor("ffffff");
		perception.setID(getSubstring(category, 3));
		perception.setName(getLabel(category));
		return perception;
	}

	public void addPerceptionFor(final ResourceNode category){
		List<Perception> list = perceptionsList.get(categories.getObject().indexOf(category));
		list.add(createPerceptionFor(category));
	}

	// ------------------------------------------------------

	private String getLabel(final ResourceNode category) {
		SemanticNode node = SNOPS.singleObject(category, RDFS.LABEL);
		if(null == node){
			return "";
		}
		return node.asValue().getStringValue();
	}

	private String getSubstring(final ResourceNode category, final int length) {
		if(null == category){
			return "";
		}
		return getLabel(category).substring(0,4).toUpperCase();
	}

}

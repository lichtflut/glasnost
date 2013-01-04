package de.lichtflut.glasnost.is.model.logic;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.ResourceView;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;

/**
 * <p>
 *  Representation of a perception of some subject.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class Perception extends ResourceView {

	public static Perception from(final SemanticNode node) {
		if (node instanceof Perception) {
			return (Perception) node;
		} else if (node instanceof ResourceNode) {
			return new Perception((ResourceNode) node);
		} else {
			return null;
		}
	}

	// ----------------------------------------------------

	/**
	 * Create a new perception.
	 */
	public Perception() {
		addAssociation(RDF.TYPE, GIS.PERCEPTION);
	}

	/**
	 * Create a new perception.
	 */
	public Perception(final QualifiedName qn) {
		super(qn);
		addAssociation(RDF.TYPE, GIS.PERCEPTION);
	}

	/**
	 * Create a new perception object wrapping given resource.
	 * @param resource The perception resource.
	 */
	public Perception(final ResourceNode resource) {
		super(resource);
	}

	// ----------------------------------------------------

	public Context getContext() {
		ResourceID rid = resourceValue(GIS.REPRESENTS_CONTEXT);
		if (rid != null) {
			return new SimpleContextID(rid.getQualifiedName());
		} else {
			return null;
		}
	}

	public void setContext(final Context context) {
		setValue(GIS.REPRESENTS_CONTEXT, context);
	}

	// ----------------------------------------------------

	public List<PerceptionItem> getItems() {
		final List<PerceptionItem> result = new ArrayList<PerceptionItem>();
		for (Statement assoc : getAssociations()) {
			if (assoc.getPredicate().equals(GIS.CONTAINS_PERCEPTION_ITEM)) {
				result.add(PerceptionItem.from(assoc.getObject()));
			}
		}
		return result;
	}

	public void addItem(final PerceptionItem item) {
		addAssociation(GIS.CONTAINS_PERCEPTION_ITEM, item);
	}

	public List<PerceptionItem> getTreeRootItems() {
		final List<PerceptionItem> result = new ArrayList<PerceptionItem>();
		for (Statement assoc : getAssociations()) {
			if (assoc.getPredicate().equals(GIS.CONTAINS_TREE_ROOT_ITEM) && assoc.getObject().isResourceNode()) {
				result.add(PerceptionItem.from(assoc.getObject()));
			}
		}
		return result;
	}

	public void addTreeRootItem(final PerceptionItem item) {
		addAssociation(GIS.CONTAINS_TREE_ROOT_ITEM, item);
	}

	// ----------------------------------------------------

	public String getID() {
		return stringValue(RB.HAS_ID);
	}

	public void setID(final String id) {
		setValue(RB.HAS_ID, id);
	}

	public String getName() {
		return stringValue(RB.HAS_NAME);
	}

	public void setName(final String name) {
		setValue(RB.HAS_NAME, name);
	}

	public String getDescription() {
		return stringValue(RB.HAS_DESCRIPTION);
	}

	public void setDescription(final String name) {
		setValue(RB.HAS_DESCRIPTION, name);
	}

	public Perception getBasePerception() {
		return Perception.from(SNOPS.fetchObject(this, GIS.BASED_ON));
	}

	public void setBasePerception(final Perception base) {
		setValue(GIS.BASED_ON, base);
	}

	// ----------------------------------------------------

	@Override
	public String toString() {
		return "Perception[" + getID() + "]";
	}

	public String printHierarchy() {
		StringBuilder sb = new StringBuilder("Hierarchy of ").append(this);
		for (PerceptionItem current : getTreeRootItems()) {
			sb.append("\n");
			sb.append(current.printHierarchy("  "));
		}
		return sb.toString();
	}

}

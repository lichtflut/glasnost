package de.lichtflut.glasnost.is.model.logic;

import java.util.ArrayList;
import java.util.List;

import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.InheritedDecorator;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.rb.core.RB;

/**
 * <p>
 *  Represents an item of a perception, may be a server, an application, a module, etc.
 * </p>
 *
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class PerceptionItem extends InheritedDecorator {

	public static PerceptionItem from(final SemanticNode node) {
		if (node instanceof PerceptionItem) {
			return (PerceptionItem) node;
		} else if (node instanceof ResourceNode) {
			return new PerceptionItem((ResourceNode) node);
		} else {
			return null;
		}
	}

	// ----------------------------------------------------

	public PerceptionItem() {
	}

	public PerceptionItem(final QualifiedName qn) {
		super(qn);
	}

	public PerceptionItem(final ResourceNode resource) {
		super(resource);
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

	public String getCanonicalName() {
		return "<no canonical name yet>";
	}

	// ----------------------------------------------------

	public List<PerceptionItem> getSubItems() {
		final List<PerceptionItem> result = new ArrayList<PerceptionItem>();
		for (Statement assoc : directAssociations()) {
			SNProperty predicate = SNProperty.from(assoc.getPredicate());
			if (predicate.isSubPropertyOf(RB.HAS_CHILD_NODE) && assoc.getObject().isResourceNode()) {
				result.add(from(assoc.getObject()));
			}
		}
		return result;
	}

	// ----------------------------------------------------

	@Override
	public String toString() {
		return "PerceptionItem[" + getCanonicalName() + "]";
	}


}

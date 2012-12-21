package de.lichtflut.glasnost.is.model.logic;

import de.lichtflut.rb.core.RB;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.InheritedDecorator;
import org.arastreju.sge.model.nodes.views.SNProperty;
import org.arastreju.sge.naming.QualifiedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static PerceptionItem from(SemanticNode node) {
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

    public PerceptionItem(QualifiedName qn) {
        super(qn);
    }

    public PerceptionItem(ResourceNode resource) {
        super(resource);
    }

    // ----------------------------------------------------

    public String getID() {
        return stringValue(RB.HAS_ID);
    }

    public void setID(String id) {
        setValue(RB.HAS_ID, id);
    }

    public String getName() {
        return stringValue(RB.HAS_NAME);
    }

    public void setName(String name) {
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
            Set<SNProperty> superProperties = predicate.getSuperProperties();
            if (predicate.isSubPropertyOf(RB.HAS_CHILD_NODE) && assoc.getObject().isResourceNode()) {
                result.add(from(assoc.getObject()));
            }
        }
        return result;
    }

    // ----------------------------------------------------

    public String toString() {
        return "PerceptionItem[" + getCanonicalName() + "]";
    }


}

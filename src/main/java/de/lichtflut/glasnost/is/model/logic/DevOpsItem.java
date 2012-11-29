package de.lichtflut.glasnost.is.model.logic;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.InheritedDecorator;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.naming.QualifiedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Represents an item of the devops model, may be a server, an application, a module, etc.
 * </p>
 *
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class DevOpsItem extends InheritedDecorator {

    public static DevOpsItem from(SemanticNode node) {
        if (node instanceof DevOpsItem) {
            return (DevOpsItem) node;
        } else if (node instanceof ResourceNode) {
            return new DevOpsItem((ResourceNode) node);
        } else {
            return null;
        }
    }

    // ----------------------------------------------------

    public DevOpsItem() {
    }

    public DevOpsItem(QualifiedName qn) {
        super(qn);
    }

    public DevOpsItem(ResourceNode resource) {
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

    public List<DevOpsItem> getSubItems() {
        final List<DevOpsItem> result = new ArrayList<DevOpsItem>();
        for (Statement assoc : directAssociations()) {
            if (assoc.getPredicate().equals(RB.HAS_CHILD_NODE) && assoc.getObject().isResourceNode()) {
                result.add(from(assoc.getObject()));
            }
        }
        return result;
    }

    // ----------------------------------------------------

    public String toString() {
        return "DevOpsItem[" + getCanonicalName() + "]";
    }


}

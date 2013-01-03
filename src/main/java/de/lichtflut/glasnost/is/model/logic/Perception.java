package de.lichtflut.glasnost.is.model.logic;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;
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

import java.util.ArrayList;
import java.util.List;

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

    public static Perception from(SemanticNode node) {
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
     * Create a new stage.
     */
    public Perception() {
        addAssociation(RDF.TYPE, GIS.PERCEPTION);
    }

    /**
     * Create a new stage.
     */
    public Perception(QualifiedName qn) {
        super(qn);
        addAssociation(RDF.TYPE, GIS.PERCEPTION);
    }

    /**
     * Create a new stage object wrapping given resource.
     * @param resource The stage resource.
     */
    public Perception(ResourceNode resource) {
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

    public void setContext(Context context) {
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

    public void addItem(PerceptionItem item) {
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

    public void addTreeRootItem(PerceptionItem item) {
        addAssociation(GIS.CONTAINS_TREE_ROOT_ITEM, item);
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

    public String getDescription() {
        return stringValue(RB.HAS_DESCRIPTION);
    }

    public void setDescription(String name) {
        setValue(RB.HAS_DESCRIPTION, name);
    }

    public Perception getBasePerception() {
        return Perception.from(SNOPS.fetchObject(this, GIS.BASED_ON));
    }

    public void setBasePerception(Perception base) {
        setValue(GIS.BASED_ON, base);
    }

    // ----------------------------------------------------

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

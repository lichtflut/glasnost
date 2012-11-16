package de.lichtflut.glasnost.is.model.logic;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.ResourceView;

/**
 * <p>
 *  Representation of a stage.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class Stage extends ResourceView {

    public static Stage from(SemanticNode node) {
        if (node == null || node.isValueNode()) {
            return null;
        }
        return new Stage(node.asResource());
    }

    // ----------------------------------------------------

    /**
     * Create a new stage.
     */
    public Stage() {
    }

    /**
     * Create a new stage object wrapping given resource.
     * @param resource The stage resource.
     */
    public Stage(ResourceNode resource) {
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

    // ----------------------------------------------------

    public String toString() {
        return "Stage[" + getName()  + "]-" + getContext();
    }

}

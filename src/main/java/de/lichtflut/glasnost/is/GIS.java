package de.lichtflut.glasnost.is;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.naming.Namespace;

/**
 * <p>
 *  Constants for Glasnost Information Server.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GIS {

    String GIS_NAMESPACE_URI = "http://glasnost.lichtflut.de/definitions/";

    String DEVOPS_NAMESPACE_URI = "http://rb.lichtflut.de/devops#";

    String STAGE_CONTEXT_NAMESPACE_URI = "http://glasnost.lichtflut.de/definitions/stage-contexts/";


    // -- TYPES -------------------------------------------

    /**
     * A stage represents a contexts.
     */
    ResourceID STAGE = new SimpleResourceID(GIS_NAMESPACE_URI, "Stage");


    /**
     * A data center DevOps item..
     */
    ResourceID DATA_CENTER = new SimpleResourceID(DEVOPS_NAMESPACE_URI, "DataCenter");

    // ----------------------------------------------------

    /**
     * A stage represents a contexts.
     */
    ResourceID REPRESENTS_CONTEXT = new SimpleResourceID(GIS_NAMESPACE_URI, "representsContext");

    /**
     * A stage contains one or more 'root' items.
     */
    ResourceID CONTAINS_TREE_ROOT_ITEM = new SimpleResourceID(GIS_NAMESPACE_URI, "containsTreeRootItem");

}

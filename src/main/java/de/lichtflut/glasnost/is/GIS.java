package de.lichtflut.glasnost.is;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

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

	String PERCEPTION_CONTEXT_NAMESPACE_URI = "http://glasnost.lichtflut.de/definitions/perception-contexts/";


	// -- TYPES -------------------------------------------

	/**
	 * A stage represents a contexts.
	 */
	ResourceID PERCEPTION = new SimpleResourceID(GIS_NAMESPACE_URI, "Perception");


	/**
	 * A data center DevOps item..
	 */
	ResourceID DATA_CENTER = new SimpleResourceID(DEVOPS_NAMESPACE_URI, "DataCenter");

	// ----------------------------------------------------

	/**
	 * A perception may represent a contexts.
	 */
	ResourceID REPRESENTS_CONTEXT = new SimpleResourceID(GIS_NAMESPACE_URI, "representsContext");

    /**
     * A perception can be based on another perception, or an item on another item.
     */
    ResourceID BASED_ON = new SimpleResourceID(GIS_NAMESPACE_URI, "basedOn");

	/**
	 * A stage contains one or more 'root' items.
	 */
	ResourceID CONTAINS_TREE_ROOT_ITEM = new SimpleResourceID(GIS_NAMESPACE_URI, "containsTreeRootItem");

    /**
     * Each perception item belongs to a perception.
     */
    ResourceID BELONGS_TO_PERCEPTION = new SimpleResourceID(GIS_NAMESPACE_URI, "belongsToPerception");

    /**
     * A perception contains items.
     */
    ResourceID CONTAINS_PERCEPTION_ITEM = new SimpleResourceID(GIS_NAMESPACE_URI, "containsPerceptionItem");

}

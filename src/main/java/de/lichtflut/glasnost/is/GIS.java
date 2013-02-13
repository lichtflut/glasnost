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
	 * A perception represents a contexts.
	 */
	ResourceID PERCEPTION = new SimpleResourceID(GIS_NAMESPACE_URI, "Perception");

	/**
	 * A category describing a perception.
	 */
	ResourceID PERCEPTION_CATEGORY = new SimpleResourceID(DEVOPS_NAMESPACE_URI, "PerceptionCategory");

	/**
	 * A item of a perception.
	 */
	ResourceID PERCEPTION_ITEM = new SimpleResourceID(GIS_NAMESPACE_URI, "PerceptionItem");

	/**
	 * A data center DevOps item.
	 */
	ResourceID DATA_CENTER = new SimpleResourceID(DEVOPS_NAMESPACE_URI, "DataCenter");

	/**
	 * A software item can have
	 */
	ResourceID SOFTWARE_ITEM = new SimpleResourceID(DEVOPS_NAMESPACE_URI, "SoftwareItem");

	/**
	 * A configuration item.
	 */
	ResourceID CONFIGURATION_ITEM = new SimpleResourceID(DEVOPS_NAMESPACE_URI, "ConfigurationItem");

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
	 * A perception contains one or more 'root' items.
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

	/**
	 * A perception mightbe of a specific type.
	 */
	ResourceID IS_OF_TYPE = new SimpleResourceID(GIS_NAMESPACE_URI, "isOfType");

	/**
	 * Describes the color of an object.
	 */
	ResourceID HAS_COLOR = new SimpleResourceID(GIS_NAMESPACE_URI, "hasColor");

	/**
	 * An entity can be represented by eg. an icon, avatar and so on.
	 */
	ResourceID IS_REPRESENTED_BY = new SimpleResourceID(GIS_NAMESPACE_URI, "isRepresentedBy");

	/**
	 * An entity can have an owner.
	 */
	ResourceID HAS_OWNER = new SimpleResourceID(GIS_NAMESPACE_URI, "hasOwner");

	/**
	 * A person can be responsible for an entity.
	 */
	ResourceID HAS_PERSON_RESPONSIBLE = new SimpleResourceID(GIS_NAMESPACE_URI, "hasPersonResponsible");

}

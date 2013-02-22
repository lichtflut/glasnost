/*
 * Copyright 2013 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.data;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;

/**
 * <p>
 * Some constants for testing only...
 * </p>
 * Created: Feb 21, 2013
 *
 * @author Ravi Knox
 */
public interface GISTestConstants {

	ResourceID HOSTS_MACHINE = new SimpleResourceID(GIS.DEVOPS_NAMESPACE_URI, "hostsMachine");

	ResourceID INHERITS_FROM = new SimpleResourceID(GIS.DEVOPS_NAMESPACE_URI, "inheritsFrom");

	// TYPES

	ResourceID ADDRESS = new SimpleResourceID(RB.COMMON_NAMESPACE_URI, "Address");

	ResourceID PHYSICAL_MACHINE = new SimpleResourceID(GIS.DEVOPS_NAMESPACE_URI, "physicalMachine");


	// PREDICATES

	ResourceID HAS_HOST_MACHINE = new SimpleResourceID(GIS.DEVOPS_NAMESPACE_URI, "hasHostMachine");

}

/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.data;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;

/**
 *  <p>
 *   A priori known URIs for RB.
 * </p>
 *
 * Created: Aug 25, 2011
 *
 * @author Ravi Knox
 */
public interface GISTestConstants {

	// TYPES

	ResourceID ADDRESS = new SimpleResourceID(RB.COMMON_NAMESPACE_URI, "Address");

	ResourceID PHYSICAL_MACHINE = new SimpleResourceID(GIS.DEVOPS_NAMESPACE_URI, "physicalMachine");


	// PREDICATES

	ResourceID HAS_HOST_MACHINE = new SimpleResourceID(GIS.DEVOPS_NAMESPACE_URI, "hasHostMachine");


}

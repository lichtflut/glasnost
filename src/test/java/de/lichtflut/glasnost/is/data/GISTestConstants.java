/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.data;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

import de.lichtflut.glasnost.is.GIS;

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

}

/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

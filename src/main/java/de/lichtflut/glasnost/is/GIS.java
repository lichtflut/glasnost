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

	String DEVOPS_NAMESPACE_URI = "http://rb.lichtflut.de/devops#";


	// -- TYPES -------------------------------------------

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

}

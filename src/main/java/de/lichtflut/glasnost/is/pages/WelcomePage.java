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
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionManagementPanel;
import de.lichtflut.rb.application.base.RBBasePage;

/**
 * <p>
 *  The dashboard - welcome and personal page.
 * </p>
 *
 * <p>
 * 	Created May 30, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class WelcomePage extends RBBasePage {

	/**
	 * @param parameters The parameters.
	 */
	public WelcomePage(final PageParameters parameters) {
		super(parameters);

		add(new PerceptionManagementPanel("perceptionManagement").setOutputMarkupId(true));
	}

}

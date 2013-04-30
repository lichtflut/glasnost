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
package de.lichtflut.glasnost.is.components.devops.perceptions;

import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;

/**
 * <p>
 * Testclass for {@link CreatePerceptionsWizzardPanel}.
 * </p>
 * Created: Jan 9, 2013
 *
 * @author Ravi Knox
 */
public class CreatePerceptionsWizzardPanelTest extends GlasnostWebTest {

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.CreatePerceptionsWizzardPanel#CreatePerceptionsPanel(java.lang.String)}.
	 */
	@Test
	public void testCreatePerceptionsPanel() {
		CreatePerceptionsWizzardPanel panel = new CreatePerceptionsWizzardPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(CreatePerceptionsWizzardPanel.class, "panel");
	}

}

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

import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.glasnost.is.data.RBEntityFactory;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.webck.common.DisplayMode;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Testclass for {@link PerceptionEditPage}.
 * </p>
 * Created: Jan 18, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionEditPageTest extends GlasnostWebTest {

	private RBEntity owner, personResponsible, perceptionCategory;
	private Perception perception;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		owner = RBEntityFactory.createPersonEntity();
		personResponsible = RBEntityFactory.createPersonEntity();
		perceptionCategory = RBEntityFactory.createPerceptionType();
		perception = createPerception();
		initNeccessaryPageData();
	}

	// ------------------------------------------------------

	/**
	 * Scenario: User is not logged in, calls Perception {@link PerceptionDisplayPage} in create mode.
	 */
	@Test
	public void testPerceptionPageNotLoggedIn(){
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);

		tester.startPage(PerceptionDisplayPage.class, parameters);

		tester.assertRenderedPage(LoginPage.class);
	}

	/**
	 * Scenario: Load page with non-existing (blank) perception in create mode.
	 */
	@Test
	public void testPerceptionPageCreateMode(){
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
		PerceptionEditPage page = new PerceptionEditPage(parameters){
			@Override
			protected boolean needsAuthentication() {
				return false;
			}
		};

		tester.startPage(page);

		tester.assertRenderedPage(page.getPageClass());
		tester.assertComponent("perceptionEditor", PerceptionEditPanel.class);
	}

	/**
	 * Scenario: Load page with existing perception in edit mode.
	 */
	@Test
	public void testPerceptionPageEditMode(){
		when(networkService.find(perception.getQualifiedName())).thenReturn(perception);
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.EDIT);
		parameters.add(CommonParams.PARAM_RESOURCE_ID, perception.getQualifiedName());
		PerceptionEditPage page = new PerceptionEditPage(parameters){
			@Override
			protected boolean needsAuthentication() {
				return false;
			}
		};

		tester.startPage(page);

		verify(networkService, times(1)).find(perception.getQualifiedName());

		tester.assertRenderedPage(page.getPageClass());
		tester.assertComponent("perceptionEditor", PerceptionEditPanel.class);
	}

	// ------------------------------------------------------

	private Perception createPerception() {
		Perception perception = new Perception();
		perception.setBasePerception(new Perception());
		perception.setImagePath("/home/glasnost/testpath");
		perception.setName("Glasnost test Perception");
		perception.setType(perceptionCategory.getNode());
		perception.setColor("#fff");
		perception.setDescription("Glasnost test perception");
		perception.setOwner(owner.getID());
		perception.setPersonResponsible(personResponsible.getID());
		return perception;
	}
}

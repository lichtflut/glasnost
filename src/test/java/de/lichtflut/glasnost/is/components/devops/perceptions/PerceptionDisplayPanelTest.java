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

import static org.mockito.Mockito.when;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.data.RBEntityFactory;
import de.lichtflut.rb.core.perceptions.Perception;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.webck.components.fields.FilePreviewLink;

/**
 * <p>
 * Testclass for {@link PerceptionDisplayPanel}
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionDisplayPanelTest extends GlasnostWebTest{

	private RBEntity owner, personResponsible, perceptionCategory;
	private Perception perception;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		owner = RBEntityFactory.createPersonEntity();
		personResponsible = RBEntityFactory.createPersonEntity();
		perceptionCategory = RBEntityFactory.createPerceptionType();
		perception = createPerception();
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a fully specified perception
	 */
	@Test
	public void testPerceptionsDisplayPanelFullyDefinedPerception() {
		initNeccessaryPageData();
		when(networkService.find(perception.getType().getQualifiedName())).thenReturn(perceptionCategory.getNode());
		when(entityManager.find(owner.getID())).thenReturn(owner);
		when(entityManager.find(personResponsible.getID())).thenReturn(personResponsible);
		PerceptionDisplayPanel panel = new PerceptionDisplayPanel("panel", new Model<Perception>(perception));

		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionDisplayPanel.class, "panel");
		tester.assertComponent("panel:form:id", Label.class);
		tester.assertComponent("panel:form:name", Label.class);
		tester.assertComponent("panel:form:type", ExternalLink.class);
		tester.assertComponent("panel:form:color", Label.class);
		tester.assertComponent("panel:form:image", FilePreviewLink.class);
		tester.assertComponent("panel:form:owner", ExternalLink.class);
		tester.assertComponent("panel:form:personResponsible", ExternalLink.class);
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a perception that holds nothing but its type.
	 */
	@Test
	public void testPerceptionsDisplayPanelNotFullyDefinedPerception() {
		initNeccessaryPageData();
		when(networkService.find(perception.getType().getQualifiedName())).thenReturn(perceptionCategory.getNode());
		PerceptionDisplayPanel panel = new PerceptionDisplayPanel("panel", new Model<Perception>(new Perception()));

		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionDisplayPanel.class, "panel");
	}

	/**
	 * Scenario: Display panel with a model containing a <code>null</code> perception
	 */
	@SuppressWarnings("unused")
	@Test(expected=IllegalArgumentException.class)
	public void testPerceptionsDisplayPanelWithNullModel() {
		new PerceptionDisplayPanel("panel", null);

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

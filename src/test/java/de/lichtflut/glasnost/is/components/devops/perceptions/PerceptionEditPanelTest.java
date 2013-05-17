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

import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.rb.core.RB;
import de.lichtflut.rb.core.RBSystem;
import de.lichtflut.rb.core.perceptions.Perception;
import de.lichtflut.rb.webck.components.colorpicker.ColorPickerPanel;
import de.lichtflut.rb.webck.components.fields.AjaxEditableUploadField;
import de.lichtflut.rb.webck.components.fields.EntityPickerField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.junit.Test;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Testclass for {@link PerceptionEditPanel}.
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionEditPanelTest extends GlasnostWebTest{

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel#PerceptionEditPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Panel is started in creation-mode with an empty {@link Perception}
	 */
	@Test
	public void testPerceptionEditPanel() {
		Perception perception = new Perception();
		PerceptionEditPanel panel = new PerceptionEditPanel("panel", new Model<Perception>(perception));

		initNeccessaryPageData();
		when(pathBuilder.queryEntities(DOMAIN_ID.toURI(), RBSystem.PERCEPTION_CATEGORY.toURI())).thenReturn("some perception types");
		when(pathBuilder.queryEntities(DOMAIN_ID.toURI(), RB.PERSON.toURI())).thenReturn("some persons");

		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionEditPanel.class, "panel");
		tester.assertComponent("panel:form:id", TextField.class);
		tester.assertComponent("panel:form:name", TextField.class);
		tester.assertComponent("panel:form:color", ColorPickerPanel.class);
		tester.assertComponent("panel:form:image", AjaxEditableUploadField.class);
		tester.assertComponent("panel:form:owner", EntityPickerField.class);
		tester.assertComponent("panel:form:personResponsible", EntityPickerField.class);
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel#onUpdate()}.
	 */
	@Test
	public void testOnUpdate() {
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel#createSaveButton()}.
	 */
	@Test
	public void testCreateSaveButton() {
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel#createCancelButton()}.
	 */
	@Test
	public void testCreateCancelButton() {
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel#store()}.
	 */
	@Test
	public void testStore() {
	}

	// ------------------------------------------------------

	@Override
	protected void earlyInitialize() {
		super.initNeccessaryPageData();
	}
}

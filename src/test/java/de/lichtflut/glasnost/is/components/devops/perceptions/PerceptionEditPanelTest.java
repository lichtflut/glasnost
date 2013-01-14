/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import static org.mockito.Mockito.when;

import org.apache.wicket.model.Model;
import org.junit.Ignore;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.core.RB;

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
	@Ignore
	// FIXME serviceContext mock does not get called and returns null to EntityPicker!?
	public void testPerceptionEditPanel() {
		Perception perception = new Perception();
		PerceptionEditPanel panel = new PerceptionEditPanel("panel", new Model<Perception>(perception));

		initNeccessaryPageData();
		when(pathBuilder.queryEntities(DOMAIN_ID.toURI(), GIS.PERCEPTION_CATEGORY.toURI())).thenReturn("some perception types");
		when(pathBuilder.queryEntities(DOMAIN_ID.toURI(), RB.PERSON.toURI())).thenReturn("some persons");

		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionEditPanel.class, "panel");
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

}

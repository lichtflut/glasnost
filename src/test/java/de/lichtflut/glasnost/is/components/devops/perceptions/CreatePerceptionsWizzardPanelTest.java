/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;

/**
 * <p>
 * [TODO Insert description here.]
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

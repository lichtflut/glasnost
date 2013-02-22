/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.dialog;

import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;

/**
 * <p>
 * Testclass for {@link CreatePerceptionsWizzardDialog}.
 * </p>
 * Created: Jan 9, 2013
 *
 * @author Ravi Knox
 */
public class CreatePerceptionsWizzardDialogTest extends GlasnostWebTest {

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.dialog.CreatePerceptionsWizzardDialog#CreatePerceptionsWizzardDialog(java.lang.String)}.
	 */
	@Test
	public void testCreatePerceptionsWizzardDialog() {
		CreatePerceptionsWizzardDialog dialog = new CreatePerceptionsWizzardDialog("dialog");

		tester.startComponentInPage(dialog);

		assertRenderedPanel(CreatePerceptionsWizzardDialog.class, "dialog");
	}

}

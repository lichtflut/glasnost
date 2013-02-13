/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.dialog;

import org.apache.wicket.model.Model;
import org.arastreju.sge.model.ResourceID;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;


/**
 * <p>
 * Testclass for {@link CatalogDialog}.
 * </p>
 * Created: Feb 13, 2013
 *
 * @author Ravi Knox
 */
public class CatalogDialogTest extends GlasnostWebTest{

	@Test
	public void testCatalogDialog(){
		CatalogDialog dialog = new CatalogDialog("dialog",  new Model<ResourceID>(GIS.DATA_CENTER));

		tester.startComponentInPage(dialog);

		assertRenderedPanel(CatalogDialog.class, "dialog");

	}


}

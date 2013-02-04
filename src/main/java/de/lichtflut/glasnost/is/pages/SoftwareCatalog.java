/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.softwareCatalog.CatalogPanel;
import de.lichtflut.rb.application.base.RBBasePage;

/**
 * <p>
 * This page displays the Softwarecatalog for browsing
 * </p>
 * Created: Jan 31, 2013
 *
 * @author Ravi Knox
 */
public class SoftwareCatalog extends RBBasePage {

	/**
	 * Constructor
	 */
	public SoftwareCatalog() {
		add(new CatalogPanel("catalog",GIS.SOFTWARE_ITEM));
	}


}

/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.model.Model;

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
public class SoftwareCatalogPage extends RBBasePage {

	/**
	 * Constructor
	 */
	public SoftwareCatalogPage() {
		add(new CatalogPanel("catalog", Model.of(GIS.SOFTWARE_ITEM)));
	}


}

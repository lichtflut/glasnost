/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import java.util.UUID;

import org.apache.wicket.model.Model;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.rb.core.RB;

/**
 * <p>
 * Testclass for {@link CatalogItemInfoPanel}.
 * </p>
 * Created: Feb 8, 2013
 *
 * @author Ravi Knox
 */
public class CatalogItemInfoPanelTest extends GlasnostWebTest {

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.softwareCatalog.CatalogItemInfoPanel#CatalogItemInfoPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testCatalogItemInfoPanelNoDescription() {
		ResourceNode node = new SNResource();

		CatalogItemInfoPanel panel = new CatalogItemInfoPanel("panel", new Model<ResourceNode>(node));

		tester.startComponentInPage(panel);

		assertRenderedPanel(CatalogItemInfoPanel.class, "panel");
		tester.assertLabel("panel:info", panel.getLocalizer().getString("label.no-description", panel));
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.softwareCatalog.CatalogItemInfoPanel#CatalogItemInfoPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testCatalogItemInfoPanelWithDescription() {
		String description = UUID.randomUUID().toString();
		ResourceNode node = new SNResource();
		node.addAssociation(RB.HAS_DESCRIPTION, new SNValue(ElementaryDataType.STRING, description));

		CatalogItemInfoPanel panel = new CatalogItemInfoPanel("panel", new Model<ResourceNode>(node));

		tester.startComponentInPage(panel);

		assertRenderedPanel(CatalogItemInfoPanel.class, "panel");
		tester.assertLabel("panel:info", description);
	}

}

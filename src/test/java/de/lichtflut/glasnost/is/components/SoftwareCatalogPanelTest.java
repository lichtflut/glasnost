/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.junit.Ignore;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;

/**
 * <p>
 * Testclass for {@link SoftwareCatalogPanel}.
 * </p>
 * Created: Jan 28, 2013
 *
 * @author Ravi Knox
 */
public class SoftwareCatalogPanelTest extends GlasnostWebTest {

	private List<ResourceNode> categories;
	private ResourceNode root;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		categories = getCategories();
		root = getRoot();
	}

	// ------------------------------------------------------



	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.SoftwareCatalogPanel#SoftwareCatalogPanel(java.lang.String)}.
	 */
	@Test
	public void testSoftwareCatalogPanel() {
		when(networkService.resolve(GIS.SOFTWARE_ITEM)).thenReturn(root);
		SoftwareCatalogPanel panel = new SoftwareCatalogPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(SoftwareCatalogPanel.class, "panel");

		tester.assertComponent("panel:categoriesTitle", GlasnostTitle.class);
		tester.assertListView("panel:categoriesList", Collections.EMPTY_LIST);
	}


	@Test
	@Ignore
	public void testGetAllCategories(){
		when(networkService.resolve(GIS.SOFTWARE_ITEM)).thenReturn(root);
		SoftwareCatalogPanel panel = new SoftwareCatalogPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(SoftwareCatalogPanel.class, "panel");

		tester.assertComponent("panel:categoriesTitle", GlasnostTitle.class);
		tester.assertListView("panel:categoriesList", getCategories());
	}

	// ------------------------------------------------------

	private List<ResourceNode> getCategories(){
		List<ResourceNode> list = new ArrayList<ResourceNode>();

		ResourceNode appServer = new SNResource();
		SNOPS.assure(appServer, RDFS.LABEL, new SNValue(ElementaryDataType.STRING, "Application Server"));
		list.add(appServer);

		ResourceNode dataStore = new SNResource();
		SNOPS.assure(dataStore, RDFS.LABEL, new SNValue(ElementaryDataType.STRING, "Datastore"));
		list.add(dataStore);

		return list;
	}

	private ResourceNode getRoot(){
		ResourceNode root = new SNResource(GIS.SOFTWARE_ITEM.getQualifiedName());
		for (ResourceNode node : categories) {
			node.addAssociation(RDFS.SUB_CLASS_OF, root);
		}
		return root;
	}

}

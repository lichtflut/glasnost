/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

/**
 * <p>
 * Testclass for {@link SoftwareCatalogPanel}.
 * </p>
 * Created: Jan 28, 2013
 *
 * @author Ravi Knox
 */
public class SoftwareCatalogPanelTest extends GlasnostWebTest {

	private Set<SNClass> categories;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		categories = getCategories();
	}

	// ------------------------------------------------------



	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.SoftwareCatalogPanel#SoftwareCatalogPanel(java.lang.String)}.
	 */
	@Test
	public void testSoftwareCatalogPanel() {
		SoftwareCatalogPanel panel = new SoftwareCatalogPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(SoftwareCatalogPanel.class, "panel");

		tester.assertComponent("panel:categoriesTitle", GlasnostTitle.class);
		tester.assertListView("panel:categoriesList", Collections.EMPTY_LIST);
	}

	@Test
	public void testGetAllCategories(){
		when(typeManager.getSubClasses(GIS.SOFTWARE_ITEM)).thenReturn(categories);
		SoftwareCatalogPanel panel = new SoftwareCatalogPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(SoftwareCatalogPanel.class, "panel");

		tester.assertComponent("panel:categoriesTitle", GlasnostTitle.class);
		tester.assertListView("panel:categoriesList", new ArrayList<SNClass>(categories));
		Iterator<SNClass> iterator = categories.iterator();
		tester.assertLabel("panel:categoriesList:0:link:label", new ResourceLabelModel(iterator.next()).getObject());
		tester.assertLabel("panel:categoriesList:1:link:label", new ResourceLabelModel(iterator.next()).getObject());
	}



	// ------------------------------------------------------

	private Set<SNClass> getCategories(){
		Set<SNClass> set = new HashSet<SNClass>();

		SNClass appServer = new SNClass();
		SNOPS.assure(appServer, RDFS.LABEL, new SNValue(ElementaryDataType.STRING, "Application Server"));
		set.add(appServer);

		SNClass dataStore = new SNClass();
		SNOPS.assure(dataStore, RDFS.LABEL, new SNValue(ElementaryDataType.STRING, "Datastore"));
		set.add(dataStore);

		return set;
	}

}

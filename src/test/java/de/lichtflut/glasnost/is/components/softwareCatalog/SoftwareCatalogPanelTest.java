/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.glasnost.is.components.softwareCatalog.SoftwareCatalogPanel;
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

	private static final String resourcePrefix = "http://lichtflut.de/common#";

	private Set<SNClass> superCategories;
	private Set<SNClass> lvlOneCategories;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		superCategories = getSuperCategories();
		lvlOneCategories = getLvlOneCategories();
	}

	// ------------------------------------------------------



	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.softwareCatalog.SoftwareCatalogPanel#SoftwareCatalogPanel(java.lang.String)}.
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
		when(typeManager.getSubClasses(GIS.SOFTWARE_ITEM)).thenReturn(superCategories);
		SoftwareCatalogPanel panel = new SoftwareCatalogPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(SoftwareCatalogPanel.class, "panel");

		tester.assertComponent("panel:categoriesTitle", GlasnostTitle.class);
		tester.assertListView("panel:categoriesList", new ArrayList<SNClass>(superCategories));
		Iterator<SNClass> iterator = superCategories.iterator();
		tester.assertComponent("panel:categoriesList:0:link", AbstractLink.class);
		tester.assertLabel("panel:categoriesList:0:link:label", new ResourceLabelModel(iterator.next()).getObject());
		tester.assertLabel("panel:categoriesList:1:link:label", new ResourceLabelModel(iterator.next()).getObject());
		tester.assertInvisible("panel:specifyingList");
	}

	@Test
	public void testShowCategoryOnClick(){
		when(typeManager.getSubClasses(GIS.SOFTWARE_ITEM)).thenReturn(superCategories);
		when(typeManager.getSubClasses(superCategories.iterator().next())).thenReturn(lvlOneCategories);
		SoftwareCatalogPanel panel = new SoftwareCatalogPanel("panel");

		tester.startComponentInPage(panel);

		assertRenderedPanel(SoftwareCatalogPanel.class, "panel");

		tester.executeAjaxEvent("panel:categoriesList:0:link", "onclick");

		tester.assertVisible("panel:specifyingList");
		tester.assertComponent("panel:specifyingList:0:itemListTitle", GlasnostTitle.class);
		tester.assertVisible("panel:specifyingList:0:subList");
		tester.assertListView("panel:specifyingList:0:subList", new ArrayList<SNClass>(lvlOneCategories));
		Iterator<SNClass> iterator = lvlOneCategories.iterator();
		tester.assertLabel("panel:specifyingList:0:subList:0:subLink:subLabel", new ResourceLabelModel(iterator.next()).getObject());
	}

	// ------------------------------------------------------

	private Set<SNClass> getSuperCategories(){
		Set<SNClass> set = new LinkedHashSet<SNClass>();

		set.add(getSNClassFor("Application Server"));
		set.add(getSNClassFor("DataStore"));

		return set;
	}

	private Set<SNClass> getLvlOneCategories(){
		Set<SNClass> set = new LinkedHashSet<SNClass>();

		set.add(getSNClassFor("Glasfish"));
		set.add(getSNClassFor("JBoss"));
		set.add(getSNClassFor("Jetty"));
		set.add(getSNClassFor("Tomcat"));
		set.add(getSNClassFor("Web Logic"));

		return set;
	}

	private SNClass getSNClassFor(final String label) {
		SNResource snResource = new SNResource(QualifiedName.create(resourcePrefix + label));
		SNClass appServer = new SNClass(snResource);
		SNOPS.assure(appServer, RDFS.LABEL, new SNValue(ElementaryDataType.STRING, label));
		return appServer;
	}

}

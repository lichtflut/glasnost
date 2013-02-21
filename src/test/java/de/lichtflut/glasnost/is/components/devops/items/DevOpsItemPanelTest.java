/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.items;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionItem;

/**
 * <p>
 * Testclass for {@link DevOpsItemPanel}.
 * </p>
 * Created: Feb 18, 2013
 *
 * @author Ravi Knox
 */
public class DevOpsItemPanelTest extends GlasnostWebTest {

	IModel<PerceptionItem> model;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		model = getPerceptionModel();
	}

	// ------------------------------------------------------

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.items.DevOpsItemPanel#DevOpsItemPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testDevOpsItemPanel() {

		DevOpsItemPanel panel = new DevOpsItemPanel("panel", model);

		tester.startComponentInPage(panel);

		assertRenderedPanel(DevOpsItemPanel.class, "panel");
		tester.assertLabel("panel:id", new PropertyModel<String>(model, "ID").getObject());
		tester.assertLabel("panel:name", new PropertyModel<String>(model, "name").getObject());
		tester.assertComponent("panel:more", AjaxLink.class);
		tester.assertInvisible("panel:subItems");
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.items.DevOpsItemPanel#DevOpsItemPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testDevOpsItemPanelOnClickEvent() {
		DevOpsItemPanel panel = new DevOpsItemPanel("panel", model);

		tester.startComponentInPage(panel);

		assertRenderedPanel(DevOpsItemPanel.class, "panel");
		tester.assertInvisible("panel:subItems");

		// unfold subItems of root perception
		tester.executeAjaxEvent(tester.getComponentFromLastRenderedPage("panel:more"), "onclick");

		tester.assertComponent("panel:details", Link.class);
		tester.assertListView("panel:subItems", model.getObject().getSubItems());
		tester.assertInvisible("panel:subItems:0:item:subItems");

		// unfold subItems of 1st lvl perception
		tester.executeAjaxEvent(tester.getComponentFromLastRenderedPage("panel:subItems:0:item:more"), "onClick");

		tester.assertListView("panel:subItems:0:item:subItems", model.getObject().getSubItems().get(0).getSubItems());
		tester.assertVisible("panel:subItems:0:item:subItems");
	}

	// ------------------------------------------------------

	private IModel<PerceptionItem> getPerceptionModel() {
		PerceptionItem root = new PerceptionItem();
		root.setID("0");
		root.setName("Root Item");
		root.setPerception(new Perception());

		PerceptionItem firstlvl = new PerceptionItem();
		firstlvl.setID("1");
		firstlvl.setName("First Level");
		firstlvl.setPerception(new Perception());
		root.addSubItem(firstlvl);

		PerceptionItem scndLvl = new PerceptionItem();
		scndLvl.setID("2");
		scndLvl.setName("Second Level");
		scndLvl.setPerception(new Perception());
		firstlvl.addSubItem(scndLvl);

		return Model.of(root);
	}

}

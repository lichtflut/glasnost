/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.items;

import static org.mockito.Mockito.when;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.arastreju.sge.model.nodes.SNResource;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.data.ConstraintsFactory;
import de.lichtflut.glasnost.is.data.GISTestConstants;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.rb.core.RB;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.entity.impl.RBEntityImpl;
import de.lichtflut.rb.core.schema.model.Datatype;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.schema.model.ResourceSchema;
import de.lichtflut.rb.core.schema.model.impl.PropertyDeclarationImpl;
import de.lichtflut.rb.core.schema.model.impl.ResourceSchemaImpl;
import de.lichtflut.rb.webck.components.entity.quickinfo.QuickInfoPanel;

/**
 * <p>
 * Testclass for {@link DevOpsItemPanel}.
 * </p>
 * Created: Feb 18, 2013
 *
 * @author Ravi Knox
 */
public class DevOpsItemPanelTest extends GlasnostWebTest {

	private IModel<PerceptionItem> model;
	private RBEntity datacenter;

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
	public void testDevOpsItemPanelNoQuickInfo() {
		datacenter = createDataCenter(false);
		prepareEntityManager();

		DevOpsItemPanel panel = new DevOpsItemPanel("panel", model);

		tester.startComponentInPage(panel);

		assertRenderedPanel(DevOpsItemPanel.class, "panel");
		tester.assertComponent("panel:detailsLink", Link.class);
		tester.assertLabel("panel:detailsLink:linkLabel", new PropertyModel<String>(model, "name").getObject());
		tester.assertComponent("panel:more", AjaxLink.class);
		tester.assertInvisible("panel:container");
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.items.DevOpsItemPanel#DevOpsItemPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testDevOpsItemPanelOnClickEvent() {
		datacenter = createDataCenter(false);
		prepareEntityManager();

		DevOpsItemPanel panel = new DevOpsItemPanel("panel", model);

		tester.startComponentInPage(panel);

		assertRenderedPanel(DevOpsItemPanel.class, "panel");
		tester.assertInvisible("panel:container");

		// unfold subItems of root perception
		tester.executeAjaxEvent(tester.getComponentFromLastRenderedPage("panel:more"), "onclick");

		tester.assertComponent("panel:container:infoPanel",QuickInfoPanel.class);
		tester.assertListView("panel:container:subItems", model.getObject().getSubItems());
		tester.assertInvisible("panel:container:subItems:0:item:container:subItems");

		// unfold subItems of 1st lvl perception
		tester.executeAjaxEvent(tester.getComponentFromLastRenderedPage("panel:container:subItems:0:item:more"), "onClick");

		tester.assertListView("panel:container:subItems:0:item:container:subItems", model.getObject().getSubItems().get(0).getSubItems());
		tester.assertVisible("panel:container:subItems:0:item:container:subItems");
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

	private void prepareEntityManager() {
		when(entityManager.find(model.getObject())).thenReturn(datacenter);
		when(entityManager.find(model.getObject().getSubItems().get(0))).thenReturn(datacenter);
		when(entityManager.find(model.getObject().getSubItems().get(0).getSubItems().get(0))).thenReturn(datacenter);
	}

	public RBEntity createDataCenter(final boolean quickInfo) {
		RBEntity entity = new RBEntityImpl(new SNResource(), buildDataCenter(quickInfo));

		entity.getField(RB.HAS_ID).addValue("DCX");
		entity.getField(RB.HAS_NAME).addValue("Datacenter X");
		entity.getField(RB.HAS_DESCRIPTION).addValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum blandit ullamcorper ligula, eu fringilla mauris gravida eu. Fusce quis tortor id est tempor scelerisque non sed neque. Fusce sem ante, rhoncus ac pellentesque eget, interdum id mi. In dui urna, hendrerit id hendrerit in, laoreet ac urna. ");

		return entity;
	}


	public static ResourceSchema buildDataCenter(final boolean quickInfo) {
		ResourceSchemaImpl schema = new ResourceSchemaImpl(GIS.DATA_CENTER);

		PropertyDeclaration id = new PropertyDeclarationImpl(RB.HAS_ID, Datatype.STRING);
		schema.addPropertyDeclaration(id);

		PropertyDeclaration name = new PropertyDeclarationImpl(RB.HAS_NAME, Datatype.STRING);
		schema.addPropertyDeclaration(name);

		PropertyDeclaration description = new PropertyDeclarationImpl(RB.HAS_DESCRIPTION, Datatype.STRING);
		schema.addPropertyDeclaration(description);

		PropertyDeclaration hostsMachine = new PropertyDeclarationImpl(GISTestConstants.HOSTS_MACHINE, Datatype.RESOURCE);
		schema.addPropertyDeclaration(hostsMachine);

		PropertyDeclaration inheritsFrom = new PropertyDeclarationImpl(GISTestConstants.INHERITS_FROM, Datatype.RESOURCE);
		inheritsFrom.setConstraint(ConstraintsFactory.buildTypeConstraint(GIS.SOFTWARE_ITEM));
		schema.addPropertyDeclaration(inheritsFrom);

		if(quickInfo){
			schema.addQuickInfo(RB.HAS_ID);
			schema.addQuickInfo(RB.HAS_NAME);
			schema.addQuickInfo(RB.HAS_DESCRIPTION);
		}
		return schema;
	}
}

/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import static org.mockito.Mockito.when;

import org.apache.wicket.model.Model;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.data.RBEntityFactory;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.core.entity.RBEntity;

/**
 * <p>
 * Testclass for {@link PerceptionDisplayPanel}
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionDisplayPanelTest extends GlasnostWebTest{

	private RBEntity owner;
	private RBEntity personResponsible;
	private ResourceNode perceptionyTypeNode;
	private Perception perception;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		owner = RBEntityFactory.createPersonEntity();
		personResponsible = RBEntityFactory.createPersonEntity();

		perceptionyTypeNode = createPerceptionTypeNode();
		perception = createPerception();
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a fully specified perception
	 */
	@Test
	public void testPerceptionsDisplayPanelFullyDefinedPerception() {
		initNeccessaryPageData();
		when(networkService.find(perception.getType().getQualifiedName())).thenReturn(perceptionyTypeNode);
		when(entityManager.find(owner.getID())).thenReturn(owner);
		when(entityManager.find(personResponsible.getID())).thenReturn(personResponsible);
		PerceptionDisplayPanel panel = new PerceptionDisplayPanel("panel", new Model<Perception>(perception));

		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionDisplayPanel.class, "panel");
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a perception that holds nothing but its type.
	 */
	@Test
	public void testPerceptionsDisplayPanelNotFullyDefinedPerception() {
		initNeccessaryPageData();
		when(networkService.find(perception.getType().getQualifiedName())).thenReturn(perceptionyTypeNode);
		PerceptionDisplayPanel panel = new PerceptionDisplayPanel("panel", new Model<Perception>(new Perception()));

		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionDisplayPanel.class, "panel");
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a <code>null</code> perception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testPerceptionsDisplayPanelWithNullData() {
		new PerceptionDisplayPanel("panel", new Model<Perception>(null));
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a <code>null</code> perception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testPerceptionsDisplayPanelWithNullModel() {
		new PerceptionDisplayPanel("panel", null);

	}

	// ------------------------------------------------------

	private Perception createPerception() {
		Perception perception = new Perception();
		perception.setBasePerception(new Perception());
		QualifiedName contextQN = new QualifiedName(GIS.PERCEPTION_CONTEXT_NAMESPACE_URI);
		perception.setContext(new SimpleContextID(contextQN));
		perception.setImagePath("/home/glasnost/testpath");
		perception.setName("Glasnost test Perception");
		perception.setType(perceptionyTypeNode);
		perception.setColor("#fff");
		perception.setDescription("Glasnost test perception");
		perception.setOwner(owner.getID());
		perception.setPersonResponsible(personResponsible.getID());
		return perception;
	}

	private ResourceNode createPerceptionTypeNode() {
		ResourceNode perceptionyTypeNode = new SNResource(new QualifiedName(GIS.PERCEPTION_CATEGORY + "#Development"));
		perceptionyTypeNode.addAssociation(RDFS.LABEL, new SNValue(ElementaryDataType.STRING, "Development"));
		return perceptionyTypeNode;
	}

}

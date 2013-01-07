/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import static org.mockito.Mockito.when;

import org.apache.wicket.model.Model;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ElementaryDataType;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SNValue;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.model.logic.Perception;

/**
 * <p>
 * Testclass for {@link PerceptionDisplayPanel}
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionDisplayPanelTest extends GlasnostWebTest{

	private ResourceNode perceptionyTypeNode;
	private Perception perception;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		perceptionyTypeNode = createPerceptionTypeNode();
		perception = createPerception();
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel#PerceptionsDisplayPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 * Scenario: Display panel with a model containing a perception
	 */
	@Test
	public void testPerceptionsDisplayPanel() {
		PerceptionDisplayPanel panel = new PerceptionDisplayPanel("panel", new Model<Perception>(perception));

		when(networkService.find(perception.getType().getQualifiedName())).thenReturn(perceptionyTypeNode);
		tester.startComponentInPage(panel);

		assertRenderedPanel(PerceptionDisplayPanel.class, "panel");
	}

	// ------------------------------------------------------

	private Perception createPerception() {
		Perception perception = new Perception();
		perception.setType(perceptionyTypeNode);
		perception.setColor("#fff");
		perception.setDescription("Glasnost test perception");
		perception.setImagePath("/home/glasnost/testpath");
		perception.setName("Glasnost test Perception");
		return perception;
	}

	private ResourceNode createPerceptionTypeNode() {
		ResourceNode perceptionyTypeNode = new SNResource(new QualifiedName(GIS.PERCEPTION_TYPE + "#Development"));
		perceptionyTypeNode.addAssociation(RDFS.LABEL, new SNValue(ElementaryDataType.STRING, "Development"));
		return perceptionyTypeNode;
	}

}

/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.glasnost.is.data.ResourceSchemaFactory;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.schema.model.ResourceSchema;

/**
 * <p>
 * Testclass for {@link CatalogProposalPanel}.
 * </p>
 * Created: Feb 5, 2013
 *
 * @author Ravi Knox
 */
public class CatalogProposalPanelTest extends GlasnostWebTest{

	private ResourceNode type;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		type = new SNResource(GIS.DATA_CENTER.getQualifiedName());
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.softwareCatalog.CatalogProposalPanel#CatalogProposalPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testCatalogProposalPanel() {
		IModel<ResourceID> model = new Model<ResourceID>(type);
		ResourceSchema schema = ResourceSchemaFactory.buildDatatCenter();
		List<ResourceID> referencedTypes = getResourceReferencedTypes(schema);

		when(schemaManager.findSchemaForType(type)).thenReturn(schema);

		CatalogProposalPanel panel = new CatalogProposalPanel("panel", model);

		tester.startComponentInPage(panel);

		assertRenderedPanel(CatalogProposalPanel.class, "panel");
		tester.assertComponent("panel:title", GlasnostTitle.class);
		tester.assertListView("panel:proposals", referencedTypes);

		verify(schemaManager, atLeastOnce()).findSchemaForType(type);

	}

	// ------------------------------------------------------

	private List<ResourceID> getResourceReferencedTypes(final ResourceSchema schema) {
		ArrayList<ResourceID> referencedTypes = new ArrayList<ResourceID>();
		for (PropertyDeclaration decl : schema.getPropertyDeclarations()) {
			if(decl.hasConstraint() && null != decl.getConstraint().getTypeConstraint()){
				referencedTypes.add(decl.getConstraint().getTypeConstraint());
			}
		}
		return referencedTypes;
	}

}

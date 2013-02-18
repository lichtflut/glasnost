/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.data.GISTestConstants;
import de.lichtflut.glasnost.is.data.ResourceSchemaFactory;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.schema.model.ResourceSchema;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.common.PanelTitle;

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
	public void testCatalogProposalPanelViewState() {
		ResourceSchema schema = ResourceSchemaFactory.buildDatatCenter();

		Set<SNClass> superclasses = new HashSet<SNClass>();
		superclasses.add(SNClass.from(GISTestConstants.PHYSICAL_MACHINE));
		when(schemaManager.findSchemaForType(type)).thenReturn(schema);
		when(typeManager.getSuperClasses(GISTestConstants.PHYSICAL_MACHINE)).thenReturn(superclasses);
		RBWebSession session = (RBWebSession) tester.getSession();
		session.getHistory().view(new EntityHandle());

		CatalogProposalPanel panel = new CatalogProposalPanel("panel", Model.of(GIS.DATA_CENTER));

		tester.startComponentInPage(panel);

		assertRenderedPanel(CatalogProposalPanel.class, "panel");
		tester.assertInvisible("panel");
	}

	/**
	 * Test method for {@link de.lichtflut.glasnost.is.components.softwareCatalog.CatalogProposalPanel#CatalogProposalPanel(java.lang.String, org.apache.wicket.model.IModel)}.
	 */
	@Test
	public void testCatalogProposalPanelEditState() {
		ResourceSchema schema = ResourceSchemaFactory.buildDatatCenter();
		List<PropertyDeclaration> referencedTypes = getResourceReferencedTypes(schema);

		Set<SNClass> superclasses = new HashSet<SNClass>();
		superclasses.add(SNClass.from(GIS.SOFTWARE_ITEM));
		when(schemaManager.findSchemaForType(type)).thenReturn(schema);
		when(typeManager.getSuperClasses(GISTestConstants.PHYSICAL_MACHINE)).thenReturn(superclasses);
		when(typeManager.getSuperClasses(GIS.DATA_CENTER)).thenReturn(superclasses);
		RBWebSession session = (RBWebSession) tester.getSession();
		session.getHistory().create(EntityHandle.forType(schema.getDescribedType()));

		CatalogProposalPanel panel = new CatalogProposalPanel("panel", Model.of(GIS.DATA_CENTER)){
			@Override
			protected Form<?> getExternalForm() {
				return new Form<Void>("form");
			}
		};

		tester.startComponentInPage(panel);

		assertRenderedPanel(panel.getClass(), "panel");
		tester.assertComponent("panel:title", PanelTitle.class);
		tester.assertListView("panel:proposals", referencedTypes);
	}

	// ------------------------------------------------------

	private List<PropertyDeclaration> getResourceReferencedTypes(final ResourceSchema schema) {
		ArrayList<PropertyDeclaration> referencedTypes = new ArrayList<PropertyDeclaration>();
		for (PropertyDeclaration decl : schema.getPropertyDeclarations()) {
			if(decl.hasConstraint() && null != decl.getConstraint().getTypeConstraint()){
				referencedTypes.add(decl);
			}
		}
		return referencedTypes;
	}

}

/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SNResource;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.RBSystem;
import de.lichtflut.rb.core.common.SchemaIdentifyingType;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.catalog.CatalogPanel;

/**
 * <p>
 * This page displays the Softwarecatalog for browsing
 * </p>
 * Created: Jan 31, 2013
 *
 * @author Ravi Knox
 */
public class SoftwareCatalogPage extends RBBasePage {

	@SpringBean
	private SemanticNetworkService networkService;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor
	 */
	public SoftwareCatalogPage() {
		add(new CatalogPanel("catalog", Model.of(GIS.SOFTWARE_ITEM)){
			@Override
			protected void applyAction(final IModel<ResourceID> model) {
				SemanticNode prototype = getPrototype(model.getObject());
				PageParameters parameters;
				if(prototype != null){
					ResourceNode copy = copy(prototype);
					networkService.attach(copy);
					setBrowingStep(copy, false);
					parameters = new PageParameters();
					parameters.add(CommonParams.PARAM_RESOURCE_ID, copy);
					parameters.add(DisplayMode.PARAMETER, DisplayMode.EDIT);
				}else{
					SNClass schema = SchemaIdentifyingType.of(model.getObject());
					setBrowingStep(model.getObject(), true);
					parameters = new PageParameters();
					parameters.add(CommonParams.PARAM_RESOURCE_TYPE, schema);
					parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
				}
				setResponsePage(DevOpsItemPage.class, parameters);
			}

			private ResourceNode copy(final SemanticNode prototype) {
				ResourceNode node = new SNResource();
				for (Statement stmt: prototype.asResource().getAssociations()) {
					node.addAssociation(stmt.getPredicate(), stmt.getObject());
				}
				return node;
			}

			private SemanticNode getPrototype(final ResourceID classId) {
				ResourceNode classNode = networkService.find(classId.getQualifiedName());
				SemanticNode node = SNOPS.fetchObject(classNode, RBSystem.HAS_PROTOTYPE);
				if(null != node){
					return node;
				}
				return null;
			}

			private void setBrowingStep(final ResourceID id, final boolean typeInfo) {
				EntityHandle handle;
				if(typeInfo){
					handle = EntityHandle.forType(id);
				} else{
					handle = EntityHandle.forID(id);
				}
				RBWebSession.get().getHistory().create(handle);
			}
		});
	}


}

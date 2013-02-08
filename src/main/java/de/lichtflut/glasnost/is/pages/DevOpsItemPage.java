package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.components.devops.items.ItemEditorInfoPanel;
import de.lichtflut.glasnost.is.components.softwareCatalog.CatalogProposalPanel;
import de.lichtflut.rb.application.resourceview.EntityDetailPage;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.components.ResourceBrowsingPanel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

/**
 * <p>
 *  Page based on EntityDetailPage displaying one DevOps item.
 * </p>
 *
 * <p>
 *  Created 04.12.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class DevOpsItemPage extends EntityDetailPage {

	@SpringBean
	private TypeManager typeManager;

	// ---------------- Constructor -------------------------

	public DevOpsItemPage(final PageParameters params) {
		super(params);
	}

	public DevOpsItemPage() {
	}

	// ----------------------------------------------------

	@Override
	protected Component createNotePadPanel(final String id, final IModel<ResourceID> model) {
		// FIXME should appear only with devOps / SoftwareItems
		SNClass typeOfResource = typeManager.getTypeOfResource(model.getObject());
		return new CatalogProposalPanel(id, new Model<ResourceID>(typeOfResource));
	}
	@Override
	protected Component createBrowser(final String componentID) {
		return new ResourceBrowsingPanel(componentID) {

			@Override
			public void createReferencedEntity(final EntityHandle handle, final ResourceID predicate) {
				super.createReferencedEntity(handle, predicate);
			}

			@Override
			protected Component createRelationshipView(final String id, final IModel<RBEntity> model) {
				return super.createRelationshipView(id, model);
			}

			@Override
			protected Component createInfoPanel(final String id, final IModel<RBEntity> model) {
				return new ItemEditorInfoPanel(id, model);
			}

			@Override
			protected Component createEntityPanel(final String id, final IModel<RBEntity> model) {
				return super.createEntityPanel(id, model);
			}

			@Override
			protected Component createClassifyPanel(final String id, final IModel<RBEntity> model) {
				return super.createClassifyPanel(id, model);
			}

			@Override
			protected void onSave(final IModel<RBEntity> model) {
			}
		};
	}

}

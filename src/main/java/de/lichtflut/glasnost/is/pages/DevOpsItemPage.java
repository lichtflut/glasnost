package de.lichtflut.glasnost.is.pages;

import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.visibleIf;
import static de.lichtflut.rb.webck.models.ConditionalModel.and;
import static de.lichtflut.rb.webck.models.ConditionalModel.isNotNull;
import static de.lichtflut.rb.webck.models.ConditionalModel.or;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.devops.items.ItemEditorInfoPanel;
import de.lichtflut.glasnost.is.components.softwareCatalog.CatalogProposalPanel;
import de.lichtflut.glasnost.is.dialog.CatalogDialog;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.application.resourceview.EntityDetailPage;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.entity.RBField;
import de.lichtflut.rb.core.services.EntityManager;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.ResourceBrowsingPanel;
import de.lichtflut.rb.webck.components.common.DialogHoster;
import de.lichtflut.rb.webck.components.notes.NotePadPanel;
import de.lichtflut.rb.webck.models.ConditionalModel;

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

	@SpringBean
	private SemanticNetworkService networkService;

	@SpringBean
	private EntityManager entityManager;

	// ---------------- Constructor -------------------------

	public DevOpsItemPage(final PageParameters params) {
		super(params);
	}

	public DevOpsItemPage() {
		super();
	}

	// ----------------------------------------------------

	@Override
	protected Component createRightSideBar(final String id, final IModel<ResourceID> model) {
		RepeatingView view = new RepeatingView(id);

		LoadableDetachableModel<ResourceID> type = new LoadableDetachableModel<ResourceID>() {
			@Override
			protected ResourceID load() {
				return typeManager.getTypeOfResource(model.getObject());
			}
		};
		CatalogProposalPanel proposal = getProposalPanel(view.newChildId(), type);
		proposal.add(visibleIf(and(isNotNull(type), or(isSubclassOf(model, GIS.CONFIGURATION_ITEM), isSubclassOf(model, GIS.DATA_CENTER)))));

		view.add(proposal);

		view.add(new NotePadPanel(view.newChildId(), model));
		return view;
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

			@Override
			protected void onCancel() {
				RBWebSession.get().getHistory().back();
				setResponsePage(DevOpsItemPage.class, DevOpsItemPage.this.getPageParameters());
			}
		};
	}

	// ------------------------------------------------------

	private CatalogProposalPanel getProposalPanel(final String id, final IModel<ResourceID> typeOfResource) {
		final Component browsingPanel = get(EntityDetailPage.BROWSER_ID);
		@SuppressWarnings("unchecked")
		final IModel<RBEntity> entity = (IModel<RBEntity>) browsingPanel.getDefaultModel();
		return new CatalogProposalPanel(id, entity){
			@Override
			protected void applyActions(final AjaxRequestTarget target, final IModel<RBField> field, final IModel<ResourceID> typeConstraint) {
				DialogHoster hoster = findParent(DialogHoster.class);
				hoster.openDialog(new CatalogDialog(hoster.getDialogID(), typeConstraint){
					@Override
					protected void applyActions(final IModel<RBEntity> model) {
						entity.getObject().getField(field.getObject().getPredicate()).addValue(model.getObject().getID());

						entityManager.store(model.getObject());
						entityManager.store(entity.getObject());

						PageParameters parameters = new PageParameters();
						parameters.add(CommonParams.PARAM_RESOURCE_ID, entity.getObject().getID());
						parameters.add(DisplayMode.PARAMETER, DisplayMode.EDIT);
						setResponsePage(DevOpsItemPage.class, parameters);
					}

				});
			}
		};
	}

	private ConditionalModel<Boolean> isSubclassOf(final IModel<ResourceID> actual, final ResourceID superclass){
		return new ConditionalModel<Boolean>() {

			@Override
			public boolean isFulfilled() {
				//checkif a direct assocciation exists
				ResourceNode node = actual.getObject().asResource();
				networkService.attach(node);
				if (SNOPS.objects(node, RDF.TYPE).contains(superclass)) {
					return true;
				}

				// check if a subclass relation exists
				SNClass type = typeManager.getTypeOfResource(actual.getObject());
				Set<SNClass> superClasses = typeManager.getSuperClasses(type);
				if (superClasses.contains(superclass)) {
					return true;
				}

				return false;
			}
		};
	}
}

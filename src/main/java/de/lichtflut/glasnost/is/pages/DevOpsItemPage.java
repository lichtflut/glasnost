package de.lichtflut.glasnost.is.pages;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
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

import de.lichtflut.glasnost.is.components.devops.items.ItemEditorInfoPanel;
import de.lichtflut.glasnost.is.components.softwareCatalog.CatalogProposalPanel;
import de.lichtflut.glasnost.is.dialog.CatalogDialog;
import de.lichtflut.rb.application.resourceview.EntityDetailPage;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.services.EntityManager;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.browsing.EntityAttributeApplyAction;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.ResourceBrowsingPanel;
import de.lichtflut.rb.webck.components.common.DialogHoster;
import de.lichtflut.rb.webck.components.notes.NotePadPanel;
import de.lichtflut.rb.webck.events.ModelChangeEvent;
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
		// TODO set only visibility if Viewstate x or y
		//		proposal.add(visibleIf(and(isNotNull(type), or(isSubclassOf(model, GIS.CONFIGURATION_ITEM), isSubclassOf(model, GIS.DATA_CENTER)))));

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
		};
	}

	// ------------------------------------------------------

	private CatalogProposalPanel getProposalPanel(final String id, final IModel<ResourceID> typeOfResource) {
		final Component browsingPanel = get(EntityDetailPage.BROWSER_ID);
		IModel<ResourceID> type = new LoadableDetachableModel<ResourceID>() {

			@Override
			protected ResourceID load() {
				@SuppressWarnings("unchecked")
				final IModel<RBEntity> entity = (IModel<RBEntity>) browsingPanel.getDefaultModel();
				if(entity.getObject() != null){
					return entity.getObject().getType();
				}
				return null;
			}
		};
		return new CatalogProposalPanel(id, type){
			@Override
			protected void applyActions(final AjaxRequestTarget target, final IModel<PropertyDeclaration> decl, final IModel<ResourceID> typeConstraint) {
				DialogHoster hoster = findParent(DialogHoster.class);
				hoster.openDialog(new CatalogDialog(hoster.getDialogID(), typeConstraint){
					@Override
					protected void applyActions(final IModel<ResourceID> model) {
						((ResourceBrowsingPanel) browsingPanel).submitForm();
						EntityAttributeApplyAction action = new EntityAttributeApplyAction((RBEntity) browsingPanel.getDefaultModelObject(), decl.getObject().getPropertyDescriptor());
						EntityHandle handle = EntityHandle.forType(typeConstraint.getObject());
						RBWebSession.get().getHistory().createReference(handle, action);
						Page page = getPage();
						send(page, Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.ENTITY));
						send(page, Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.RELATIONSHIP));
						de.lichtflut.glasnost.is.events.ModelChangeEvent<ResourceID> mce = new de.lichtflut.glasnost.is.events.ModelChangeEvent<ResourceID>(typeConstraint.getObject(), de.lichtflut.glasnost.is.events.ModelChangeEvent.PROPOSAL_UPDATE);
						send(getPage(), Broadcast.BREADTH, mce);
						closeDialog();
					}
				});
			}
		};
	}

	private ConditionalModel<Boolean> isSubclassOf(final IModel<ResourceID> actual, final ResourceID superclass){
		return new ConditionalModel<Boolean>() {

			@Override
			public boolean isFulfilled() {
				if(null == actual.getObject()){
					return false;
				}
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

/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.lichtflut.glasnost.is.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.devops.items.ItemEditorInfoPanel;
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
import de.lichtflut.rb.webck.components.catalog.CatalogProposalPanel;
import de.lichtflut.rb.webck.components.common.DialogHoster;
import de.lichtflut.rb.webck.components.notes.NotePadPanel;
import de.lichtflut.rb.webck.events.ModelChangeEvent;

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
public class GlasnostEntityDetailPage extends EntityDetailPage {

	@SpringBean
	private TypeManager typeManager;

	@SpringBean
	private SemanticNetworkService networkService;

	@SpringBean
	private EntityManager entityManager;

	// ---------------- Constructor -------------------------

	public GlasnostEntityDetailPage(final PageParameters params) {
		super(params);
	}

	public GlasnostEntityDetailPage() {
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
		proposal.setOutputMarkupPlaceholderTag(true);

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
		IModel<ResourceID> type = new LoadableDetachableModel<ResourceID>() {

			@Override
			protected ResourceID load() {
				@SuppressWarnings("unchecked")
				final IModel<RBEntity> entity = (IModel<RBEntity>) getBrowsingPanel().getDefaultModel();
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
						setBrowingStep(decl, typeConstraint);
						sendEvents(typeConstraint);
						close();
					}

					private void setBrowingStep(final IModel<PropertyDeclaration> decl,
							final IModel<ResourceID> typeConstraint) {
						EntityAttributeApplyAction action = new EntityAttributeApplyAction((RBEntity) getBrowsingPanel().getDefaultModelObject(), decl.getObject().getPropertyDescriptor());
						EntityHandle handle = EntityHandle.forType(typeConstraint.getObject());
						RBWebSession.get().getHistory().createReference(handle, action);
					}

					private void sendEvents(final IModel<ResourceID> typeConstraint) {
						Page page = getPage();
						send(page, Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.ENTITY));
						send(page, Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.RELATIONSHIP));
						ModelChangeEvent<ResourceID> mce = new ModelChangeEvent<ResourceID>(typeConstraint.getObject(), ModelChangeEvent.PROPOSAL_UPDATE);
						send(getPage(), Broadcast.BREADTH, mce);
					}

				});
			}

			@Override
			protected Form<?> getExternalForm() {
				return getBrowsingPanel().getForm();
			}

			@Override
			protected List<SNClass> getAcceptableSuperclasses() {
				List<SNClass> list = new ArrayList<SNClass>();
				list.add(SNClass.from(GIS.SOFTWARE_ITEM));
				return list;
			}
		};
	}

	private ResourceBrowsingPanel getBrowsingPanel() {
		return (ResourceBrowsingPanel) get(EntityDetailPage.BROWSER_ID);
	}
}

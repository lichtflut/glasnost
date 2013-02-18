/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.events.ModelChangeEvent;
import de.lichtflut.rb.core.schema.model.Datatype;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.schema.model.ResourceSchema;
import de.lichtflut.rb.core.services.SchemaManager;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import de.lichtflut.rb.webck.components.common.PanelTitle;
import de.lichtflut.rb.webck.models.basic.DerivedModel;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

/**
 * <p>
 * This Panel displays various links that match the SChema for a given type.
 * </p>
 * Created: Feb 5, 2013
 * 
 * @author Ravi Knox
 */
public class CatalogProposalPanel extends Panel {

	@SpringBean
	private SchemaManager schemaManager;

	@SpringBean
	private TypeManager typeManager;

	private final IModel<ResourceID> type;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 * @param model {@link IModel} containing the type
	 */
	public CatalogProposalPanel(final String id, final IModel<ResourceID> model) {
		super(id);
		type = model;
		add(new PanelTitle("title", new ResourceModel("title")));

		addListView("proposals", getSchemaFor(type));

		setOutputMarkupId(true);
	}

	// ------------------------------------------------------

	private IModel<ResourceSchema> getSchemaFor(final IModel<ResourceID> model) {
		return new DerivedModel<ResourceSchema, ResourceID>(model) {
			@Override
			protected ResourceSchema derive(final ResourceID original) {
				return schemaManager.findSchemaForType(model.getObject());
			}
		};
	}

	/**
	 * @return a List containing all (super)types, that a {@link PropertyDeclaration} can  reference to, in order to get a proposal entry.
	 */
	protected List<SNClass> getAcceptableSuperclasses(){
		List<SNClass> list = new ArrayList<SNClass>();
		list.add(SNClass.from(GIS.SOFTWARE_ITEM));
		return list;
	}

	protected void applyActions(final AjaxRequestTarget target, final IModel<PropertyDeclaration> decl, final IModel<ResourceID> typeConstraint) {
	}

	/**
	 * Returns a form, that will be submitted when the user wants to create a catalog item.
	 * @return a {@link Form}
	 */
	protected Form<?> getExternalForm() {
		return null;
	}

	// ------------------------------------------------------

	private void addListView(final String id, final IModel<ResourceSchema> model) {
		ListView<PropertyDeclaration> view = new ListView<PropertyDeclaration>(id, getReferencedTypes(model)) {
			@Override
			protected void populateItem(final ListItem<PropertyDeclaration> item) {
				AjaxSubmitLink link = new AjaxSubmitLink("link", getExternalForm()) {
					@Override
					protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
						IModel<PropertyDeclaration> field = item.getModel();
						ResourceID constraint = field.getObject().getConstraint().getTypeConstraint();
						IModel<ResourceID> typeConstraint = Model.of(constraint);
						CatalogProposalPanel.this.applyActions(target, field, typeConstraint);
					}

				};
				ResourceLabelModel labelModel = new ResourceLabelModel(item.getModel().getObject().getPropertyDescriptor());
				link.add(new Label("linkLabel", new StringResourceModel("link.label", new Model<String>(), labelModel)));
				item.add(link);
			}

		};

		add(view);
	}

	private IModel<List<PropertyDeclaration>> getReferencedTypes(final IModel<ResourceSchema> model) {
		return new DerivedModel<List<PropertyDeclaration>, ResourceSchema>(model) {
			@Override
			protected List<PropertyDeclaration> derive(final ResourceSchema original) {
				List<PropertyDeclaration> referencedTypes = new ArrayList<PropertyDeclaration>();
				for (PropertyDeclaration decl: model.getObject().getPropertyDeclarations()) {
					if(checkForAcceptedTypeReference(decl)){
						referencedTypes.add(decl);
					}
				}
				return referencedTypes;
			}
		};
	}

	private boolean checkForAcceptedTypeReference(final PropertyDeclaration decl) {
		if(!Datatype.RESOURCE.name().equals(decl.getDatatype().name())){
			return false;
		}
		if(null == decl.getConstraint().getTypeConstraint()){
			return false;
		}
		ResourceID constraint = decl.getConstraint().getTypeConstraint();
		if(getAcceptableSuperclasses().contains(constraint)){
			return true;
		}
		Set<SNClass> superClasses = typeManager.getSuperClasses(constraint);
		superClasses.retainAll(getAcceptableSuperclasses());
		if(superClasses.size() > 0) {
			return true;
		}
		return false;
	}

	// ------------------------------------------------------

	@Override
	public void onEvent(final IEvent<?> event) {
		ModelChangeEvent<Object> mce = ModelChangeEvent.from(event);
		if(mce.isAbout(ModelChangeEvent.PROPOSAL_UPDATE)){
			type.setObject((ResourceID)mce.getPayload());
			RBAjaxTarget.add(CatalogProposalPanel.this);
		}
	}
}

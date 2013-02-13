/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.entity.RBField;
import de.lichtflut.rb.core.schema.model.Datatype;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.services.SchemaManager;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.components.common.PanelTitle;
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

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 * @param model {@link IModel} containing the type
	 */
	public CatalogProposalPanel(final String id, final IModel<RBEntity> model) {
		super(id, model);

		add(new PanelTitle("title", new ResourceModel("title")));
		addListView("proposals", model);
	}

	// ------------------------------------------------------

	/**
	 * @return a List containing all (super)types, that a {@link PropertyDeclaration} can  reference to, in order to get a proposal entry.
	 */
	protected List<SNClass> getAcceptableSuperclasses(){
		List<SNClass> list = new ArrayList<SNClass>();
		list.add(SNClass.from(GIS.SOFTWARE_ITEM));
		return list;
	}

	protected void applyActions(final AjaxRequestTarget target, final IModel<RBField> field, final IModel<ResourceID> typeConstraint) {
	}

	// ------------------------------------------------------

	private void addListView(final String id, final IModel<RBEntity> model) {
		ListView<RBField> view = new ListView<RBField>(id, getReferencedTypes(model)) {

			@Override
			protected void populateItem(final ListItem<RBField> item) {
				AjaxLink<Void> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(final AjaxRequestTarget target) {
						IModel<RBField> field = item.getModel();
						IModel<ResourceID> typeConstraint = Model.of(field.getObject().getConstraint().getTypeConstraint());
						CatalogProposalPanel.this.applyActions(target, field, typeConstraint);
					}
				};
				ResourceLabelModel labelModel = new ResourceLabelModel(item.getModel().getObject().getPredicate());
				link.add(new Label("linkLabel", new StringResourceModel("link.label", new Model<String>(), labelModel)));
				item.add(link);
			}

		};
		add(view);
	}

	private IModel<List<RBField>> getReferencedTypes(final IModel<RBEntity> model) {
		return new LoadableDetachableModel<List<RBField>>() {
			@Override
			protected List<RBField> load() {
				return getResourceReferencedTypes(model);
			}

			private List<RBField> getResourceReferencedTypes(final IModel<RBEntity> model) {
				List<RBField> referencedTypes = new ArrayList<RBField>();
				for (RBField field : model.getObject().getAllFields()) {
					if(checkForAcceptedTypeReference(field)){
						referencedTypes.add(field);
					}
				}
				return referencedTypes;
			}
		};
	}

	private boolean checkForAcceptedTypeReference(final RBField field) {
		if(!Datatype.RESOURCE.name().equals(field.getDataType().name())){
			return false;
		}
		if(null == field.getConstraint().getTypeConstraint()){
			return false;
		}
		ResourceID constraint = field.getConstraint().getTypeConstraint();
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

}

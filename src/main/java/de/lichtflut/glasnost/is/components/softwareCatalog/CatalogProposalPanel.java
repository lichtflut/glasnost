/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import java.util.ArrayList;
import java.util.List;

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

import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.schema.model.ResourceSchema;
import de.lichtflut.rb.core.services.SchemaManager;
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

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 * @param model {@link IModel} containing the type
	 */
	public CatalogProposalPanel(final String id, final IModel<ResourceID> model) {
		super(id, model);

		add(new GlasnostTitle("title", new ResourceModel("title")));
		addListView("proposals", model);
	}

	// ------------------------------------------------------

	private void addListView(final String id, final IModel<ResourceID> model) {
		ListView<ResourceID> view = new ListView<ResourceID>(id, getReferencedTypes(model)) {

			@Override
			protected void populateItem(final ListItem<ResourceID> item) {
				AjaxLink<Void> link = new AjaxLink<Void>("link") {

					@Override
					public void onClick(final AjaxRequestTarget target) {
						// TODO Auto-generated method stub

					}
				};
				ResourceLabelModel labelModel = new ResourceLabelModel(item.getModel());
				link.add(new Label("linkLabel", new StringResourceModel("link.label", new Model<String>(), labelModel)));
				item.add(link);

			}

		};
		add(view);
	}

	private IModel<List<ResourceID>> getReferencedTypes(final IModel<ResourceID> model) {
		return new LoadableDetachableModel<List<ResourceID>>() {
			@Override
			protected List<ResourceID> load() {
				return getResourceReferencedTypes(schemaManager.findSchemaForType(model.getObject()));
			}

			private List<ResourceID> getResourceReferencedTypes(final ResourceSchema schema) {
				ArrayList<ResourceID> referencedTypes = new ArrayList<ResourceID>();
				for (PropertyDeclaration decl : schema.getPropertyDeclarations()) {
					if(decl.hasConstraint() && null != decl.getConstraint().getTypeConstraint()){
						referencedTypes.add(decl.getConstraint().getTypeConstraint());
					}
				}
				return referencedTypes;
			}
		};
	}


}

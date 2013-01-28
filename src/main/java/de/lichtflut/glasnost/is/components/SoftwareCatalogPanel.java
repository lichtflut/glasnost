/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.models.ConditionalModel;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

/**
 * <p>
 * This Panel offers some predefined SoftwareItems to choose from.
 * </p>
 * Created: Jan 28, 2013
 * 
 * @author Ravi Knox
 */
public class SoftwareCatalogPanel extends Panel {

	@SpringBean
	private TypeManager typeManager;

	IModel<ResourceID> root = new Model<ResourceID>();

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 */
	public SoftwareCatalogPanel(final String id) {
		super(id);
		addCategoriesTitle("categoriesTitle",new ResourceModel("title.category"));
		createCategoriesList("categoriesList");

		addListView("containerList");
	}

	// ------------------------------------------------------

	private void addListView(final String id) {
		ListView<ResourceNode> list = new ListView<ResourceNode>(id) {
			@Override
			protected void populateItem(final ListItem<ResourceNode> item) {
				item.add(new Label("itemListTitle", new ResourceLabelModel(item.getModelObject())));
				item.add(createSubListForType("list", item.getModel()));
			}

			private Component createSubListForType(final String id, final IModel<ResourceNode> model) {

				return null;
			}
		};
		list.add(ConditionalBehavior.visibleIf(ConditionalModel.isNotNull(root)));
		add(list);
	}

	protected IModel<? extends List<ResourceNode>> getAllCategories() {
		return new LoadableDetachableModel<List<ResourceNode>>() {
			@Override
			protected List<ResourceNode> load() {
				// Change to custom Query, what if softwareItem is subtype of xyz
				List<ResourceNode> list = new ArrayList<ResourceNode>();
				Set<SNClass> categories = typeManager.getSubClasses(GIS.SOFTWARE_ITEM);
				for (SemanticNode temp : categories) {
					list.add(SNClass.from(temp));
				}
				return list;
			}
		};
	}

	// ------------------------------------------------------

	private void addCategoriesTitle(final String id, final ResourceModel resourceModel) {
		add(new GlasnostTitle(id, resourceModel));
	}

	private void createCategoriesList(final String id) {
		ListView<ResourceNode> list = new ListView<ResourceNode>(id, getAllCategories()) {
			@Override
			protected void populateItem(final ListItem<ResourceNode> item) {
				ResourceNode category = item.getModelObject();
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(final AjaxRequestTarget target) {
						// TODO Auto-generated method stub
					}
				};
				link.add(new Label("label", new ResourceLabelModel(category)));
				item.add(link);
			}
		};
		add(list);
	}

}

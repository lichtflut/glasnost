/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components;

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
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.services.SemanticNetworkService;
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
	private SemanticNetworkService networkService;

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
	}

	// ------------------------------------------------------

	protected IModel<? extends List<ResourceNode>> getAllCategories() {
		return new LoadableDetachableModel<List<ResourceNode>>() {
			@Override
			protected List<ResourceNode> load() {
				// Change to custom Query, what if softwareItem is subtype of xyz
				List<ResourceNode> list = new ArrayList<ResourceNode>();
				ResourceNode node = networkService.resolve(GIS.SOFTWARE_ITEM);
				Set<SemanticNode> categories = SNOPS.objects(node, RDFS.SUB_CLASS_OF);
				for (SemanticNode temp : categories) {
					list.add((ResourceNode) temp);
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

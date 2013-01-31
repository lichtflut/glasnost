/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
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

	private final IModel<List<ResourceNode>> root = new ListModel<ResourceNode>(new LinkedList<ResourceNode>());

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 */
	public SoftwareCatalogPanel(final String id) {
		super(id);

		addCategoriesPanel("categories");
		addSpecifyingList("specifyingList");

		setOutputMarkupId(true);
	}

	// ------------------------------------------------------

	/**
	 * @param base Base class
	 * @return a IModel containing all subclasses for a given type
	 */
	protected IModel<? extends List<ResourceNode>> getAllSubClassesFor(final ResourceID base) {
		return new LoadableDetachableModel<List<ResourceNode>>() {
			@Override
			protected List<ResourceNode> load() {
				List<ResourceNode> list = new LinkedList<ResourceNode>();
				Set<SNClass> categories = typeManager.getSubClasses(base);
				for (SemanticNode temp : categories) {
					list.add(SNClass.from(temp));
				}
				Collections.sort(list, getNodeComparator());
				return list;
			}
		};
	}

	/**
	 * @return a comparator for all SoftwareItems
	 */
	Comparator<ResourceNode> getNodeComparator(){
		return new Comparator<ResourceNode>() {
			@Override
			public int compare(final ResourceNode o1, final ResourceNode o2) {
				return o1.getQualifiedName().toURI().compareTo(o2.getQualifiedName().toURI());
			}
		};
	}

	// ------------------------------------------------------

	private void addCategoriesPanel(final String id) {
		Component panel = new SoftwareCategoriesPanel(id){
			@Override
			protected IModel<? extends List<ResourceNode>> getAllSubClassesFor(final ResourceID base) {
				return SoftwareCatalogPanel.this.getAllSubClassesFor(base);
			}

			@Override
			Comparator<ResourceNode> getNodeComparator() {
				return SoftwareCatalogPanel.this.getNodeComparator();
			}

			@Override
			protected void applyActions(final ListItem<ResourceNode> item, final AjaxRequestTarget target) {
				if(addToList(item)){
					RBAjaxTarget.add(SoftwareCatalogPanel.this);
				}
			}
		};
		add(panel);
	}

	private void addSpecifyingList(final String id) {
		ListView<ResourceNode> list = new ListView<ResourceNode>(id, root) {
			@Override
			protected void populateItem(final ListItem<ResourceNode> item) {
				// TODO onClick-title remove all below
				item.add(new GlasnostTitle("itemListTitle", new ResourceLabelModel(item.getModelObject())));
				item.add(createSubListForType("subList", item.getModel()));
			}
		};
		list.add(ConditionalBehavior.visibleIf(ConditionalModel.isNotEmpty(root)));
		add(list);
	}

	private Component createSubListForType(final String id, final IModel<ResourceNode> model) {
		// get all nodes of type model and list 'em up
		IModel<? extends List<ResourceNode>> subClasses = getAllSubClassesFor(model.getObject());
		ListView<ResourceNode> subList = new ListView<ResourceNode>(id,subClasses) {
			@Override
			protected void populateItem(final ListItem<ResourceNode> item) {
				AjaxLink<?> link = new AjaxLink<Void>("subLink") {
					@Override
					public void onClick(final AjaxRequestTarget target) {
						if(addToList(item)){
							RBAjaxTarget.add(SoftwareCatalogPanel.this);
						}
					}
				};
				link.add(new Label("subLabel", new ResourceLabelModel(item.getModel())));
				item.add(link);
			}
		};
		return subList;
	}

	private boolean addToList(final ListItem<ResourceNode> item) {
		boolean success = false;
		if(!root.getObject().contains(item.getModelObject())){
			root.getObject().add(item.getModelObject());
			success = true;
		}
		return success;
	}

}

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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNClass;

import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.rb.core.common.SchemaIdentifyingType;
import de.lichtflut.rb.core.services.SchemaManager;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import de.lichtflut.rb.webck.components.common.DialogHoster;
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
// TODO remove style tag from html, create table dynamically
public class CatalogPanel extends Panel {

	@SpringBean
	private TypeManager typeManager;

	@SpringBean
	private SemanticNetworkService networkService;

	@SpringBean
	private SchemaManager schemaManager;

	private final IModel<List<ResourceNode>> root = new ListModel<ResourceNode>(new LinkedList<ResourceNode>());

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 * @param type Superclass of all catalog items
	 */
	public CatalogPanel(final String id, final ResourceID type) {
		super(id);

		addCategoriesPanel("categories",type);
		addSpecifyingList("specifyingList", type);

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

	private void addCategoriesPanel(final String id, final ResourceID type) {
		Component panel = new CategoriesPanel(id, type){
			@Override
			protected IModel<? extends List<ResourceNode>> getAllSubClassesFor(final ResourceID base) {
				return CatalogPanel.this.getAllSubClassesFor(base);
			}

			@Override
			Comparator<ResourceNode> getNodeComparator() {
				return CatalogPanel.this.getNodeComparator();
			}

			@Override
			protected void applyActions(final ListItem<ResourceNode> item, final AjaxRequestTarget target) {
				root.getObject().clear();
				root.getObject().add(item.getModelObject());
				RBAjaxTarget.add(CatalogPanel.this);
			}
		};
		add(panel);
	}

	private void addSpecifyingList(final String id, final ResourceID type) {
		ListView<ResourceNode> list = new ListView<ResourceNode>(id, root) {
			@Override
			protected void populateItem(final ListItem<ResourceNode> item) {
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
						// nomore subclasses, try to find a schema
						if(typeManager.getSubClasses(item.getModelObject()).isEmpty()){
							openDialog(item.getModel());
						}
						else if(addToList(item, model)){
							RBAjaxTarget.add(CatalogPanel.this);
						}
					}

					private void openDialog(final IModel<ResourceNode> model) {
						ResourceNode node = networkService.find(model.getObject().getQualifiedName());
						SNClass identifyingType = SchemaIdentifyingType.of(node);
						final DialogHoster dialogHoster = findParent(DialogHoster.class);
						dialogHoster.openDialog(new CreateEntityDialog(dialogHoster.getDialogID(), new Model<ResourceID>(identifyingType)));
					}
				};
				link.add(new Label("subLabel", new ResourceLabelModel(item.getModel())));
				item.add(link);
			}
		};
		return subList;
	}

	private boolean addToList(final ListItem<ResourceNode> item, final IModel<ResourceNode> superClass) {
		boolean success = false;
		int index = root.getObject().indexOf(superClass.getObject());
		while(root.getObject().size() > index+1){
			root.getObject().remove(index+1);
		}
		if(!root.getObject().contains(item.getModelObject())){
			root.getObject().add(item.getModelObject());
			success = true;
		}
		return success;
	}

}

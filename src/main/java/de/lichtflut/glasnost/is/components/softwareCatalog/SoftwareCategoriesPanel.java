/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.views.SNClass;
import org.arastreju.sge.structure.OrderBySerialNumber;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.rb.core.services.TypeManager;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

/**
 * <p>
 * Display all direct subclasses of {@link GIS#SOFTWARE_ITEM}
 * </p>
 * Created: Jan 31, 2013
 *
 * @author Ravi Knox
 */
public class SoftwareCategoriesPanel extends Panel {

	@SpringBean
	private TypeManager typeManager;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Componet id
	 */
	public SoftwareCategoriesPanel(final String id) {
		super(id);

		addCategoriesTitle("categoriesTitle", new ResourceModel("title.category"));
		createCategoriesList("categoriesList");
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
		return new OrderBySerialNumber();
	}

	protected void applyActions(final ListItem<ResourceNode> item, final AjaxRequestTarget target) {
	}

	// ------------------------------------------------------

	private void addCategoriesTitle(final String id, final IModel<String> resourceModel) {
		add(new GlasnostTitle(id, resourceModel));
	}

	private void createCategoriesList(final String id) {
		ListView<ResourceNode> list = new ListView<ResourceNode>(id, getAllSubClassesFor(GIS.SOFTWARE_ITEM)) {
			@Override
			protected void populateItem(final ListItem<ResourceNode> item) {
				ResourceNode category = item.getModelObject();
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(final AjaxRequestTarget target) {
						applyActions(item, target);
					}

				};
				link.add(new Label("label", new ResourceLabelModel(category)));
				item.add(link);
			}
		};
		add(list);
	}

}

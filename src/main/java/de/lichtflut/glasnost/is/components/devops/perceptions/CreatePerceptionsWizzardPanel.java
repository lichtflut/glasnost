/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.nodes.ResourceNode;

import de.lichtflut.glasnost.is.events.ModelChangeEvent;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.webck.components.form.RBCancelButton;
import de.lichtflut.rb.webck.components.form.RBStandardButton;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

/**
 * <p>
 * This Panel allows you to create multiple perceptions at once.
 * </p>
 * Created: Jan 9, 2013
 * 
 * @author Ravi Knox
 */
public class CreatePerceptionsWizzardPanel extends Panel {

	@SpringBean
	private PerceptionDefinitionService perceptionDefinitionService;

	private PerceptionWizzardListModel model;
	private IModel<List<ResourceNode>> categories;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Comonent id
	 */
	public CreatePerceptionsWizzardPanel(final String id) {
		super(id);
		initializeModel();

		Form<?> form = new Form<Void>("form");
		addPerceptions("list", form);
		add(form);
		addSaveButton("save", form);
		addCancelButton("cancel", form);

		setOutputMarkupId(true);
	}

	// ------------------------------------------------------

	protected void onCancel(final AjaxRequestTarget target, final Form<?> form) {
	}

	protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
		// TODO overload store(List<Perception) method.
		for (List<Perception> list : model.getObject()) {
			for (Perception perception : list) {
				perceptionDefinitionService.store(perception);
			}
		}
		send(getPage(), Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.PERCEPTION));
	}

	// ------------------------------------------------------

	private void addPerceptions(final String id, final Form<?> form) {
		ListView<List<Perception>> listView = new ListView<List<Perception>>(id, model) {
			@Override
			protected void populateItem(final ListItem<List<Perception>> item) {
				addSecondLvlListView(item);
			}
		};
		form.add(listView);
	}

	private void addSecondLvlListView(final ListItem<List<Perception>> item) {
		final ListView<Perception> list = createListView(item);
		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		container.add(list);

		AjaxSubmitLink link = new AjaxSubmitLink("link") {
			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				model.addPerceptionFor(categories.getObject().get(item.getIndex()));
				target.add(container);
			}
		};
		IModel<String> labelModel = new ResourceLabelModel(categories.getObject().get(item.getIndex()));
		link.add(new Label("label", labelModel));

		item.add(link);
		item.add(container);
	}

	private PropertyListView<Perception> createListView(final ListItem<List<Perception>> item) {
		final PropertyListView<Perception> list = new PropertyListView<Perception>("list",
				item.getModelObject()) {
			@Override
			protected void populateItem(final ListItem<Perception> item) {
				item.add(new TextField<String>("ID"));
				item.add(new TextField<String>("name"));
				item.add(new TextField<String>("color"));
			}
		};
		return list;
	}

	private void addCancelButton(final String string, final Form<?> form) {
		RBCancelButton button = new RBCancelButton("cancel"){
			@Override
			protected void applyActions(final AjaxRequestTarget target, final Form<?> form) {
				CreatePerceptionsWizzardPanel.this.onCancel(target, form);
			}
		};
		button.add(new Label("cancelLabel", new ResourceModel("button.cancel")));

		form.add(button);
	}


	private void addSaveButton(final String id, final Form<?> form) {
		RBStandardButton button = new RBStandardButton(id){
			@Override
			protected void applyActions(final AjaxRequestTarget target, final Form<?> form) {
				CreatePerceptionsWizzardPanel.this.onSubmit(target, form);
			}
		};
		button.add(new Label("saveLabel", new ResourceModel("button.save")));
		form.add(button);
	}

	private void initializeModel() {
		categories = new LoadableDetachableModel<List<ResourceNode>>() {
			@Override
			protected List<ResourceNode> load() {
				return perceptionDefinitionService.findAllPerceptionCategories();
			}
		};
		model = new PerceptionWizzardListModel(categories);
	}

}

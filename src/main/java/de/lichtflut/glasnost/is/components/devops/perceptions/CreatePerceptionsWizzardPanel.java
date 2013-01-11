/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.lichtflut.glasnost.is.events.ModelChangeEvent;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.webck.components.form.RBCancelButton;
import de.lichtflut.rb.webck.components.form.RBStandardButton;

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

	private final Map<String, List<Perception>> perceptionsMap = new LinkedHashMap<String, List<Perception>>();
	// TODO change how current label is retrieved in Listview
	private final ArrayList<String> keys;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Comonent id
	 */
	public CreatePerceptionsWizzardPanel(final String id) {
		super(id);
		fillPerceptions();
		keys = new ArrayList<String>(perceptionsMap.keySet());

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
		for (List<Perception> list : perceptionsMap.values()) {
			for (Perception perception : list) {
				perceptionDefinitionService.store(perception);
			}
		}
		send(this, Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.PERCEPTION));
	}

	// ------------------------------------------------------

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


	private void addPerceptions(final String id, final Form<?> form) {
		ListView<List<Perception>> listView = new ListView<List<Perception>>(id, new ArrayList<List<Perception>>(
				perceptionsMap.values())) {
			@Override
			protected void populateItem(final ListItem<List<Perception>> item) {
				addSecondLvlListView(item);
			}
		};
		form.add(listView);
	}

	private void addSecondLvlListView(final ListItem<List<Perception>> item) {
		final PropertyListView<Perception> list = createListView(item);
		final WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		container.add(list);

		AjaxSubmitLink link = new AjaxSubmitLink("link") {
			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				List<Perception> modelObject = list.getModelObject();
				modelObject.add(createPerceptionFor(item.getIndex()));
				target.add(container);
			}
		};
		link.add(new Label("label", new ResourceModel(keys.get(item.getIndex()))));

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

	private Perception createPerceptionFor(final int index) {
		//TODO load data from properties file
		Perception perception;
		switch (index) {
			case 0:
				perception = new Perception();
				perception.setID("CPT");
				perception.setName("Conceptual");
				perception.setColor("#fff");
				break;
			case 1:
				perception = new Perception();
				perception.setID("DEV");
				perception.setName("Development");
				perception.setColor("#f00");
				break;
			case 2:
				perception = new Perception();
				perception.setID("TST");
				perception.setName("Test");
				perception.setColor("#0f0");
				break;
			case 3:
				perception = new Perception();
				perception.setID("PRE");
				perception.setName("Pre-Production");
				perception.setColor("#00f");
				break;
			case 4:
				perception = new Perception();
				perception.setID("PRO");
				perception.setName("Production");
				perception.setColor("#ff0");
				break;
			default:
				perception = new Perception();
				break;
		}
		return perception;
	};

	private void fillPerceptions() {
		//TODO Initially load from Entities/RDF:Label
		perceptionsMap.put("perception.conceptual", new ArrayList<Perception>());
		perceptionsMap.put("perception.development", new ArrayList<Perception>());
		perceptionsMap.put("perception.test", new ArrayList<Perception>());
		perceptionsMap.put("perception.pre-prod", new ArrayList<Perception>());
		perceptionsMap.put("perception.production", new ArrayList<Perception>());
	}

	// ------------------------------------------------------

	@Override
	protected void onDetach() {
		super.onDetach();
		perceptionsMap.clear();
	}

}

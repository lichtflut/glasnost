/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.webck.common.RBAjaxTarget;

/**
 * <p>
 * This Panel allows you to create multiple perceptions at once.
 * </p>
 * Created: Jan 9, 2013
 *
 * @author Ravi Knox
 */
public class CreatePerceptionsWizzardPanel extends Panel {

	private final Map<String, Perception> perceptionKey = new HashMap<String, Perception>();
	// TODO change how current label is retrieved in Listview
	private final ArrayList<String> keys;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * @param id Comonent id
	 */
	public CreatePerceptionsWizzardPanel(final String id) {
		super(id);
		fillPerceptions();
		keys = new ArrayList<String>(perceptionKey.keySet());
		Form<?> form = new Form<Void>("form");

		addPerceptions("list", form);


		add(form);
		// For each perception type (dev, copt, qa) add perception. onclick perception type set visible true

	}

	private void addPerceptions(final String id, final Form<?> form) {
		ListView<Perception> listView = new PropertyListView<Perception>(id, new ArrayList<Perception>(perceptionKey.values())) {
			@Override
			protected void populateItem(final ListItem<Perception> item) {
				final WebMarkupContainer container = new WebMarkupContainer("container");
				container.add(new TextField<String>("ID"));
				container.add(new TextField<String>("name"));
				container.add(new TextField<String>("color"));
				container.setVisible(false);
				container.setOutputMarkupId(true);

				AjaxLink<?> link = new AjaxLink<Void>("link"){
					@Override
					public void onClick(final AjaxRequestTarget target) {
						container.setVisible(true);
						RBAjaxTarget.add(item);
					}
				};
				link.add(new Label("label", new ResourceModel(keys.get(item.getIndex()))));

				item.add(link);
				item.add(container);
				item.setOutputMarkupId(true);
			}

		};

		form.add(listView);
	}

	private void fillPerceptions() {
		Perception conceptual = new Perception();
		conceptual.setID("CPT");
		conceptual.setName("Conceptual");
		conceptual.setColor("#fff");
		perceptionKey.put("perception.conceptual",conceptual);

		Perception development = new Perception();
		development.setID("DEV");
		development.setName("Development");
		development.setColor("#f00");
		perceptionKey.put("perception.development", development);

		Perception test = new Perception();
		test.setID("TST");
		test.setName("Test");
		test.setColor("#0f0");
		perceptionKey.put("perception.test", test);

		Perception preProd = new Perception();
		preProd.setID("PRE");
		preProd.setName("Pre-Production");
		preProd.setColor("#00f");
		perceptionKey.put("perception.pre-prod", preProd);

		Perception prod = new Perception();
		prod.setID("PRO");
		prod.setName("Production");
		prod.setColor("#ff0");
		perceptionKey.put("perception.production", prod);
	}

}

package de.lichtflut.glasnost.is.components.devops.perceptions;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.ui.PerceptionModel;
import de.lichtflut.rb.webck.common.RBAjaxTarget;

/**
 * <p>
 *  This panel lists all registered perceptions and allows creation and editing.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class PerceptionManagementPanel extends Panel {

	private final IModel<Perception> selected = new Model<Perception>();

	// ----------------------------------------------------

	public PerceptionManagementPanel(final String id) {
		this(id, new PerceptionModel());
	}

	public PerceptionManagementPanel(final String id, final IModel<List<Perception>> model) {
		super(id, model);

		setOutputMarkupId(true);

		final ListView<Perception> perceptionView = createListView(model);
		add(perceptionView);

		add(new PerceptionEditPanel("editor", selected) {
			@Override
			public void onUpdate() {
				selected.setObject(null);
				RBAjaxTarget.add(PerceptionManagementPanel.this);
			}
		});

		add(createNewLink());
	}

	// ----------------------------------------------------

	private ListView<Perception> createListView(final IModel<List<Perception>> model) {
		return new ListView<Perception>("perceptionView", model) {
			@Override
			protected void populateItem(final ListItem<Perception> item) {
				Perception perception = item.getModelObject();

				Label id = new Label("id", perception.getID());
				id.add(new AttributeModifier("title", perception.getContext()));
				item.add(id);

				Label name = new Label("name", perception.getName());
				item.add(name);

				item.add(createEditLink(item.getModel()));
				item.add(createDeleteLink(item.getModel()));
				item.add(createUpLink(item.getModel()));
				item.add(createDownLink(item.getModel()));

			}
		};
	}

	// ----------------------------------------------------

	private AbstractLink createNewLink() {
		return new AjaxLink<Void>("createPerceptionLink") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				selected.setObject(new Perception());
				updateEditor(target);
			}
		};
	}

	private AjaxLink<?> createDeleteLink(final IModel<Perception> model) {
		final AjaxLink<?> link = new AjaxLink<Void>("delete") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
			}
		};
		return link;
	}

	private AjaxLink<?> createEditLink(final IModel<Perception> model) {
		return new AjaxLink<Void>("edit") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				selected.setObject(model.getObject());
				updateEditor(target);
			}
		};
	}

	private AjaxLink<?> createUpLink(final IModel<Perception> model) {
		final AjaxLink<?> link = new AjaxLink<Void>("up") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
			}
		};
		return link;
	}

	private AjaxLink<?> createDownLink(final IModel<Perception> model) {
		final AjaxLink<?> link = new AjaxLink<Void>("down") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
			}
		};
		return link;
	}

	// ----------------------------------------------------

	@Override
	protected void onDetach() {
		super.onDetach();
		selected.detach();
	}

	private void updateEditor(final AjaxRequestTarget target) {
		target.add(get("editor"));
	}

}

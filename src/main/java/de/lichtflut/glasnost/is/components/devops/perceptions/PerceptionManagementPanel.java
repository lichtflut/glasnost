package de.lichtflut.glasnost.is.components.devops.perceptions;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.lichtflut.glasnost.is.dialog.CreatePerceptionsWizzardDialog;
import de.lichtflut.glasnost.is.events.ModelChangeEvent;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.ui.PerceptionModel;
import de.lichtflut.glasnost.is.pages.PerceptionPage;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import de.lichtflut.rb.webck.components.common.DialogHoster;

/**
 * <p>
 * This panel lists all registered perceptions and allows creation and editing.
 * </p>
 * 
 * <p>
 * Created 16.11.12
 * </p>
 * 
 * @author Oliver Tigges
 */
public class PerceptionManagementPanel extends Panel {

	@SpringBean
	private PerceptionDefinitionService perceptionDefinitionService;

	// ----------------------------------------------------

	public PerceptionManagementPanel(final String id) {
		this(id, new PerceptionModel());
	}

	public PerceptionManagementPanel(final String id, final IModel<List<Perception>> model) {
		super(id, model);

		setOutputMarkupId(true);

		final ListView<Perception> perceptionView = createListView(model);
		add(perceptionView);

		add(createNewLink());
		add(createPerceptionWizzardLink("openWizzardLink"));
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

				item.add(createViewLink(item.getModel()));
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
				PageParameters parameters = new PageParameters();
				parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
				setResponsePage(PerceptionPage.class, parameters);
			}
		};
	}

	private AbstractLink createPerceptionWizzardLink(final String id) {
		return new AjaxLink<Void>(id) {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				final DialogHoster dialogHoster = findParent(DialogHoster.class);
				dialogHoster.openDialog(new CreatePerceptionsWizzardDialog(dialogHoster.getDialogID()) {
					@Override
					protected void onUpdate(final AjaxRequestTarget target, final Form<?> form) {
						dialogHoster.closeDialog(this);
					}
				});
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

	private AjaxLink<?> createViewLink(final IModel<Perception> model) {
		return new AjaxLink<Void>("view") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				PageParameters parameters = new PageParameters();
				parameters.add(CommonParams.PARAM_RESOURCE_ID, model.getObject().toURI());
				parameters.add(DisplayMode.PARAMETER, DisplayMode.VIEW);
				setResponsePage(PerceptionPage.class, parameters);
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
	public void onEvent(final IEvent<?> event) {
		ModelChangeEvent<?> mce = ModelChangeEvent.from(event);
		if (mce.isAbout(ModelChangeEvent.PERCEPTION)) {
			RBAjaxTarget.add(PerceptionManagementPanel.this);
		}
	}

}

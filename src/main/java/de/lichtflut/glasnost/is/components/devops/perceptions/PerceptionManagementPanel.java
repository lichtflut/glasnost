package de.lichtflut.glasnost.is.components.devops.perceptions;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.lichtflut.glasnost.is.dialog.CreatePerceptionsWizzardDialog;
import de.lichtflut.glasnost.is.events.ModelChangeEvent;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionOrder;
import de.lichtflut.glasnost.is.model.ui.PerceptionModel;
import de.lichtflut.glasnost.is.pages.PerceptionDetailPage;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import de.lichtflut.rb.webck.components.common.DialogHoster;
import de.lichtflut.rb.webck.components.dialogs.ConfirmationDialog;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

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

				Label owner = new Label("owner", new ResourceLabelModel(perception.getOwner()));
				item.add(owner);

				Label color = new Label("color", perception.getColor());
				item.add(color);

				item.add(createViewLink(item.getModel()));
				item.add(createDeleteLink(item.getModel()));
				item.add(createUpLink(item.getModel(), model));
				item.add(createDownLink(item.getModel(), model));
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
				setResponsePage(PerceptionDetailPage.class, parameters);
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
				openConfirmationDialog(model);
			}

			private void openConfirmationDialog(final IModel<Perception> model) {
				final String confirmation = getString("dialog.confirmation.delete") + " '" + model.getObject().getID()
						+ "'";
				final DialogHoster hoster = findParent(DialogHoster.class);
				hoster.openDialog(new ConfirmationDialog(hoster.getDialogID(), Model.of(confirmation)) {

					@Override
					public void onConfirm() {
						perceptionDefinitionService.delete(model.getObject());
						send(getPage(), Broadcast.BREADTH, new ModelChangeEvent<Void>(ModelChangeEvent.PERCEPTION));
					}

					@Override
					public void onCancel() {
						hoster.closeDialog(this);
					}
				});
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
				setResponsePage(PerceptionDetailPage.class, parameters);
			}
		};
	}

	private AjaxLink<?> createUpLink(final IModel<Perception> model, final IModel<List<Perception>> perceptions) {
		final AjaxLink<?> link = new AjaxLink<Void>("up") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				swapAndStore(model, perceptions, -1);
				update();
			}


		};
		return link;
	}

	private AjaxLink<?> createDownLink(final IModel<Perception> model, final IModel<List<Perception>> perceptions) {
		final AjaxLink<?> link = new AjaxLink<Void>("down") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				swapAndStore(model, perceptions, +1);
				update();
			}
		};
		return link;
	}

	private void swapAndStore(final IModel<Perception> model, final IModel<List<Perception>> perceptions,
			final int positions) {
		List<Perception> list = perceptions.getObject();
		int pos = list.indexOf(model.getObject());
		if(checḱRange(pos, positions, list)){
			Perception actual = list.get(pos);
			Perception wanted = list.get(pos + positions);
			new PerceptionOrder(list).swap(actual, wanted);
			perceptionDefinitionService.store(list);
		}
	}

	private boolean checḱRange(final int pos, final int positions, final List<Perception> list) {
		boolean valid = false;
		if (pos > 0 || positions > pos){
			if(list.size()-1 > pos || positions < 1) {
				valid=true;
			}
		}
		return valid;
	}

	private void update() {
		RBAjaxTarget.add(PerceptionManagementPanel.this);
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

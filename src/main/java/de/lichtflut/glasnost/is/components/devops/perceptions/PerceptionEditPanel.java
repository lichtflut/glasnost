package de.lichtflut.glasnost.is.components.devops.perceptions;

import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.enableIf;
import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.visibleIf;
import static de.lichtflut.rb.webck.models.ConditionalModel.isNotNull;
import static de.lichtflut.rb.webck.models.ConditionalModel.isNull;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.core.RB;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.components.fields.EntityPickerField;
import de.lichtflut.rb.webck.components.form.RBCancelButton;
import de.lichtflut.rb.webck.components.form.RBDefaultButton;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

/**
 * <p>
 *  Panel for editing of a perception.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class PerceptionEditPanel extends TypedPanel<Perception> {

	@SpringBean
	private PerceptionDefinitionService service;

	// ----------------------------------------------------

	/**
	 * Constructor.
	 * @param id The component id.
	 * @param model The model containing the perception.
	 */
	public PerceptionEditPanel(final String id, final IModel<Perception> model) {
		super(id, model);

		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);

		Form<?> form = new Form<Void>("form");
		form.add(new FeedbackPanel("feedback"));

		createFields(model, form);

		form.add(createCancelButton());
		form.add(createSaveButton());

		add(form);

		add(visibleIf(isNotNull(model)));
	}


	// ------------------------------------------------------

	/**
	 * Gets called when form gets submitted.
	 * @param target
	 * @param form
	 */
	protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
		store();
		onUpdate(target, form);
	}

	protected void onUpdate(final AjaxRequestTarget target, final Form<?> form) {
	}


	/**
	 * Add input fields to form.
	 * @param model IModel containing the perception
	 * @param form
	 */
	protected void createFields(final IModel<Perception> model, final Form<?> form) {
		TextField<String> idField = new TextField<String>("id", new PropertyModel<String>(model, "ID"));
		idField.add(enableIf(isNull(new ContextModel(model))));
		form.add(idField);

		TextField<String> nameField = new TextField<String>("name", new PropertyModel<String>(model, "name"));
		form.add(nameField);

		EntityPickerField entityPicker = new EntityPickerField("type", new PropertyModel<ResourceID>(model, "type"), GIS.PERCEPTION_CATEGORY);
		form.add(entityPicker);

		TextField<String> colorField = new TextField<String>("color", new PropertyModel<String>(model, "color"));
		form.add(colorField);

		TextField<String> fileuploadField = new TextField<String>("image", new PropertyModel<String>(model, "imagePath"));
		//		AjaxEditableUploadField fileUpload = new AjaxEditableUploadField("image", new PropertyModel<Object>(model, "imagePath"));
		form.add(fileuploadField);

		EntityPickerField ownerPicker = new EntityPickerField("owner", new PropertyModel<ResourceID>(model, "owner"), RB.PERSON);
		form.add(ownerPicker);

		EntityPickerField personResponsiblePicker = new EntityPickerField("personResponsible", new PropertyModel<ResourceID>(model, "personResponsible"), RB.PERSON);
		form.add(personResponsiblePicker);
	}

	protected Button createSaveButton() {
		return new RBDefaultButton("save") {
			@Override
			protected void applyActions(final AjaxRequestTarget target, final Form<?> form) {
				PerceptionEditPanel.this.onSubmit(target, form);
			}
		};
	}


	protected Button createCancelButton() {
		return new RBCancelButton("cancel") {
			@Override
			protected void applyActions(final AjaxRequestTarget target, final Form<?> form) {
				onUpdate(target, form);
			}
		};
	}

	protected void store() {
		Perception perception = getModelObject();
		service.store(perception);
	}

	// ----------------------------------------------------

	private class ContextModel extends DerivedDetachableModel<Context, Perception> {

		public ContextModel(final IModel<Perception> original) {
			super(original);
		}

		@Override
		protected Context derive(final Perception perception) {
			return perception.getContext();
		}
	}
}

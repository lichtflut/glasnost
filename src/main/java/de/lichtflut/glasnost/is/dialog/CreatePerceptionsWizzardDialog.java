/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.dialog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

import de.lichtflut.glasnost.is.components.devops.perceptions.CreatePerceptionsWizzardPanel;
import de.lichtflut.rb.webck.components.dialogs.AbstractRBDialog;


/**
 * <p>
 * Wizzard to create multiple Perceptions at once
 * </p>
 * Created: Jan 9, 2013
 *
 * @author Ravi Knox
 */
public class CreatePerceptionsWizzardDialog extends AbstractRBDialog {

	/**
	 * Constructor.
	 * @param id Component id
	 */
	public CreatePerceptionsWizzardDialog(final String id) {
		super(id);
		add(new CreatePerceptionsWizzardPanel("wizzard"){
			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				super.onSubmit(target, form);
				CreatePerceptionsWizzardDialog.this.onUpdate(target, form);
			}

			@Override
			protected void onCancel(final AjaxRequestTarget target, final Form<?> form) {
				super.onCancel(target, form);
				CreatePerceptionsWizzardDialog.this.onUpdate(target, form);
			}
		});
	}

	// ------------------------------------------------------

	protected void onUpdate(final AjaxRequestTarget target, final Form<?> form) {
	}

}

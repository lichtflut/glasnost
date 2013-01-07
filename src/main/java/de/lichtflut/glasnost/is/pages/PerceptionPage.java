/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel;
import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.models.ConditionalModel;

/**
 * <p>
 * This page allows you to create or edit {@link Perception}s.
 * </p>
 * Created: Jan 4, 2013
 * 
 * @author Ravi Knox
 */
public class PerceptionPage extends RBBasePage {

	private final IModel<DisplayMode> displayMode;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param model
	 * @param displayMode
	 */
	// FIXME Change/Add parameter to PagePArameters
	public PerceptionPage(final IModel<Perception> model, final IModel<DisplayMode> displayMode) {
		this.displayMode = displayMode;

		Component perceptionDisplay = createPerceptionDisplay("perceptionDisplay", model);
		Component perceptionEditor = createPerceptionEditor("perceptionEditor", model);

		perceptionDisplay.add(ConditionalBehavior.visibleIf(ConditionalModel.areEqual(displayMode, DisplayMode.VIEW)));
		perceptionEditor.add(ConditionalBehavior.visibleIf(ConditionalModel.not(ConditionalModel.areEqual(displayMode, DisplayMode.VIEW))));

		add(perceptionDisplay, perceptionEditor);
	}

	// ------------------------------------------------------

	/**
	 * Create perception display.
	 * 
	 * @param id Component id
	 * @param model IModel of the perception
	 * @return
	 */
	private Component createPerceptionDisplay(final String id, final IModel<Perception> model) {
		return new PerceptionDisplayPanel(id, model){
			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				displayMode.setObject(DisplayMode.EDIT);
				target.add(PerceptionPage.this);
			}
		};
	}

	/**
	 * Create perception editor.
	 * 
	 * @param id Component id
	 * @param model IModel of the perception
	 * @return
	 */
	private Component createPerceptionEditor(final String id, final IModel<Perception> model) {
		return new PerceptionEditPanel(id, model);
	}

}

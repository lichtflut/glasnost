/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.model.IModel;

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel;
import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.webck.common.DisplayMode;

/**
 * <p>
 * This page allows you to create or edit {@link Perception}s.
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionPage extends RBBasePage {

	public PerceptionPage(final IModel<Perception> model, final IModel<DisplayMode> displayMode) {
		if(DisplayMode.VIEW.name().equals(displayMode.getObject().name())){
			addPerceptionDisplay("perception", model);
		}else{
			addPerceptionEditor("perception", model);
		}
	}

	// ------------------------------------------------------

	/**
	 * Add perception display.
	 * @param id Component id
	 * @param model IModel of the perception
	 */
	private void addPerceptionDisplay(final String id, final IModel<Perception> model) {
		add(new PerceptionDisplayPanel(id, model));

	}

	/**
	 * Add perception editor.
	 * @param id Component id
	 * @param model IModel of the perception
	 */
	private void addPerceptionEditor(final String id, final IModel<Perception> model) {
		add(new PerceptionEditPanel(id, model));
	}
}

/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.dialog;

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
		add(new CreatePerceptionsWizzardPanel("wizzard"));
	}

}

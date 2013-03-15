/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionManagementPanel;
import de.lichtflut.rb.application.base.RBBasePage;

/**
 * <p>
 *  The dashboard - welcome and personal page.
 * </p>
 *
 * <p>
 * 	Created May 30, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class WelcomePage extends RBBasePage {

	/**
	 * @param parameters The parameters.
	 */
	public WelcomePage(final PageParameters parameters) {
		super(parameters);

		add(new PerceptionManagementPanel("perceptionManagement").setOutputMarkupId(true));
	}

}

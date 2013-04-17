/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.webck.components.common.FindClassInEntityPanel;

/**
 * <p>
 * [TODO Insert description here.]
 * </p>
 * Created: Mar 18, 2013
 * 
 * @author Ravi Knox
 */
public class FindClassInEntityPage extends RBBasePage {

	/**
	 * Constructor
	 */
	public FindClassInEntityPage() {
		add(new FindClassInEntityPanel("findClass"));
	}
}

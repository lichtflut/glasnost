/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * <p>
 * This behavior adds a colorpicker to a component.
 * </p>
 * Created: Jan 16, 2013
 *
 * @author Ravi Knox
 */
public class ColorPickerBehavior extends Behavior {

	@Override
	public void renderHead(final Component component, final IHeaderResponse response) {
		response.renderJavaScriptReference("jQueryColorPicker/jquery.colorpicker.js");
		response.renderOnLoadJavaScript("$('#" + component.getId() + "'.colorpicker({" +
				"size: 20, label: 'Color: ', hide: false})");

		super.renderHead(component, response);
	}


}

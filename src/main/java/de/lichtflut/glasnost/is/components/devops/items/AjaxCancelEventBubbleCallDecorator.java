/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.items;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;

/**
 * <p>
 * [TODO Insert description here.]
 * </p>
 * Created: Feb 21, 2013
 *
 * @author Ravi Knox
 */
public class AjaxCancelEventBubbleCallDecorator extends AjaxPostprocessingCallDecorator {

	private static final long serialVersionUID = 1L;

	public AjaxCancelEventBubbleCallDecorator()
	{
		this((IAjaxCallDecorator)null);
	}

	public AjaxCancelEventBubbleCallDecorator(final IAjaxCallDecorator delegate)
	{
		super(delegate);
	}

	@Override
	public CharSequence postDecorateScript(final Component component, final CharSequence script) {
		return "e = event; if(e.stopPropagation) {e.stopPropagation();}else{e.cancelBubble = true;}" + script;
	}

}

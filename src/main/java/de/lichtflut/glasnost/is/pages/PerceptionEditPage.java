/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.lichtflut.glasnost.is.pages;

import static de.lichtflut.rb.webck.models.ConditionalModel.areEqual;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.rb.core.perceptions.Perception;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.webck.common.DisplayMode;

/**
 * <p>
 * This page allows you to create or edit {@link Perception}s.
 * </p>
 * Created: Jan 18, 2013
 * 
 * @author Ravi Knox
 */
public class PerceptionEditPage extends RBBasePage {

	@SpringBean
	private SemanticNetworkService networkService;

	private final IModel<DisplayMode> displayMode;

	// ------------------------------------------------------

	/**
	 * Constructor. PageParameters might contain
	 * <ul>
	 * <li>
	 * {@link DisplayMode}</li>
	 * <li>
	 * {@link CommonParams#PARAM_RESOURCE_ID}</li>
	 * <ul>
	 * 
	 * @param parameters PageParamter
	 */
	public PerceptionEditPage(final PageParameters parameters) {
		super(parameters);
		this.displayMode = new Model<DisplayMode>(DisplayMode.fromParams(parameters));
		IModel<Perception> model = getPerceptionFromParam(parameters, displayMode);

		initPage(model);
	}

	// ------------------------------------------------------

	/**
	 * Initialize and add page components.
	 * 
	 * @param model Model containing the perception
	 */
	private void initPage(final IModel<Perception> model) {
		add(createPerceptionEditor("perceptionEditor", model));

	}

	/**
	 * Create perception editor.
	 * 
	 * @param id Component id
	 * @param model IModel of the perception
	 * @return
	 */
	private Component createPerceptionEditor(final String id, final IModel<Perception> model) {
		return new PerceptionEditPanel(id, model) {
			@Override
			protected void onUpdate(final AjaxRequestTarget target, final Form<?> form) {
				if (null == model.getObject().getID()) {
					setResponsePage(WelcomePage.class);
				} else {
					PageParameters parameters = new PageParameters();
					parameters.add(CommonParams.PARAM_RESOURCE_ID, model.getObject().getQualifiedName());
					setResponsePage(PerceptionDisplayPage.class, parameters);
				}
			}

			@Override
			protected void onCancel(final AjaxRequestTarget target, final Form<?> form) {
				setResponsePage(WelcomePage.class);
			}
		};
	}

	private IModel<Perception> getPerceptionFromParam(final PageParameters parameters,
			final IModel<DisplayMode> displayMode) {
		IModel<Perception> model;
		if (areEqual(this.displayMode, DisplayMode.CREATE).isFulfilled()) {
			model = new Model<Perception>(new Perception());
		} else {
			StringValue id = parameters.get(CommonParams.PARAM_RESOURCE_ID);
			ResourceNode node = networkService.find(QualifiedName.fromURI(id.toString()));
			model = new Model<Perception>(new Perception(node));
		}

		return model;
	}
}

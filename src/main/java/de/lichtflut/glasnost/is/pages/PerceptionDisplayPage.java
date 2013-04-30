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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.webck.common.DisplayMode;

/**
 * <p>
 * This page allows you to display details of a {@link Perception}s.
 * </p>
 * Created: Jan 4, 2013
 * 
 * @author Ravi Knox
 */
public class PerceptionDisplayPage extends RBBasePage {

	@SpringBean
	private SemanticNetworkService networkService;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * PageParameters might contain
	 * <ul><li>
	 * {@link CommonParams#PARAM_RESOURCE_ID}
	 * </li><ul>
	 * @param parameters PageParamter
	 */
	public PerceptionDisplayPage(final PageParameters parameters){
		super(parameters);
		IModel<Perception> model = getPerceptionFromParam(parameters);

		addPerceptionDisplay("perceptionDisplay", model);
	}

	/**
	 * Constructor.
	 * @param model Model containing the {@link Perception}
	 */
	public PerceptionDisplayPage(final IModel<Perception> model) {
		addPerceptionDisplay("perceptionDisplay", model);
	}

	// ------------------------------------------------------

	/**
	 * Create perception display.
	 * 
	 * @param id Component id
	 * @param model IModel of the perception
	 */
	private void addPerceptionDisplay(final String id, final IModel<Perception> model) {
		PerceptionDisplayPanel panel = new PerceptionDisplayPanel(id, model) {
			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				PageParameters parameters = new PageParameters();
				parameters.add(CommonParams.PARAM_RESOURCE_ID, model.getObject().getQualifiedName());
				parameters.add(DisplayMode.PARAMETER, DisplayMode.EDIT);
				setResponsePage(PerceptionEditPage.class, parameters);
			}
		};
		add(panel);
	}

	private IModel<Perception> getPerceptionFromParam(final PageParameters parameters) {
		StringValue id = parameters.get(CommonParams.PARAM_RESOURCE_ID);
		DisplayMode displayMode = DisplayMode.fromParams(parameters);
		IModel<Perception> model;
		if(DisplayMode.VIEW.name().equals(displayMode.name())){
			ResourceNode node = networkService.find(new QualifiedName(id.toString()));
			model = new Model<Perception>(new Perception(node));
		}else{
			model = new Model<Perception>(new Perception());
		}
		return model;
	}
}

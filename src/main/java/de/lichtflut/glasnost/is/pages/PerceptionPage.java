/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.visibleIf;
import static de.lichtflut.rb.webck.models.ConditionalModel.areEqual;
import static de.lichtflut.rb.webck.models.ConditionalModel.not;

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

import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionDisplayPanel;
import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.services.SemanticNetworkService;
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

	@SpringBean
	private SemanticNetworkService networkService;

	private final IModel<DisplayMode> displayMode;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * PageParameters might contain
	 * <ul><li>
	 * {@link DisplayMode}
	 * </li><li>
	 * {@link CommonParams#PARAM_RESOURCE_ID}
	 * </li><ul>
	 * @param parameters PageParamter
	 */
	public PerceptionPage(final PageParameters parameters){
		super(parameters);
		this.displayMode = new Model<DisplayMode>(DisplayMode.fromParams(parameters));
		IModel<Perception> model = getPerceptionFromParam(parameters, displayMode);

		initPage(model);
	}

	/**
	 * Constructor.
	 * @param model Model containing the {@link Perception}
	 * @param createMode Model containing the {@link DisplayMode}
	 */
	public PerceptionPage(final IModel<Perception> model, final Model<DisplayMode> displayMode) {
		this.displayMode = displayMode;
		initPage(model);
	}

	// ------------------------------------------------------

	/**
	 * Initialize and add page components.
	 * @param model Model containing the perception
	 */
	private void initPage(final IModel<Perception> model) {
		Component perceptionDisplay = createPerceptionDisplay("perceptionDisplay", model);
		Component perceptionEditor = createPerceptionEditor("perceptionEditor", model);

		perceptionDisplay.add(visibleIf(areEqual(displayMode, DisplayMode.VIEW)));
		perceptionEditor.add(visibleIf(not(areEqual(displayMode, DisplayMode.VIEW))));

		add(perceptionDisplay, perceptionEditor);
	}

	/**
	 * Create perception display.
	 * 
	 * @param id Component id
	 * @param model IModel of the perception
	 * @return
	 */
	private Component createPerceptionDisplay(final String id, final IModel<Perception> model) {
		return new PerceptionDisplayPanel(id, model) {
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

	private IModel<Perception> getPerceptionFromParam(final PageParameters parameters, final IModel<DisplayMode> displayMode) {
		IModel<Perception> model;
		if(areEqual(this.displayMode, DisplayMode.CREATE).isFulfilled()){
			model = new Model<Perception>(new Perception());
		}else{
			StringValue id = parameters.get(CommonParams.PARAM_RESOURCE_ID);
			ResourceNode node = networkService.find(new QualifiedName(id.toString()));
			model = new Model<Perception>(new Perception(node));
		}

		return model;
	}
}

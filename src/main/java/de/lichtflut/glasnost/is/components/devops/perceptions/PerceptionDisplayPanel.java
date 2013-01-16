/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.glasnost.is.components.GlasnostTitle;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.services.EntityManager;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.browsing.ResourceLinkProvider;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.components.entity.VisualizationMode;
import de.lichtflut.rb.webck.components.fields.FilePreviewLink;
import de.lichtflut.rb.webck.models.ConditionalModel;
import de.lichtflut.rb.webck.models.basic.DerivedModel;

/**
 * <p>
 * Display a Perception with its properties.
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionDisplayPanel extends TypedPanel<Perception> {

	@SpringBean
	private SemanticNetworkService networkService;

	@SpringBean
	private EntityManager entityManager;

	@SpringBean
	private ResourceLinkProvider resourceLinkProvider;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * @param id Component id
	 * @param model Model containing a perception
	 */
	public PerceptionDisplayPanel(final String id, final IModel<Perception> model) {
		super(id, model);
		if(null == model || null == model.getObject()){
			throw new IllegalArgumentException("Perception must not be null");
		}

		add(new GlasnostTitle("title", new StringResourceModel("title", new PropertyModel<String>(model.getObject(), "ID"))));

		Form<?> form = new Form<Void>("form");

		addDisplayComponents(model, form);

		add(form);

		setOutputMarkupId(true);
	}

	// ------------------------------------------------------

	/**
	 * Gets triggert when 'edit'-button is clicked.
	 * @param target
	 * @param form
	 */
	protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
	}

	// ------------------------------------------------------

	private void addDisplayComponents(final IModel<Perception> model, final Form<?> form) {
		form.add(new Label("id", new PropertyModel<Perception>(model,"ID")));
		form.add(new Label("name", new PropertyModel<Perception>(model,"name")));
		form.add(createLinkForEntity("type", model, "type"));
		form.add(new Label("color", new PropertyModel<Perception>(model,"color")));
		form.add(new FilePreviewLink("image", getImagePath(model)));
		form.add(createLinkForEntity("owner", model,"owner"));
		form.add(createLinkForEntity("personResponsible", model, "personResponsible"));
		form.add(createEditButton("edit", model));
	}

	private IModel<String> getImagePath(final IModel<Perception> model) {
		return new DerivedModel<String, IModel<Perception>>(model){

			@Override
			protected String derive(final IModel<Perception> original) {
				return model.getObject().getImagePath();
			}
		};
	}

	private Component createLinkForEntity(final String id, final IModel<?> model, final String propertyKey) {
		IModel<ResourceID> propertyModel = new PropertyModel<ResourceID>(model, propertyKey);
		ExternalLink link = new ExternalLink(id, getLinkModelForEntity(propertyModel));
		link.add(new Label("label", getLabelModelForEntity(propertyModel)));
		link.add(ConditionalBehavior.visibleIf(ConditionalModel.isNotNull(propertyModel)));
		return link;
	}

	private IModel<String> getLinkModelForEntity(final IModel<ResourceID> model) {
		return new DerivedModel<String, IModel<ResourceID>>(model) {
			@Override
			protected String derive(final IModel<ResourceID> original) {
				return getUrlTo(model.getObject());
			}
		};
	}

	private String getUrlTo(final ResourceID ref) {
		if(null == ref){
			return "";
		}
		return resourceLinkProvider.getUrlToResource(ref, VisualizationMode.DETAILS, DisplayMode.VIEW);
	}

	private Component createEditButton(final String id, final IModel<Perception> model) {
		AjaxButton edit = new AjaxButton(id) {
			@Override
			protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
				PerceptionDisplayPanel.this.onSubmit(target,form);
			}
		};
		return edit;
	}

	private IModel<String> getLabelModelForEntity(final IModel<ResourceID> id) {
		return new DerivedModel<String, IModel<ResourceID>>(id) {
			@Override
			protected String derive(final IModel<ResourceID> original) {
				if(null == original){
					return "";
				}
				RBEntity entity = entityManager.find(id.getObject());
				if(entity == null){
					return id.getObject().toURI();
				}
				return entity.getLabel();
			}
		};
	}

}

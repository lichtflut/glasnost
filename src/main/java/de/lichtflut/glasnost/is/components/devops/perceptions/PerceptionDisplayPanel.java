/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.devops.perceptions;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.services.EntityManager;
import de.lichtflut.rb.core.services.SemanticNetworkService;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.components.fields.FilePreviewLink;

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

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * @param id Component id
	 * @param model Model containing a perception
	 */
	public PerceptionDisplayPanel(final String id, final IModel<Perception> model) {
		super(id, model);
		createDisplayComponents(model);
	}

	// ------------------------------------------------------

	private void createDisplayComponents(final IModel<Perception> model) {
		add(new Label("id", new PropertyModel<Perception>(model,"id")));
		add(new Label("name", new PropertyModel<Perception>(model,"name")));
		add(new Label("type", getLabelForType(model)));
		add(new Label("color", new PropertyModel<Perception>(model,"color")));
		add(new FilePreviewLink("Image", new Model<String>(model.getObject().getImagePath())));
		add(new Label("owner", getLabelForEntity(model.getObject().getOwner())));
		add(new Label("personResponsible", getLabelForEntity(model.getObject().getPersonResponsible())));

	}

	private String getLabelForEntity(final ResourceID id) {
		RBEntity entity = entityManager.find(id);
		if(entity == null){
			return id.toURI();
		}
		return entity.getLabel();
	}

	private String getLabelForType(final IModel<Perception> model) {
		// TODO throw exeption if node could not be found? (Data inconsistentency)
		ResourceID type = model.getObject().getType();
		if(null == type){
			return null;
		}
		ResourceNode node = networkService.find(type.getQualifiedName());
		return getLabelFromNode(node);
	}

	private String getLabelFromNode(final ResourceNode node) {
		// TODO internationalization of labels
		SemanticNode label = SNOPS.singleObject(node, RDFS.LABEL);
		if(label == null){
			return node.toURI();
		}
		return label.asValue().getStringValue();
	}

}

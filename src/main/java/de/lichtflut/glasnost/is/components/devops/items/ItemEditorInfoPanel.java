package de.lichtflut.glasnost.is.components.devops.items;

import java.util.Set;

import de.lichtflut.rb.core.entity.RBEntity;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;

import de.lichtflut.rb.core.common.ResourceLabelBuilder;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.components.entity.EntityInfoPanel;
import de.lichtflut.rb.webck.models.ConditionalModel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

/**
 * <p>
 *  Panel to be used in EntityEditor view.
 * </p>
 *
 * <p>
 *  Created 07.12.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class ItemEditorInfoPanel extends EntityInfoPanel {

	public ItemEditorInfoPanel(final String id, final IModel<RBEntity> model) {
		super(id, model);
		add(createInheritorInfo("inheritors", model));
	}

	// ----------------------------------------------------

	protected Component createInheritorInfo(final String id, final IModel<RBEntity> model) {
		DerivedDetachableModel<String, RBEntity> labelModel = new DerivedDetachableModel<String, RBEntity>(model) {
			@Override
			protected String derive(final RBEntity original) {
				StringBuilder sb = new StringBuilder();
				Set<ResourceNode> inheritors = SNOPS.objectsAsResources(original.getNode(), Aras.INHERITS_FROM);
				boolean first = true;
				for (ResourceNode inheritor : inheritors) {
					if (first) {
						first = false;
					} else {
						sb.append(", ");
					}
					sb.append(ResourceLabelBuilder.getInstance().getLabel(inheritor, getLocale()));
				}
				return sb.toString();
			}
		};
		return new Label(id, labelModel).add(ConditionalBehavior.visibleIf(ConditionalModel.isNotBlank(labelModel)));
	}

}

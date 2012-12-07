package de.lichtflut.glasnost.is.components.devops.items;

import de.lichtflut.rb.core.common.ResourceLabelBuilder;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.webck.components.entity.EntityInfoPanel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.nodes.ResourceNode;

import java.util.Set;

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

    public ItemEditorInfoPanel(String id, IModel<ResourceNode> model) {
        super(id, model, true);

        add(createInheritorInfo("inheritors", model));

    }

    // ----------------------------------------------------

    protected Component createInheritorInfo(String id, IModel<ResourceNode> model) {
        DerivedDetachableModel<String, ResourceNode> labelModel = new DerivedDetachableModel<String, ResourceNode>(model) {
            @Override
            protected String derive(ResourceNode original) {
                StringBuilder sb = new StringBuilder();
                Set<ResourceNode> inheritors = SNOPS.objectsAsResources(original, Aras.INHERITS_FROM);
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
        return new Label(id, labelModel);
    }

}

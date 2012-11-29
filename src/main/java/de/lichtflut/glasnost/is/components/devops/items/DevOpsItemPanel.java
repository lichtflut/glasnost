package de.lichtflut.glasnost.is.components.devops.items;

import de.lichtflut.glasnost.is.model.logic.DevOpsItem;
import de.lichtflut.rb.application.RBApplication;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.models.basic.AbstractLoadableDetachableModel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.arastreju.sge.naming.QualifiedName;

import java.util.List;

/**
 * <p>
 *  Panel displaying a DevOpsItem with it's main data:
 *  <ul>
 *      <li>ID</li>
 *      <li>Name</li>
 *      <li>Canonical name</li>
 *      <li>Qualified name</li>
 *      <li>Quick Info</li>
 *      <li>Image</li>
 *  </ul>
 *  <p>
 *      Additionally it's possible to expand this item and show a list of all sub items.
 *  </p>
 * </p>
 *
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class DevOpsItemPanel extends TypedPanel<DevOpsItem> {

    private boolean expanded = true;

    // ----------------------------------------------------

    /**
     * @param id The component ID.
     * @param model The model.
     */
    public DevOpsItemPanel(final String id, final IModel<DevOpsItem> model) {
        super(id, model);

        add(new Label("itemID", new PropertyModel(model, "ID")));
        add(new Label("name", new PropertyModel(model, "name")));

        ListView<DevOpsItem> subItemsView = new ListView<DevOpsItem>("children", getSubItems(model)) {
            @Override
            protected void populateItem(ListItem<DevOpsItem> item) {
                item.add(new DevOpsItemPanel("item", item.getModel()));
            }
        };
        add(subItemsView);

        add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                QualifiedName qn = model.getObject().getQualifiedName();
                PageParameters params = new PageParameters();
                params.add(CommonParams.PARAM_RESOURCE_ID, qn);
                setResponsePage(RBApplication.get().getEntityDetailPage(), params);
            }
        });
    }

    // ----------------------------------------------------

    private IModel<List<DevOpsItem>> getSubItems(IModel<DevOpsItem> parent) {
        return new DerivedDetachableModel<List<DevOpsItem>, DevOpsItem>(parent) {
            @Override
            protected List<DevOpsItem> derive(DevOpsItem parent) {
                return parent.getSubItems();
            }
        };
    }
}

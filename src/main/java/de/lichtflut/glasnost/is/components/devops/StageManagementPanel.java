package de.lichtflut.glasnost.is.components.devops;

import de.lichtflut.glasnost.is.model.logic.Stage;
import de.lichtflut.glasnost.is.model.ui.StagesModel;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

/**
 * <p>
 *  This panel lists all registered stages and allows creation and editing.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class StageManagementPanel extends Panel {

    private final IModel<Stage> selected = new Model<Stage>();

    // ----------------------------------------------------

    public StageManagementPanel(String id) {
        this(id, new StagesModel());
    }

    public StageManagementPanel(String id, IModel<List<Stage>> model) {
        super(id, model);

        setOutputMarkupId(true);

        final ListView<Stage> stageView = createListView(model);
        add(stageView);

        add(new StageEditPanel("editor", selected) {
            @Override
            public void onUpdate() {
                selected.setObject(null);
                RBAjaxTarget.add(StageManagementPanel.this);
            }
        });

        add(createNewLink());
    }

    // ----------------------------------------------------

    private ListView<Stage> createListView(final IModel<List<Stage>> model) {
        return new ListView<Stage>("stageView", model) {
            @Override
            protected void populateItem(ListItem<Stage> item) {
                Stage stage = item.getModelObject();

                Label id = new Label("id", stage.getID());
                id.add(new AttributeModifier("title", stage.getContext()));
                item.add(id);

                Label name = new Label("name", stage.getName());
                item.add(name);

                item.add(createEditLink(item.getModel()));
                item.add(createDeleteLink(item.getModel()));
                item.add(createUpLink(item.getModel()));
                item.add(createDownLink(item.getModel()));

            }
        };
    }

    // ----------------------------------------------------

    private AbstractLink createNewLink() {
        return new AjaxLink("createStageLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selected.setObject(new Stage());
                updateEditor(target);
            }
        };
    }

    private AjaxLink createDeleteLink(IModel<Stage> model) {
        final AjaxLink link = new AjaxLink("delete") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
            }
        };
        return link;
    }

    private AjaxLink createEditLink(final IModel<Stage> model) {
        return new AjaxLink("edit") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                selected.setObject(model.getObject());
                updateEditor(target);
            }
        };
    }

    private AjaxLink createUpLink(IModel<Stage> model) {
        final AjaxLink link = new AjaxLink("up") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
            }
        };
        return link;
    }

    private AjaxLink createDownLink(IModel<Stage> model) {
        final AjaxLink link = new AjaxLink("down") {
            @Override
            public void onClick(final AjaxRequestTarget target) {
            }
        };
        return link;
    }

    // ----------------------------------------------------

    @Override
    protected void onDetach() {
        super.onDetach();
        selected.detach();
    }

    private void updateEditor(AjaxRequestTarget target) {
        target.add(get("editor"));
    }

}

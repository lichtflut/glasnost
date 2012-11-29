package de.lichtflut.glasnost.is.pages;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.devops.items.DevOpsItemPanel;
import de.lichtflut.glasnost.is.model.logic.DevOpsItem;
import de.lichtflut.glasnost.is.model.logic.Stage;
import de.lichtflut.glasnost.is.services.DevOpsService;
import de.lichtflut.glasnost.is.services.StageDefinitionService;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.apriori.RDF;

import java.util.List;

/**
 * <p>
 *  Start page for DevOps.
 * </p>
 *
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class DevOpsPage extends RBBasePage {

    @SpringBean
    private StageDefinitionService stageService;

    @SpringBean
    private DevOpsService devOpsService;

    // ----------------------------------------------------

    public DevOpsPage(PageParameters parameters) {
        super(parameters);


        ListView<Stage> listView = new ListView<Stage>("stages", stageService.findAllStages()) {
            @Override
            protected void populateItem(ListItem<Stage> item) {
                item.add(createStage(item.getModel()));
            }
        };

        add(listView);

    }

    // ----------------------------------------------------

    private Component createStage(final IModel<Stage> model) {
        WebMarkupContainer container = new WebMarkupContainer("stage");
        container.add(new Label("name", new PropertyModel(model, "name")));
        ListView<DevOpsItem> listView = new ListView<DevOpsItem>("items", rootItemsModel(model)) {
            @Override
            protected void populateItem(ListItem<DevOpsItem> item) {
                item.add(new DevOpsItemPanel("item", item.getModel()));
            }
        };
        container.add(listView);
        container.add(new Link("addRootItem") {
            @Override
            public void onClick() {
                DevOpsItem item = new DevOpsItem();
                item.addAssociation(RDF.TYPE, GIS.DATA_CENTER);
                item.setID("DCX");
                item.setName("DataCenter X");
                devOpsService.addBaseItemToStage(item, model.getObject().getQualifiedName());
            }
        });
        return container;
    }

    // ----------------------------------------------------

    private IModel<List<DevOpsItem>> rootItemsModel(IModel<Stage> stage) {
        return new DerivedDetachableModel<List<DevOpsItem>, Stage>(stage) {
            @Override
            protected List<DevOpsItem> derive(Stage stage) {
                return stage.getTreeRootItems();
            }
        };
    }
}

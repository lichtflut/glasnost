package de.lichtflut.glasnost.is.pages;

import java.util.List;

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

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.devops.items.DevOpsItemPanel;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.glasnost.is.services.PerceptionService;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.webck.models.basic.AbstractLoadableDetachableModel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

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
	private PerceptionDefinitionService perceptionDefinitionService;

	@SpringBean
	private PerceptionService perceptionService;

	// ----------------------------------------------------

	public DevOpsPage(final PageParameters parameters) {
		super(parameters);

		ListView<Perception> listView = new ListView<Perception>("stages", getStageListModel()) {
			@Override
			protected void populateItem(final ListItem<Perception> item) {
				item.add(createStage(item.getModel()));
			}
		};

		add(listView);

	}

	// ----------------------------------------------------

	private Component createStage(final IModel<Perception> model) {
		WebMarkupContainer container = new WebMarkupContainer("stage");
		container.add(new Label("name", new PropertyModel<IModel<Perception>>(model, "name")));
		ListView<PerceptionItem> listView = new ListView<PerceptionItem>("items", rootItemsModel(model)) {
			@Override
			protected void populateItem(final ListItem<PerceptionItem> item) {
				item.add(new DevOpsItemPanel("item", item.getModel()));
			}
		};
		container.add(listView);
		container.add(new Link<Void>("addRootItem") {
			@Override
			public void onClick() {
				PerceptionItem item = new PerceptionItem();
				item.addAssociation(RDF.TYPE, GIS.DATA_CENTER);
				item.setID("DCX");
				item.setName("DataCenter X");
				perceptionService.addBaseItemToPerception(item, model.getObject().getQualifiedName());
			}
		});
		return container;
	}

	// ----------------------------------------------------

	private IModel<List<PerceptionItem>> rootItemsModel(final IModel<Perception> stage) {
		return new DerivedDetachableModel<List<PerceptionItem>, Perception>(stage) {
			@Override
			protected List<PerceptionItem> derive(final Perception perception) {
				return perception.getTreeRootItems();
			}
		};
	}

	private IModel<List<Perception>> getStageListModel() {
		return new AbstractLoadableDetachableModel<List<Perception>>() {
			@Override
			public List<Perception> load() {
				return DevOpsPage.this.perceptionDefinitionService.findAllPerceptions();
			}
		};
	}

}

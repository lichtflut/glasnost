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

import java.util.Collections;
import java.util.List;

import de.lichtflut.rb.core.RBSystem;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.apriori.RDFS;
import org.arastreju.sge.model.nodes.views.SNText;
import org.arastreju.sge.structure.OrderBySerialNumber;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.components.devops.items.DevOpsItemPanel;
import de.lichtflut.rb.core.perceptions.Perception;
import de.lichtflut.rb.core.perceptions.PerceptionItem;
import de.lichtflut.rb.core.services.PerceptionDefinitionService;
import de.lichtflut.rb.core.services.PerceptionService;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.webck.components.common.PanelTitle;
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
		container.add(new PanelTitle("name", new PropertyModel<String>(model, "name")));
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
                item.addAssociation(RBSystem.HAS_SCHEMA_IDENTIFYING_TYPE, GIS.DATA_CENTER);
                item.addAssociation(RDFS.LABEL, new SNText("-- data center unnamed --"));
				item.setID("DCX");
				item.setName("-- data center unnamed --");
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
				List<Perception> perceptions = perceptionDefinitionService.findAllPerceptions();
				Collections.sort(perceptions, new OrderBySerialNumber());
				return perceptions;
			}
		};
	}

}

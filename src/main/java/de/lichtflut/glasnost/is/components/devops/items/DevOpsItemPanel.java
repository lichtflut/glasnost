package de.lichtflut.glasnost.is.components.devops.items;

import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.rb.application.RBApplication;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

/**
 * <p>
 *  Panel displaying a PerceptionItem with it's main data:
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
public class DevOpsItemPanel extends TypedPanel<PerceptionItem> {

	@SuppressWarnings("unused")
	private final boolean expanded = true;

	// ----------------------------------------------------

	/**
	 * @param id The component ID.
	 * @param model The model.
	 */
	public DevOpsItemPanel(final String id, final IModel<PerceptionItem> model) {
		super(id, model);

		add(new Label("itemID", new PropertyModel<IModel<PerceptionItem>>(model, "ID")));
		add(new Label("name", new PropertyModel<IModel<PerceptionItem>>(model, "name")));

		ListView<PerceptionItem> subItemsView = new ListView<PerceptionItem>("children", getSubItems(model)) {
			@Override
			protected void populateItem(final ListItem<PerceptionItem> item) {
				item.add(new DevOpsItemPanel("item", item.getModel()));
			}
		};
		add(subItemsView);

		add(new AjaxEventBehavior("onclick") {
			@Override
			protected void onEvent(final AjaxRequestTarget target) {
				QualifiedName qn = model.getObject().getQualifiedName();
				PageParameters params = new PageParameters();
				params.add(CommonParams.PARAM_RESOURCE_ID, qn);
				setResponsePage(RBApplication.get().getEntityDetailPage(), params);
			}
		});
	}

	// ----------------------------------------------------

	private IModel<List<PerceptionItem>> getSubItems(final IModel<PerceptionItem> parent) {
		return new DerivedDetachableModel<List<PerceptionItem>, PerceptionItem>(parent) {
			@Override
			protected List<PerceptionItem> derive(final PerceptionItem parent) {
				return parent.getSubItems();
			}
		};
	}
}

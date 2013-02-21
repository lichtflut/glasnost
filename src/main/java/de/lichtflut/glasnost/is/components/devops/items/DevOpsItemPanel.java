package de.lichtflut.glasnost.is.components.devops.items;

import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.glasnost.is.pages.DevOpsItemPage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.webck.behaviors.ConditionalBehavior;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.models.ConditionalModel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

/**
 * <p>
 * Panel displaying a PerceptionItem with it's main data:
 * <ul>
 * <li>ID</li>
 * <li>Name</li>
 * <li>Canonical name</li>
 * <li>Qualified name</li>
 * <li>Quick Info</li>
 * <li>Image</li>
 * </ul>
 * <p>
 * Additionally it's possible to expand this item and show a list of all sub items.
 * </p>
 * </p>
 * 
 * <p>
 * Created 29.11.12
 * </p>
 * 
 * @author Oliver Tigges
 */
public class DevOpsItemPanel extends TypedPanel<PerceptionItem> {

	IModel<Boolean> expanded;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id The component ID.
	 * @param model The model.
	 */
	public DevOpsItemPanel(final String id, final IModel<PerceptionItem> model) {
		super(id, model);
		expanded = new Model<Boolean>(false);

		addLabels(model);
		add(new AjaxFallbackLink<String>("details") {
			@Override
			public void onClick(final AjaxRequestTarget target) {
				PageParameters parameters = new PageParameters();
				parameters.add(CommonParams.PARAM_RESOURCE_ID, model.getObject().getPerception().getQualifiedName());
				parameters.add(DisplayMode.PARAMETER, DisplayMode.VIEW);
				setResponsePage(DevOpsItemPage.class, parameters);
			}
		});
		addListView("subItems", model);

		// Move to "more... " link
		add(new AjaxEventBehavior("onclick") {
			@Override
			protected void onEvent(final AjaxRequestTarget target) {
				if(Boolean.TRUE == expanded.getObject()){
					expanded.setObject(false);
				}else{
					expanded.setObject(true);
				}
				RBAjaxTarget.add(DevOpsItemPanel.this);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				// TODO Move decorator class, refactor, rename?
				return new AjaxCancelEventBubbleCallDecorator();
			}
		});
	}

	// ----------------------------------------------------

	private void addLabels(final IModel<PerceptionItem> model) {
		add(new Label("id", new PropertyModel<String>(model, "ID")));
		add(new Label("name", new PropertyModel<String>(model, "name")));
	}

	private void addListView(final String id, final IModel<PerceptionItem> model) {
		ListView<PerceptionItem> view = new ListView<PerceptionItem>(id, getSubItems(model)) {
			@Override
			protected void populateItem(final ListItem<PerceptionItem> item) {
				item.add(new DevOpsItemPanel("item", item.getModel()));
			}
		};
		view.add(ConditionalBehavior.visibleIf(ConditionalModel.isTrue(expanded)));
		add(view);
	}

	private IModel<List<PerceptionItem>> getSubItems(final IModel<PerceptionItem> parent) {
		return new DerivedDetachableModel<List<PerceptionItem>, PerceptionItem>(parent) {
			@Override
			protected List<PerceptionItem> derive(final PerceptionItem parent) {
				return parent.getSubItems();
			}
		};
	}
}

package de.lichtflut.glasnost.is.components.devops.items;

import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.visibleIf;
import static de.lichtflut.rb.webck.models.ConditionalModel.isTrue;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.glasnost.is.pages.DevOpsItemPage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.entity.RBField;
import de.lichtflut.rb.core.services.EntityManager;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBAjaxTarget;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.components.entity.quickinfo.QuickInfoPanel;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;

/**
 * <p>
 * Panel displaying a PerceptionItem with it's quick-info, if available.
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

	@SpringBean
	private EntityManager entityManager;

	private final IModel<Boolean> expanded;

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

		addTitleComponents(model);
		addInfo("container", model);

		setOutputMarkupId(true);
	}

	// ------------------------------------------------------

	private void addTitleComponents(final IModel<PerceptionItem> model) {
		Link<?> detailsLink = new Link<Void>("detailsLink") {
			@Override
			public void onClick() {
				PageParameters parameters = new PageParameters();
				parameters.add(CommonParams.PARAM_RESOURCE_ID, model.getObject().getQualifiedName());
				parameters.add(DisplayMode.PARAMETER, DisplayMode.VIEW);
				setResponsePage(DevOpsItemPage.class, parameters);
			}
		};
		detailsLink.add(new Label("linkLabel", new PropertyModel<String>(model, "name")));
		add(detailsLink);
		addMoreLink("more", model);
	}

	/**
	 * Adds an Ajaxlink to fold/unfold further info
	 */
	private void addMoreLink(final String id, final IModel<PerceptionItem> model) {
		final IModel<String> labelModel = new Model<String>(" ");
		AjaxLink<String> moreLink = new AjaxLink<String>(id){
			@Override
			public void onClick(final AjaxRequestTarget target) {
				if(Boolean.TRUE == expanded.getObject()){
					expanded.setObject(false);
				}else{
					expanded.setObject(true);
				}
				RBAjaxTarget.add(DevOpsItemPanel.this);
			}

			@Override
			protected void onComponentTag(final ComponentTag tag) {
				super.onComponentTag(tag);
				if(Boolean.TRUE == expanded.getObject()){
					tag.put("class", "devops-fold-action fold");
				}else{
					tag.put("class", "devops-fold-action unfold");
				}
			}
		};
		moreLink.add(new Label("linkLabel", labelModel));
		add(moreLink);
	}

	private void addInfo(final String id, final IModel<PerceptionItem> model) {
		WebMarkupContainer container = new WebMarkupContainer("container");
		addQuickInfo("quickInfo", model, container);
		container.add(visibleIf(isTrue(expanded)));

		addListView("subItems", model, container);
		add(container);
	}

	private void addQuickInfo(final String string, final IModel<PerceptionItem> model, final WebMarkupContainer container) {
		IModel<RBEntity> rbEntity = new LoadableDetachableModel<RBEntity>() {
			@Override
			protected RBEntity load() {
				return entityManager.find(model.getObject());
			}
		};

		Panel infoPanel = new QuickInfoPanel("infoPanel", new ListModel<RBField>(rbEntity.getObject().getQuickInfo()));
		container.add(infoPanel);
	}

	private void addListView(final String id, final IModel<PerceptionItem> model, final WebMarkupContainer container) {
		ListView<PerceptionItem> view = new ListView<PerceptionItem>(id, getSubItems(model)) {
			@Override
			protected void populateItem(final ListItem<PerceptionItem> item) {
				item.add(new DevOpsItemPanel("item", item.getModel()));
			}
		};
		view.add(visibleIf(isTrue(expanded)));
		container.add(view);
	}

	// ------------------------------------------------------

	private IModel<List<PerceptionItem>> getSubItems(final IModel<PerceptionItem> parent) {
		return new DerivedDetachableModel<List<PerceptionItem>, PerceptionItem>(parent) {
			@Override
			protected List<PerceptionItem> derive(final PerceptionItem parent) {
				return parent.getSubItems();
			}
		};
	}

}

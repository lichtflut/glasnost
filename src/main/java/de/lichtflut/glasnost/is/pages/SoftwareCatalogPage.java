/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.catalog.CatalogPanel;

/**
 * <p>
 * This page displays the Softwarecatalog for browsing
 * </p>
 * Created: Jan 31, 2013
 *
 * @author Ravi Knox
 */
public class SoftwareCatalogPage extends RBBasePage {

	/**
	 * Constructor
	 */
	public SoftwareCatalogPage() {
		add(new CatalogPanel("catalog", Model.of(GIS.SOFTWARE_ITEM)){
			@Override
			protected void applyAction(final IModel<ResourceID> model) {
				setBrowingStep(model);
				PageParameters parameters = new PageParameters();
				parameters.add(CommonParams.PARAM_RESOURCE_TYPE, model.getObject());
				parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
				setResponsePage(DevOpsItemPage.class, parameters);
			}

			private void setBrowingStep(final IModel<ResourceID> typeConstraint) {
				EntityHandle handle = EntityHandle.forType(typeConstraint.getObject());
				RBWebSession.get().getHistory().create(handle);
			}
		});
	}


}

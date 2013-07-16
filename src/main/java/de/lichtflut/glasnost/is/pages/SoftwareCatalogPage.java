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

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.application.base.RBBasePage;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.services.SemanticNetworkService;
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

	@SpringBean
	private SemanticNetworkService networkService;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor
	 */
	public SoftwareCatalogPage() {
		add(new CatalogPanel("catalog", Model.of(GIS.SOFTWARE_ITEM)){
			@Override
			protected void applyAction(final IModel<ResourceID> model) {
				setBrowingStep(model.getObject());
				PageParameters parameters = new PageParameters();
				parameters.add(CommonParams.PARAM_RESOURCE_TYPE, model.getObject());
				parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
				setResponsePage(GlasnostEntityDetailPage.class, parameters);
			}

			private void setBrowingStep(final ResourceID id) {
				EntityHandle handle = EntityHandle.forType(id);
				RBWebSession.get().getHistory().create(handle);
			}
		});
	}


}

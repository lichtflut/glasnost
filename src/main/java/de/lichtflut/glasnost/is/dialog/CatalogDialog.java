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
package de.lichtflut.glasnost.is.dialog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.glasnost.is.pages.DevOpsPage;
import de.lichtflut.rb.webck.browsing.JumpTarget;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.catalog.CatalogPanel;
import de.lichtflut.rb.webck.components.dialogs.AbstractRBDialog;
import de.lichtflut.rb.webck.models.resources.ResourceLabelModel;

/**
 * <p>
 * A Dialog that displays the {@link CatalogPanel}.
 * </p>
 * Created: Feb 13, 2013
 *
 * @author Ravi Knox
 */
public class CatalogDialog extends AbstractRBDialog {

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 */
	public CatalogDialog(final String id, final IModel<ResourceID> superclass) {
		super(id);
		add(new CatalogPanel("catalog", superclass){
			@Override
			protected void onCancel(final AjaxRequestTarget target, final Form<?> form) {
				closeDialog();
				RBWebSession.get().getHistory().clear(getJumpTarget());
			}

			@Override
			protected void applyAction(final IModel<ResourceID> model) {
				CatalogDialog.this.applyActions(model);
			}
		});
		setTitle(new StringResourceModel("dialog.title", new Model<String>(), new ResourceLabelModel(superclass)));
		setModal(true).setWidth(850).setHeight(600);
	}

	// ------------------------------------------------------

	protected void applyActions(final IModel<ResourceID> model) {

	}

	protected JumpTarget getJumpTarget() {
		return new JumpTarget(DevOpsPage.class);
	}

}

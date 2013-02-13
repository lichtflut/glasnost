/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.dialog;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.glasnost.is.GlasnostWebApplication;
import de.lichtflut.glasnost.is.components.softwareCatalog.CatalogPanel;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.webck.browsing.JumpTarget;
import de.lichtflut.rb.webck.common.RBWebSession;
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
			protected void applyActions(final IModel<RBEntity> model) {
				CatalogDialog.this.applyActions(model);
			}

			@Override
			protected void onCancel() {
				CatalogDialog.this.closeDialog();
				RBWebSession.get().getHistory().clear(getJumpTarget());
			}
		});
		setTitle(new StringResourceModel("dialog.title", new Model<String>(), new ResourceLabelModel(superclass)));
		setModal(true);
		setWidth(850);
		setHeight(600);
	}

	// ------------------------------------------------------

	protected JumpTarget getJumpTarget() {
		return new JumpTarget(GlasnostWebApplication.get().getBrowseAndSearchPage());
	}

	/**
	 * Execute further operations on the newly created entity(e.g. manually create references).
	 */
	protected void applyActions(final IModel<RBEntity> model) {
	}


}

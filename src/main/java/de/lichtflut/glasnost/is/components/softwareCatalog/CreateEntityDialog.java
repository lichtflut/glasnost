/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.components.softwareCatalog;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.model.ResourceID;

import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.services.EntityManager;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.ResourceBrowsingPanel;
import de.lichtflut.rb.webck.components.dialogs.AbstractRBDialog;

/**
 * <p>
 * Dialog for creating an entity on the fly.
 * </p>
 * Created: Jan 31, 2013
 * 
 * @author Ravi Knox
 */
public class CreateEntityDialog extends AbstractRBDialog {

	@SpringBean
	private EntityManager entityManager;

	// ---------------- Constructor -------------------------

	/**
	 * Constructor.
	 * 
	 * @param id Component id
	 */
	public CreateEntityDialog(final String id, final IModel<ResourceID> type) {
		super(id);
		setBrowsingStep(EntityHandle.forType(type.getObject()));
		add(new ResourceBrowsingPanel("editor"){
			@Override
			protected void onSave(final IModel<RBEntity> model) {
				CreateEntityDialog.this.onSave(model);
			}

			@Override
			protected void onCancel() {
				CreateEntityDialog.this.onCancel();
			}

		});
		String simpleName = type.getObject().getQualifiedName().getSimpleName();
		setTitle(new StringResourceModel("header.create-entity", Model.of(simpleName)));
	}

	// ------------------------------------------------------

	protected void onCancel() {
	}


	protected void onSave(final IModel<RBEntity> model) {
	}

	// ------------------------------------------------------

	private void setBrowsingStep(final EntityHandle entityHandle) {
		RBWebSession.get().getHistory().create(entityHandle);
	}

}

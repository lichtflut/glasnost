package de.lichtflut.glasnost.is.pages;

import de.lichtflut.glasnost.is.components.devops.items.ItemEditorInfoPanel;
import de.lichtflut.rb.application.resourceview.EntityDetailPage;
import de.lichtflut.rb.core.entity.EntityHandle;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.webck.components.ResourceBrowsingPanel;
import de.lichtflut.rb.webck.components.entity.LocalButtonBar;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;
import de.lichtflut.rb.webck.models.entity.RBEntityModel;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.nodes.ResourceNode;

/**
 * <p>
 *  Page based on EntityDetailPage displaying one DevOps item.
 * </p>
 *
 * <p>
 *  Created 04.12.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class DevOpsItemPage extends EntityDetailPage {

    public DevOpsItemPage(PageParameters params) {
        super(params);
    }

    public DevOpsItemPage() {
    }

    // ----------------------------------------------------

    @Override
    protected Component createBrowser(String componentID) {
        return new ResourceBrowsingPanel(componentID) {

            @Override
            public void createReferencedEntity(EntityHandle handle, ResourceID predicate) {
                super.createReferencedEntity(handle, predicate);
            }

            @Override
            protected Component createRelationshipView(String id, IModel<RBEntity> model) {
                return super.createRelationshipView(id, model);
            }

            @Override
            protected Component createInfoPanel(String id, IModel<RBEntity> model) {
                return new ItemEditorInfoPanel(id, new DerivedDetachableModel<ResourceNode, RBEntity>(model) {
                    @Override
                    protected ResourceNode derive(RBEntity entity) {
                        return entity.getNode();
                    }
                });
            }

            @Override
            protected Component createEntityPanel(String id, IModel<RBEntity> model) {
                return super.createEntityPanel(id, model);
            }

            @Override
            protected Component createClassifyPanel(String id, IModel<RBEntity> model) {
                return super.createClassifyPanel(id, model);
            }

            @Override
            protected Component createRelationshipPanel(String id, RBEntityModel model) {
                return super.createRelationshipPanel(id, model);
            }

            @Override
            protected LocalButtonBar createLocalButtonBar(String id, RBEntityModel model) {
                return super.createLocalButtonBar(id, model);
            }

            @Override
            protected void onSave(IModel<RBEntity> model) {
            }
        };
    }

}

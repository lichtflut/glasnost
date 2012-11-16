package de.lichtflut.glasnost.is.components.devops;

import de.lichtflut.glasnost.is.model.logic.Stage;
import de.lichtflut.glasnost.is.services.StageDefinitionService;
import de.lichtflut.rb.webck.components.common.TypedPanel;
import de.lichtflut.rb.webck.components.form.RBDefaultButton;
import de.lichtflut.rb.webck.models.basic.DerivedDetachableModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.context.Context;

import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.enableIf;
import static de.lichtflut.rb.webck.behaviors.ConditionalBehavior.visibleIf;
import static de.lichtflut.rb.webck.models.ConditionalModel.isNotNull;
import static de.lichtflut.rb.webck.models.ConditionalModel.isNull;

/**
 * <p>
 *  Panel for editing of a stage.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class StageEditPanel extends TypedPanel<Stage> {

    @SpringBean
    private StageDefinitionService service;

    // ----------------------------------------------------

    /**
     * Constructor.
     * @param id The component id.
     * @param model The model containing the stage.
     */
    public StageEditPanel(String id, IModel<Stage> model) {
        super(id, model);

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        Form form = new Form("form");
        form.add(new FeedbackPanel("feedback"));

        TextField<String> idField = new TextField<String>("id", new PropertyModel<String>(model, "ID"));
        idField.add(enableIf(isNull(new ContextModel(model))));
        form.add(idField);

        TextField<String> nameField = new TextField<String>("name", new PropertyModel<String>(model, "name"));
        form.add(nameField);

        form.add(createCancelButton());

        form.add(createSaveButton());

        add(form);

        add(visibleIf(isNotNull(model)));
    }

    // ----------------------------------------------------

    public void onUpdate() {
    }

    // ----------------------------------------------------

    protected Button createSaveButton() {
        return new RBDefaultButton("save") {
            @Override
            protected void applyActions(AjaxRequestTarget target, Form<?> form) {
                store();
            }
        };
    }

    protected Button createCancelButton() {
        return new RBDefaultButton("cancel") {
            @Override
            protected void applyActions(AjaxRequestTarget target, Form<?> form) {
                onUpdate();
            }
        };
    }

    protected void store() {
        Stage stage = getModelObject();
        service.store(stage);
        onUpdate();
    }

    // ----------------------------------------------------

    private class ContextModel extends DerivedDetachableModel<Context, Stage> {

        public ContextModel(IModel<Stage> original) {
            super(original);
        }

        @Override
        protected Context derive(Stage stage) {
            return stage.getContext();
        }
    }
}

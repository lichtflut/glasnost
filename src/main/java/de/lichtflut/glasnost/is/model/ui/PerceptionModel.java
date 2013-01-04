package de.lichtflut.glasnost.is.model.ui;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.webck.models.basic.AbstractLoadableDetachableModel;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * <p>
 *  Model loading all perceptions.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class PerceptionModel extends AbstractLoadableDetachableModel<List<Perception>> {

    @SpringBean
    private PerceptionDefinitionService perceptionDefinitionService;

    // ----------------------------------------------------

    public PerceptionModel() {
        super();
        Injector.get().inject(this);
    }

    // ----------------------------------------------------

    @Override
    public List<Perception> load() {
        return perceptionDefinitionService.findAllPerceptions();
    }

}

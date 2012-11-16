package de.lichtflut.glasnost.is.model.ui;

import de.lichtflut.glasnost.is.model.logic.Stage;
import de.lichtflut.glasnost.is.services.StageDefinitionService;
import de.lichtflut.rb.webck.models.basic.AbstractLoadableDetachableModel;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * <p>
 *  Model loading all stages.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class StagesModel extends AbstractLoadableDetachableModel<List<Stage>> {

    @SpringBean
    private StageDefinitionService stageDefinitionService;

    // ----------------------------------------------------

    public StagesModel() {
        super();
        Injector.get().inject(this);
    }

    // ----------------------------------------------------

    @Override
    public List<Stage> load() {
        return stageDefinitionService.findAllStages();
    }

}

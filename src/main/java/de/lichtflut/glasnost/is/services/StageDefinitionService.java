package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.model.logic.Stage;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;

import java.util.List;

/**
 * <p>
 * DESCRITPION.
 * </p>
 * <p/>
 * <p>
 * Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface StageDefinitionService {
    void store(Stage stage);

    Stage findByQualifiedName(QualifiedName qn);

    Stage findByContext(Context context);

    List<Stage> findAllStages();
}

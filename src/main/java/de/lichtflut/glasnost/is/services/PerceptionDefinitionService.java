package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.model.logic.Perception;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;

import java.util.List;

/**
 * <p>
 *  Service for the definitions of perceptions.
 * </p>
 * <p/>
 * <p>
 * Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface PerceptionDefinitionService {

    void store(Perception perception);

    Perception findByQualifiedName(QualifiedName qn);

    Perception findByContext(Context context);

    List<Perception> findAllPerceptions();
}

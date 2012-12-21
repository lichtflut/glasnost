package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import org.arastreju.sge.naming.QualifiedName;

import java.util.List;

/**
 * <p>
 *  Service interface for Perceptions.
 * </p>
 *
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface PerceptionService {

    PerceptionItem findItemByID(QualifiedName qn);

    List<PerceptionItem> getBaseItemsOfPerception(QualifiedName qn);

    void addBaseItemToPerception(PerceptionItem item, QualifiedName qn);
}

package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.model.logic.DevOpsItem;
import org.arastreju.sge.naming.QualifiedName;

import java.util.List;

/**
 * <p>
 * DESCRITPION.
 * </p>
 * <p/>
 * <p>
 * Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface DevOpsService {
    DevOpsItem findItemByID(QualifiedName qn);

    List<DevOpsItem> getBaseItemsOfStage(QualifiedName qn);

    void addBaseItemToStage(DevOpsItem item, QualifiedName qn);
}

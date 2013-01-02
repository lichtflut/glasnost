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

    /**
     * Find a perception item by it's qualified name.
     * @param qn The qualified name.
     * @return The item or null.
     */
    PerceptionItem findItemByID(QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Get all item's of the perception.
     * @param qn The qualified name of the perception.
     * @return The perception's items.
     */
    List<PerceptionItem> getItemsOfPerception(QualifiedName qn);

    /**
     * Add a new item to the perception.
     * @param item The new item.
     * @param qn The perception's qualified name.
     */
    void addItemToPerception(PerceptionItem item, QualifiedName qn);

    // ----------------------------------------------------

    /**
     * Get the base/root item's of the perception. Starting with this item the tree can be build.
     * @param qn The qualified name of the perception.
     * @return The perception's root items.
     */
    List<PerceptionItem> getBaseItemsOfPerception(QualifiedName qn);

    /**
     * Add a new base/root item to the perception.
     * @param item The new base/root item.
     * @param qn The perception's qualified name.
     */
    void addBaseItemToPerception(PerceptionItem item, QualifiedName qn);

}

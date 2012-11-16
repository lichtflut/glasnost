package de.lichtflut.glasnost.is;

import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;

/**
 * <p>
 *  Constants for Glasnost Information Server.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface GIS {

    String GIS_NAMESPACE_URI = "http://glasnost.lichtflut.de/definitions/";

    // ----------------------------------------------------

    /**
     * A stage represents a contexts.
     */
    ResourceID REPRESENTS_CONTEXT = new SimpleResourceID(GIS_NAMESPACE_URI, "representsContext");

}

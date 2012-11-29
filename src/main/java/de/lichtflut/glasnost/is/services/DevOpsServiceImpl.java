package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.model.logic.DevOpsItem;
import de.lichtflut.glasnost.is.model.logic.Stage;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>
 *  Service for managment of IT items and landscapes.
 * </p>
 *
 * <p>
 *  Created 29.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class DevOpsServiceImpl implements DevOpsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevOpsServiceImpl.class);

    private ServiceContext context;

    private ArastrejuResourceFactory arasFactory;

    // ----------------------------------------------------

    public DevOpsServiceImpl(ServiceContext context, ArastrejuResourceFactory arasFactory) {
        this.context = context;
        this.arasFactory = arasFactory;
    }

    // ----------------------------------------------------

    @Override
    public DevOpsItem findItemByID(QualifiedName qn) {
        return DevOpsItem.from(conversation().findResource(qn));
    }

    // ----------------------------------------------------

    @Override
    public List<DevOpsItem> getBaseItemsOfStage(QualifiedName qn) {
        Stage stage = Stage.from(conversation().findResource(qn));
        if (stage != null) {
            return stage.getTreeRootItems();
        } else {
            throw new IllegalArgumentException("Requested stage does not exist: " + qn);
        }
    }

    @Override
    public void addBaseItemToStage(DevOpsItem item, QualifiedName qn) {
        Stage stage = Stage.from(conversation().findResource(qn));
        if (stage != null) {
            stage.addTreeRootItem(item);
        } else {
            throw new IllegalArgumentException("Requested stage does not exist: " + qn);
        }
    }

    // ----------------------------------------------------

    private Conversation conversation() {
        return arasFactory.getConversation();
    }

}

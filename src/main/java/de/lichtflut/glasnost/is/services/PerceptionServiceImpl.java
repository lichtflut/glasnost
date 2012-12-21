package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;
import org.arastreju.sge.Conversation;
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
public class PerceptionServiceImpl implements PerceptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerceptionServiceImpl.class);

    private ServiceContext context;

    private ArastrejuResourceFactory arasFactory;

    // ----------------------------------------------------

    public PerceptionServiceImpl(ServiceContext context, ArastrejuResourceFactory arasFactory) {
        this.context = context;
        this.arasFactory = arasFactory;
    }

    // ----------------------------------------------------

    @Override
    public PerceptionItem findItemByID(QualifiedName qn) {
        return PerceptionItem.from(conversation().findResource(qn));
    }

    // ----------------------------------------------------

    @Override
    public List<PerceptionItem> getBaseItemsOfPerception(QualifiedName qn) {
        Perception perception = Perception.from(conversation().findResource(qn));
        if (perception != null) {
            return perception.getTreeRootItems();
        } else {
            throw new IllegalArgumentException("Requested perception does not exist: " + qn);
        }
    }

    @Override
    public void addBaseItemToPerception(PerceptionItem item, QualifiedName qn) {
        Perception attachedPerception = Perception.from(conversation().findResource(qn));
        if (attachedPerception != null) {
            attachedPerception.addTreeRootItem(item);
        } else {
            throw new IllegalArgumentException("Requested stage does not exist: " + qn);
        }
    }

    // ----------------------------------------------------

    private Conversation conversation() {
        return arasFactory.getConversation();
    }

}

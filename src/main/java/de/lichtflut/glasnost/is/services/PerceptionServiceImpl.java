package de.lichtflut.glasnost.is.services;

import java.util.ArrayList;
import java.util.List;

import de.lichtflut.glasnost.is.GIS;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionItem;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;

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

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(PerceptionServiceImpl.class);

	@SuppressWarnings("unused")
	private final ServiceContext context;

	private final ArastrejuResourceFactory arasFactory;

	// ----------------------------------------------------

	public PerceptionServiceImpl(final ServiceContext context, final ArastrejuResourceFactory arasFactory) {
		this.context = context;
		this.arasFactory = arasFactory;
	}

	// ----------------------------------------------------

	@Override
	public PerceptionItem findItemByID(final QualifiedName qn) {
		return PerceptionItem.from(conversation().findResource(qn));
	}

	// ----------------------------------------------------

    @Override
    public List<PerceptionItem> getItemsOfPerception(QualifiedName qn) {
        List<PerceptionItem> result = new ArrayList<PerceptionItem>();
        Query query = conversation().createQuery().addField(GIS.BELONGS_TO_PERCEPTION, qn);
        for (ResourceNode node : query.getResult()) {
            result.add(PerceptionItem.from(node));
        }
        return result;
    }

    @Override
    public void addItemToPerception(PerceptionItem item, QualifiedName qn) {
        Perception attachedPerception = Perception.from(conversation().findResource(qn));
        PerceptionItem attachedItem = PerceptionItem.from(conversation().resolve(item));
        if (attachedPerception != null) {
            attachedItem.setPerception(attachedPerception);
        } else {
            throw new IllegalArgumentException("Requested perception does not exist: " + qn);
        }
    }

    // ----------------------------------------------------

	@Override
	public List<PerceptionItem> getBaseItemsOfPerception(final QualifiedName qn) {
		Perception perception = Perception.from(conversation().findResource(qn));
		if (perception != null) {
			return perception.getTreeRootItems();
		} else {
			throw new IllegalArgumentException("Requested perception does not exist: " + qn);
		}
	}

	@Override
	public void addBaseItemToPerception(final PerceptionItem item, final QualifiedName qn) {
		Perception attachedPerception = Perception.from(conversation().findResource(qn));
        PerceptionItem attachedItem = PerceptionItem.from(conversation().resolve(item));
		if (attachedPerception != null) {
            attachedItem.setPerception(attachedPerception);
			attachedPerception.addTreeRootItem(item);
		} else {
			throw new IllegalArgumentException("Requested perception does not exist: " + qn);
		}
	}

	// ----------------------------------------------------

	private Conversation conversation() {
		return arasFactory.getConversation();
	}

}

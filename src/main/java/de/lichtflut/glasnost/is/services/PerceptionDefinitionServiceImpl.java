package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Service for creating, changing and reading the stages.
 * </p>
 *
 * <p>
 *  Created Nov 16, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class PerceptionDefinitionServiceImpl implements PerceptionDefinitionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerceptionDefinitionServiceImpl.class);

    private ServiceContext context;

    private ArastrejuResourceFactory arasFactory;

    // ----------------------------------------------------

    public PerceptionDefinitionServiceImpl(ServiceContext context, ArastrejuResourceFactory arasFactory) {
        this.context = context;
        this.arasFactory = arasFactory;
    }

    // ----------------------------------------------------

    @Override
    public void store(Perception perception) {
        if (perception.getContext() == null) {
            LOGGER.info("Creating new context for perception {}.", perception.getID());
            Context ctx = new SimpleContextID(GIS.PERCEPTION_CONTEXT_NAMESPACE_URI, perception.getID());
            arasFactory.getOrganizer().registerContext(ctx.getQualifiedName());
            perception.setContext(ctx);
            LOGGER.info("Registered new perception context {}.", ctx.getQualifiedName());
        } else {
            LOGGER.info("Updating existing perception {}.", perception);

        }

        conversation().attach(perception);
    }

    @Override
    public Perception findByQualifiedName(QualifiedName qn) {
        return Perception.from(conversation().findResource(qn));
    }

    @Override
    public Perception findByContext(Context context) {
        QueryResult result = conversation().createQuery().addField(GIS.REPRESENTS_CONTEXT, context).getResult();
        if (result.size() > 1) {
            LOGGER.error("More than one stage registered for context {}.", context);
        }
        return Perception.from(result.getSingleNode());
    }

    @Override
    public List<Perception> findAllPerceptions() {
        final List<Perception> result = new ArrayList<Perception>();
        final Query query = conversation().createQuery();
        query.addField(RDF.TYPE, GIS.PERCEPTION);
        for (ResourceNode stageNode : query.getResult()) {
            result.add(Perception.from(stageNode));
        }
        return result;
    }

    // ----------------------------------------------------

    private Conversation conversation() {
        return arasFactory.getConversation();
    }

}

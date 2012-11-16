package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.model.logic.Stage;
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
public class StageDefinitionServiceImpl implements StageDefinitionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageDefinitionServiceImpl.class);

    private ServiceContext context;

    private ArastrejuResourceFactory arasFactory;

    // ----------------------------------------------------

    public StageDefinitionServiceImpl(ServiceContext context, ArastrejuResourceFactory arasFactory) {
        this.context = context;
        this.arasFactory = arasFactory;
    }

    // ----------------------------------------------------

    @Override
    public void store(Stage stage) {
        if (stage.getContext() == null) {
            LOGGER.info("Creating new context for stage {}.", stage.getID());
            Context ctx = new SimpleContextID(GIS.STAGE_CONTEXT_NAMESPACE_URI, stage.getID());
            arasFactory.getOrganizer().registerContext(ctx.getQualifiedName());
            stage.setContext(ctx);
            LOGGER.info("Registered new stage context {}.", ctx.getQualifiedName());
        } else {
            LOGGER.info("Updating existing stage {}.", stage);

        }

        conversation().attach(stage);
    }

    @Override
    public Stage findByQualifiedName(QualifiedName qn) {
        return Stage.from(conversation().findResource(qn));
    }

    @Override
    public Stage findByContext(Context context) {
        QueryResult result = conversation().createQuery().addField(GIS.REPRESENTS_CONTEXT, context).getResult();
        if (result.size() > 1) {
            LOGGER.error("More than one stage registered for context {}.", context);
        }
        return Stage.from(result.getSingleNode());
    }

    @Override
    public List<Stage> findAllStages() {
        final List<Stage> result = new ArrayList<Stage>();
        final Query query = conversation().createQuery();
        query.addField(RDF.TYPE, GIS.STAGE);
        for (ResourceNode stageNode : query.getResult()) {
            result.add(Stage.from(stageNode));
        }
        return result;
    }

    // ----------------------------------------------------

    private Conversation conversation() {
        return arasFactory.getConversation();
    }

}

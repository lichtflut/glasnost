package de.lichtflut.glasnost.is.services;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.model.logic.Stage;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;
import org.arastreju.sge.Conversation;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class StageDefinitionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageDefinitionService.class);

    private ServiceContext context;

    private ArastrejuResourceFactory arasFactory;

    // ----------------------------------------------------

    public StageDefinitionService(ServiceContext context, ArastrejuResourceFactory arasFactory) {
        this.context = context;
        this.arasFactory = arasFactory;
    }

    // ----------------------------------------------------

    public void store(Stage stage) {
        conversation().attach(stage);
    }

    public Stage findByQualifiedName(QualifiedName qn) {
        return Stage.from(conversation().findResource(qn));
    }

    public Stage findByContext(Context context) {
        QueryResult result = conversation().createQuery().addField(GIS.REPRESENTS_CONTEXT, context).getResult();
        if (result.size() > 1) {
            LOGGER.error("More than one stage registered for context {}.", context);
        }
        return Stage.from(result.getSingleNode());
    }

    // ----------------------------------------------------

    private Conversation conversation() {
        return arasFactory.getConversation();
    }

}

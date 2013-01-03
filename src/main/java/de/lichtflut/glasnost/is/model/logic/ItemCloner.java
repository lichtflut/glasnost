package de.lichtflut.glasnost.is.model.logic;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.DetachedStatement;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.StatementMetaInfo;
import org.arastreju.sge.model.associations.HalfStatement;
import org.arastreju.sge.model.nodes.StatementOrigin;
import org.arastreju.sge.model.nodes.views.InheritedDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *  Cloner of perception items.
 * </p>
 *
 * <p>
 *  Created 03.01.13
 * </p>
 *
 * @author Oliver Tigges
 */
public class ItemCloner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCloner.class);

    private final Set<ResourceID> predicateBlackList = new HashSet<ResourceID>();

    // ----------------------------------------------------

    public ItemCloner() {
        predicateBlackList.add(GIS.BELONGS_TO_PERCEPTION);
        predicateBlackList.add(Aras.INHERITS_FROM);
        predicateBlackList.add(RB.HAS_CHILD_NODE);
    }

    // ----------------------------------------------------

    public PerceptionItem createClone(PerceptionItem original) {
        PerceptionItem clone = new PerceptionItem();
        for (Statement stmt : original.getAssociations()) {
            StatementMetaInfo metaInfo = stmt.getMetaInfo();
            if (predicateBlackList.contains(stmt.getPredicate())) {
                continue;
            }
            if (!StatementOrigin.ASSERTED.equals(metaInfo.getOrigin())) {
                continue;
            }
            clone.addAssociation(stmt.getPredicate(), stmt.getObject());
        }
        clone.addAssociation(Aras.INHERITS_FROM, original);
        clone.addAssociation(GIS.BASED_ON, original);
        for (ResourceID predicate : predicateBlackList) {
            InheritedDecorator.revoke(clone, new HalfStatement(predicate, Aras.ANY));
        }
        return clone;
    }

}

package de.lichtflut.glasnost.is.model.logic;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.Aras;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.StatementMetaInfo;
import org.arastreju.sge.model.associations.HalfStatement;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.model.nodes.StatementOrigin;
import org.arastreju.sge.model.nodes.views.InheritedDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
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
        // statements with these predicates are not to be cloned
        predicateBlackList.add(GIS.BELONGS_TO_PERCEPTION);
        predicateBlackList.add(RB.HAS_CHILD_NODE);
    }

    // ----------------------------------------------------

    public PerceptionItem createClone(PerceptionItem original) {
        PerceptionItem clone = initClone(original);
        cloneAssociations(clone, original);
        defineRevocations(clone);
        rebaseClone(clone, original);
        return clone;
    }

    // ----------------------------------------------------

    private PerceptionItem initClone(PerceptionItem original) {
        PerceptionItem clone = new PerceptionItem();
        clone.addAssociation(Aras.INHERITS_FROM, original);
        clone.addAssociation(GIS.BASED_ON, original);
        return clone;
    }

    private void cloneAssociations(PerceptionItem target, PerceptionItem original) {
        for (Statement stmt : original.getAssociations()) {
            StatementMetaInfo metaInfo = stmt.getMetaInfo();
            if (predicateBlackList.contains(stmt.getPredicate())) {
                continue;
            }
            if (!StatementOrigin.ASSERTED.equals(metaInfo.getOrigin())) {
                // ignore inferred and inherited statements.
                continue;
            }
            target.addAssociation(stmt.getPredicate(), stmt.getObject());
        }
    }

    private void defineRevocations(PerceptionItem clone) {
        for (ResourceID predicate : predicateBlackList) {
            InheritedDecorator.revoke(clone, new HalfStatement(predicate, Aras.ANY));
        }
    }

    /**
     * The cloned not must not inherit the base of the original item. It will inherit this indirectly.
     */
    private void rebaseClone(PerceptionItem clone, PerceptionItem original) {
        Collection<SemanticNode> originalBases = getBases(original);
        for (SemanticNode current : originalBases) {
            InheritedDecorator.revoke(clone, new HalfStatement(GIS.BASED_ON, current));
            InheritedDecorator.revoke(clone, new HalfStatement(Aras.INHERITS_FROM, current));
        }
    }

    private Collection<SemanticNode> getBases(PerceptionItem item) {
        return SNOPS.objects(item, GIS.BASED_ON);
    }

}

/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.lichtflut.glasnost.is.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.arastreju.sge.Conversation;
import org.arastreju.sge.SNOPS;
import org.arastreju.sge.apriori.RDF;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.ContextID;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.model.nodes.SemanticNode;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.query.Query;
import org.arastreju.sge.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.model.logic.PerceptionCloner;
import de.lichtflut.rb.core.services.ArastrejuResourceFactory;
import de.lichtflut.rb.core.services.ServiceContext;

/**
 * <p>
 *  Service for creating, changing and reading the perceptions.
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

	private final ArastrejuResourceFactory arasFactory;

	// ----------------------------------------------------

	public PerceptionDefinitionServiceImpl(final ServiceContext context, final ArastrejuResourceFactory arasFactory) {
		this.arasFactory = arasFactory;
	}

	// ----------------------------------------------------

	@Override
	public void store(final Perception perception) {
		if (perception.getContext() == null) {
			LOGGER.info("Creating new context for perception {}.", perception.getID());
			Context ctx = ContextID.forContext(GIS.PERCEPTION_CONTEXT_NAMESPACE_URI, perception.getID());
			arasFactory.getOrganizer().registerContext(ctx.getQualifiedName());
			perception.setContext(ctx);
			LOGGER.info("Registered new perception context {}.", ctx.getQualifiedName());
		} else {
			LOGGER.info("Updating existing perception {}.", perception);

		}

		conversation().attach(perception);
	}

	@Override
	public void store(final List<Perception> perceptions) {
		for (Perception perception : perceptions) {
			store(perception);
		}
	}

	@Override
	public void delete(final Perception perception) {
		Set<SemanticNode> items = SNOPS.objects(perception, GIS.BELONGS_TO_PERCEPTION);
		for (SemanticNode semanticNode : items) {
			conversation().remove(semanticNode.asResource());
		}
		conversation().remove(perception);
		LOGGER.info("Removed perception: " + perception.toURI());
	}

	@Override
	public Perception findByQualifiedName(final QualifiedName qn) {
		return Perception.from(conversation().findResource(qn));
	}

	@Override
	public Perception findByContext(final Context context) {
		QueryResult result = conversation().createQuery().addField(GIS.REPRESENTS_CONTEXT, context).getResult();
		if (result.size() > 1) {
			LOGGER.error("More than one perception registered for context {}.", context);
		}
		return Perception.from(result.getSingleNode());
	}

	@Override
	public List<Perception> findAllPerceptions() {
		final List<Perception> result = new ArrayList<Perception>();
		final Query query = conversation().createQuery();
		query.addField(RDF.TYPE, GIS.PERCEPTION);
		for (ResourceNode perceptionNode : query.getResult()) {
			result.add(Perception.from(perceptionNode));
		}
		return result;
	}

	// ----------------------------------------------------

	@Override
	public void definePerceptionBase(final Perception target, final QualifiedName baseQN) {
		Perception base = findByQualifiedName(baseQN);
		if (base == null) {
			throw new IllegalArgumentException("Base perception not found: " + baseQN);
		}
		target.setBasePerception(base);
	}

	@Override
	public Perception cloneItems(final QualifiedName targetQN, final QualifiedName baseQN) {
		Perception base = findByQualifiedName(baseQN);
		Perception target = findByQualifiedName(targetQN);

		if (base == null) {
			throw new IllegalArgumentException("Base perception not found: " + baseQN);
		}
		if (target == null) {
			target = new Perception(targetQN);
		}

		new PerceptionCloner(base).clone(target);
		return target;
	}

	@Override
	public List<ResourceNode> findAllPerceptionCategories() {
		final Query query = conversation().createQuery();
		query.addField(RDF.TYPE, GIS.PERCEPTION_CATEGORY);
		return query.getResult().toList(2000);
	}

	// ----------------------------------------------------

	private Conversation conversation() {
		return arasFactory.getConversation();
	}

}

package de.lichtflut.glasnost.is.services;

import java.util.List;

import org.arastreju.sge.context.Context;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;

import de.lichtflut.glasnost.is.model.logic.Perception;

/**
 * <p>
 *  Service for the definitions of perceptions.
 * </p>
 * <p/>
 * <p>
 * Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public interface PerceptionDefinitionService {

	void store(Perception perception);

	Perception findByQualifiedName(QualifiedName qn);

	Perception findByContext(Context context);

	List<Perception> findAllPerceptions();

	// ----------------------------------------------------

	/**
	 * Define the base perception of the given perception.
	 * @param perception The perception.
	 * @param base The qualified base perception.
	 */
	void definePerceptionBase(Perception perception, QualifiedName base);

	/**
	 * Clone all items (not yet cloned) from base perception.
	 * @param target The target perception to contain the cloned items.
	 * @param base The base perception containing the original items.
	 * @return The perception containing the cloned items.
	 */
	Perception cloneItems(QualifiedName target, QualifiedName base);

	/**
	 * Retrieves a List of all known perception categories.
	 * @return a List of ResourceNodes
	 */
	List<ResourceNode> findAllPerceptionCategories();

}

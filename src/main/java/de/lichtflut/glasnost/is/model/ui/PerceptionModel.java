package de.lichtflut.glasnost.is.model.ui;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.structure.OrderBySerialNumber;

import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.glasnost.is.services.PerceptionDefinitionService;
import de.lichtflut.rb.webck.models.basic.AbstractLoadableDetachableModel;

/**
 * <p>
 *  Model loading all perceptions.
 * </p>
 *
 * <p>
 *  Created 16.11.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class PerceptionModel extends AbstractLoadableDetachableModel<List<Perception>> {

	@SpringBean
	private PerceptionDefinitionService perceptionDefinitionService;

	// ----------------------------------------------------

	public PerceptionModel() {
		super();
		Injector.get().inject(this);
	}

	// ----------------------------------------------------

	@Override
	public List<Perception> load() {
		List<Perception> perceptions = perceptionDefinitionService.findAllPerceptions();
		Collections.sort(perceptions, new OrderBySerialNumber());
		return perceptions;
	}

	public void remove(final Perception perception){
		int indexOf = getObject().indexOf(perception);
		getObject().remove(indexOf);
	}

}

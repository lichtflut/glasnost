/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package de.lichtflut.glasnost.is.data;

import java.util.Date;

import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.core.entity.impl.RBEntityImpl;

/**
 * <p>
 * This class provides {@link RBEntity}s for testing purposes.
 * </p>
 * Created: May 7, 2012
 *
 * @author Ravi Knox
 */
public class RBEntityFactory {

	/**
	 * Mock Person Entity. Not all Fields are set.
	 * @return
	 */
	public static RBEntity createPersonEntity(){
		RBEntity entity = new RBEntityImpl(de.lichtflut.glasnost.is.data.ResourceSchemaFactory.buildPersonSchema());
		entity.getField(RBConstants.HAS_FIRST_NAME).setValue(0, "Hans");
		entity.getField(RBConstants.HAS_LAST_NAME).setValue(0, "Müller");
		entity.getField(RBConstants.HAS_DATE_OF_BIRTH).setValue(0, new Date());
		entity.getField(RBConstants.HAS_EMAIL).setValue(0, "hmüller@google.de");
		return entity;
	}

}

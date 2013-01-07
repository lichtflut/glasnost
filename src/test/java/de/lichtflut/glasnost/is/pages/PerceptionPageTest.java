/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;

import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.webck.common.DisplayMode;


/**
 * <p>
 * Testclass for {@link PerceptionPage}.
 * </p>
 * Created: Jan 4, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionPageTest extends GlasnostWebTest{

	private static final Model<DisplayMode> CREATE_MODE = new Model<DisplayMode>(DisplayMode.CREATE);

	// ------------- SetUp & tearDown -----------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setupTest() {
		initNeccessaryPageData();
	}

	// ------------------------------------------------------

	/**
	 * Scenario: User is not logged in, calls Perception {@link PerceptionPage} ind create mode.
	 */
	@Test
	public void testPerceptionPageNotLoggedIn(){
		IModel<Perception> model = new Model<Perception>(new Perception());
		PerceptionPage page = new PerceptionPage(model, CREATE_MODE);

		tester.startPage(page);

		tester.assertRenderedPage(LoginPage.class);
	}

	/**
	 * Scenario: Load page with non-existing (blank) perception in create mode.
	 */
	@Test
	public void testPerceptionPageCreateMode(){
		initNeccessaryPageData();
		IModel<Perception> model = new Model<Perception>(new Perception());
		PerceptionPage page = new PerceptionPage(model, CREATE_MODE){
			@Override
			protected boolean needsAuthentication() {
				return false;
			}
		};

		tester.startPage(page);

		tester.assertRenderedPage(PerceptionPage.class);
		tester.assertComponent("perceptionEditor", PerceptionEditPanel.class);
		tester.assertInvisible("perceptionDisplay");
	}

	/**
	 * Scenario: Load page with existing perception.
	 */
	@Test
	public void testPerceptionPageEditMode(){
		// FIXME
	}

	/**
	 * Scenario: Load page without perception.
	 */
	@Test
	public void testPerceptionPageNoPerception(){
	}
}

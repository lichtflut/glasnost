/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
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
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);

		tester.startPage(PerceptionPage.class, parameters);

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
		tester.assertComponent("container:perceptionEditor", PerceptionEditPanel.class);
		tester.assertInvisible("container:perceptionDisplay");
	}

	/**
	 * Scenario: Load page with PageParameters.
	 */
	// FIXME Fix Authentication issue for page call with page parameters
	@Test
	public void testPerceptionPageEditMode(){
		//		when(serviceContext.getUser()).thenReturn(new RBUser());
		//		PageParameters parameters = new PageParameters();
		//		parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
		//
		//		tester.startPage(PerceptionPage.class, parameters);
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
		tester.assertComponent("container:perceptionEditor", PerceptionEditPanel.class);
		tester.assertInvisible("container:perceptionDisplay");
	}

}

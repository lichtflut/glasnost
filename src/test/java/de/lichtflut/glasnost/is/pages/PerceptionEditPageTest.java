/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.naming.QualifiedName;
import org.junit.Test;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.glasnost.is.GlasnostWebTest;
import de.lichtflut.glasnost.is.components.colorpicker.ColorPickerPanel;
import de.lichtflut.glasnost.is.components.devops.perceptions.PerceptionEditPanel;
import de.lichtflut.glasnost.is.data.RBEntityFactory;
import de.lichtflut.glasnost.is.model.logic.Perception;
import de.lichtflut.rb.application.common.CommonParams;
import de.lichtflut.rb.core.entity.RBEntity;
import de.lichtflut.rb.webck.common.DisplayMode;
import de.lichtflut.rb.webck.components.fields.EntityPickerField;

/**
 * <p>
 * Testclass for {@link PerceptionEditPage}.
 * </p>
 * Created: Jan 18, 2013
 *
 * @author Ravi Knox
 */
public class PerceptionEditPageTest extends GlasnostWebTest{

	private RBEntity owner, personResponsible, perceptionCategory;
	private Perception perception;

	// ------------- SetUp & tearDown -----------------------

	@Override
	protected void setupTest() {
		owner = RBEntityFactory.createPersonEntity();
		personResponsible = RBEntityFactory.createPersonEntity();
		perceptionCategory = RBEntityFactory.createPerceptionType();
		perception = createPerception();
		initNeccessaryPageData();
	}

	// ------------------------------------------------------

	/**
	 * Scenario: User is not logged in, calls Perception {@link PerceptionDisplayPage} in create mode.
	 */
	@Test
	public void testPerceptionPageNotLoggedIn(){
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);

		tester.startPage(PerceptionDisplayPage.class, parameters);

		tester.assertRenderedPage(LoginPage.class);
	}

	/**
	 * Scenario: Load page with non-existing (blank) perception in create mode.
	 */
	@Test
	public void testPerceptionPageCreateMode(){
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.CREATE);
		PerceptionEditPage page = new PerceptionEditPage(parameters){
			@Override
			protected boolean needsAuthentication() {
				return false;
			}
		};

		tester.startPage(page);

		tester.assertRenderedPage(page.getPageClass());
		tester.assertComponent("perceptionEditor", PerceptionEditPanel.class);
	}

	/**
	 * Scenario: Load page with existing perception in edit mode.
	 */
	@Test
	public void testPerceptionPageEditMode(){
		when(networkService.find(perception.getQualifiedName())).thenReturn(perception);
		PageParameters parameters = new PageParameters();
		parameters.add(DisplayMode.PARAMETER, DisplayMode.EDIT);
		parameters.add(CommonParams.PARAM_RESOURCE_ID, perception.getQualifiedName());
		PerceptionEditPage page = new PerceptionEditPage(parameters){
			@Override
			protected boolean needsAuthentication() {
				return false;
			}
		};

		tester.startPage(page);

		verify(networkService, times(1)).find(perception.getQualifiedName());

		tester.assertRenderedPage(page.getPageClass());
		tester.assertComponent("perceptionEditor", PerceptionEditPanel.class);
		tester.assertComponent("perceptionEditor:form:color", ColorPickerPanel.class);
		tester.assertComponent("perceptionEditor:form:owner", EntityPickerField.class);
	}

	// ------------------------------------------------------

	private Perception createPerception() {
		Perception perception = new Perception();
		perception.setBasePerception(new Perception());
		QualifiedName contextQN = new QualifiedName(GIS.PERCEPTION_CONTEXT_NAMESPACE_URI);
		perception.setContext(new SimpleContextID(contextQN));
		perception.setImagePath("/home/glasnost/testpath");
		perception.setName("Glasnost test Perception");
		perception.setType(perceptionCategory.getNode());
		perception.setColor("#fff");
		perception.setDescription("Glasnost test perception");
		perception.setOwner(owner.getID());
		perception.setPersonResponsible(personResponsible.getID());
		return perception;
	}
}

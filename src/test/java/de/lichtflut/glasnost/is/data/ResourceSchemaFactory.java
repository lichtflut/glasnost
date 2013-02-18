/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */

package de.lichtflut.glasnost.is.data;

import java.util.Locale;

import de.lichtflut.glasnost.is.GIS;
import de.lichtflut.rb.core.RB;
import de.lichtflut.rb.core.schema.model.Datatype;
import de.lichtflut.rb.core.schema.model.PropertyDeclaration;
import de.lichtflut.rb.core.schema.model.ResourceSchema;
import de.lichtflut.rb.core.schema.model.impl.CardinalityBuilder;
import de.lichtflut.rb.core.schema.model.impl.ExpressionBasedLabelBuilder;
import de.lichtflut.rb.core.schema.model.impl.FieldLabelDefinitionImpl;
import de.lichtflut.rb.core.schema.model.impl.LabelExpressionParseException;
import de.lichtflut.rb.core.schema.model.impl.PropertyDeclarationImpl;
import de.lichtflut.rb.core.schema.model.impl.ResourceSchemaImpl;

/**
 * <p>
 * This class provides ResourceSchemas for testing purposes.
 * </p>
 * Created: May 4, 2012
 * 
 * @author Ravi Knox
 */
public class ResourceSchemaFactory {

	public static ResourceSchema buildPersonSchema() {
		ResourceSchemaImpl schema = new ResourceSchemaImpl(RB.PERSON);

		PropertyDeclaration firstname = new PropertyDeclarationImpl(RB.HAS_FIRST_NAME, Datatype.STRING);
		PropertyDeclaration lastname = new PropertyDeclarationImpl(RB.HAS_LAST_NAME, Datatype.STRING);
		PropertyDeclaration address = new PropertyDeclarationImpl(RB.HAS_ADDRESS, Datatype.RESOURCE);
		PropertyDeclaration dateOfBirth = new PropertyDeclarationImpl(RB.HAS_DATE_OF_BIRTH, Datatype.DATE);
		PropertyDeclaration	email = new PropertyDeclarationImpl(RB.HAS_EMAIL, Datatype.STRING);
		PropertyDeclaration children = new PropertyDeclarationImpl(RB.HAS_CHILD_NODE, Datatype.RESOURCE);

		schema.addQuickInfo(RB.HAS_FIRST_NAME);
		schema.addQuickInfo(RB.HAS_LAST_NAME);
		schema.addQuickInfo(RB.HAS_EMAIL);

		address.setConstraint(ConstraintsFactory.buildTypeConstraint(GISTestConstants.ADDRESS));
		email.setConstraint(ConstraintsFactory.buildPublicEmailConstraint());
		children.setConstraint(ConstraintsFactory.buildTypeConstraint(RB.PERSON));

		firstname.setCardinality(CardinalityBuilder.hasExcactlyOne());
		lastname.setCardinality(CardinalityBuilder.hasExcactlyOne());
		address.setCardinality(CardinalityBuilder.hasOptionalOneToMany());
		dateOfBirth.setCardinality(CardinalityBuilder.hasExcactlyOne());
		email.setCardinality(CardinalityBuilder.hasOptionalOneToMany());

		firstname.setFieldLabelDefinition(new FieldLabelDefinitionImpl("Firstname"));
		firstname.getFieldLabelDefinition().setLabel(Locale.GERMAN, "Vorname");
		lastname.setFieldLabelDefinition(new FieldLabelDefinitionImpl("Surename"));
		lastname.getFieldLabelDefinition().setLabel(Locale.GERMAN, "Nachname");
		address.setFieldLabelDefinition(new FieldLabelDefinitionImpl("Address"));
		address.getFieldLabelDefinition().setLabel(Locale.GERMAN, "Adresse");
		dateOfBirth.setFieldLabelDefinition(new FieldLabelDefinitionImpl("Date of Birth"));
		dateOfBirth.getFieldLabelDefinition().setLabel(Locale.GERMAN, "Geburtsdatum");
		email.setFieldLabelDefinition(new FieldLabelDefinitionImpl("E-Mail"));
		email.getFieldLabelDefinition().setLabel(Locale.GERMAN, "Email");
		children.setFieldLabelDefinition(new FieldLabelDefinitionImpl("Children"));
		children.getFieldLabelDefinition().setLabel(Locale.GERMAN, "Kinder");

		schema.addPropertyDeclaration(firstname);
		schema.addPropertyDeclaration(lastname);
		schema.addPropertyDeclaration(address);
		schema.addPropertyDeclaration(dateOfBirth);
		schema.addPropertyDeclaration(email);
		schema.addPropertyDeclaration(children);


		try {
			schema.setLabelBuilder(new ExpressionBasedLabelBuilder(RB.PERSON.toURI()));
		} catch (LabelExpressionParseException e) {
			e.printStackTrace();
		}
		return schema;
	}

	public static ResourceSchema buildPerceptionCategory() {
		ResourceSchema schema = new ResourceSchemaImpl(GIS.PERCEPTION_CATEGORY);

		PropertyDeclaration nameProperty = new PropertyDeclarationImpl(RB.HAS_NAME, Datatype.STRING);
		nameProperty.setCardinality(CardinalityBuilder.hasExcactlyOne());
		nameProperty.setFieldLabelDefinition(new FieldLabelDefinitionImpl("Perception type"));

		schema.addPropertyDeclaration(nameProperty);
		return schema;
	}

	public static ResourceSchema buildDatatCenter(){
		ResourceSchema schema = new ResourceSchemaImpl(GIS.DATA_CENTER);

		PropertyDeclaration id = new PropertyDeclarationImpl(RB.HAS_ID, Datatype.STRING);
		schema.addPropertyDeclaration(id);

		PropertyDeclaration name = new PropertyDeclarationImpl(RB.HAS_NAME, Datatype.STRING);
		schema.addPropertyDeclaration(name);

		PropertyDeclaration description = new PropertyDeclarationImpl(RB.HAS_DESCRIPTION, Datatype.STRING);
		schema.addPropertyDeclaration(description);

		PropertyDeclaration hostMachine = new PropertyDeclarationImpl(GISTestConstants.HAS_HOST_MACHINE, Datatype.RESOURCE);
		hostMachine.setConstraint(ConstraintsFactory.buildTypeConstraint(GISTestConstants.PHYSICAL_MACHINE));
		schema.addPropertyDeclaration(hostMachine);

		PropertyDeclaration inheritsFrom = new PropertyDeclarationImpl(GIS.DATA_CENTER, Datatype.RESOURCE);
		inheritsFrom.setConstraint(ConstraintsFactory.buildTypeConstraint(GIS.DATA_CENTER));
		schema.addPropertyDeclaration(inheritsFrom);

		return schema;
	}
}

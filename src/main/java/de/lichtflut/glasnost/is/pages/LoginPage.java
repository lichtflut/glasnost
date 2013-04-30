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
package de.lichtflut.glasnost.is.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

import de.lichtflut.glasnost.is.components.AboutTeaserPanel;
import de.lichtflut.rb.application.base.AbstractLoginPage;
import de.lichtflut.rb.application.custom.RequestAccountPage;
import de.lichtflut.rb.application.custom.ResetPasswordPage;
import de.lichtflut.rb.core.security.LoginData;
import de.lichtflut.rb.webck.components.login.LoginPanel;
import de.lichtflut.rb.webck.models.infra.VersionInfoModel;

/**
 * <p>
 * Login page.
 * </p>
 * 
 * <p>
 * Created Dec 8, 2011
 * </p>
 * 
 * @author Oliver Tigges
 */
public class LoginPage extends AbstractLoginPage {

	/**
	 * Constructor.
	 */
	public LoginPage() {

		add(new LoginPanel("loginPanel") {
			@Override
			public void onLogin(final LoginData loginData) {
				tryLogin(loginData);
			}
		});

		add(new Link<String>("resetEmail") {
			@Override
			public void onClick() {
				setResponsePage(ResetPasswordPage.class);
			}
		});

		add(new Link<String>("requestAccount") {
			@Override
			public void onClick() {
				setResponsePage(RequestAccountPage.class);
			}
		});

		add(new AboutTeaserPanel("about"));

		addVersionInfo();
	}

	// ----------------------------------------------------

	private void addVersionInfo() {
		final VersionInfoModel model = new VersionInfoModel();
		add(new Label("version", new PropertyModel<String>(model, "version")));
		add(new Label("build", new PropertyModel<String>(model, "buildTimestamp")));
	}

}

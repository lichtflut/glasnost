/*
 * Copyright 2011 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.pages;

import de.lichtflut.glasnost.is.components.AboutTeaserPanel;
import de.lichtflut.rb.application.common.RBPermission;
import de.lichtflut.rb.application.custom.RequestAccountPage;
import de.lichtflut.rb.application.custom.ResetPasswordPage;
import de.lichtflut.rb.application.extensions.ServiceContextInitializer;
import de.lichtflut.rb.application.pages.AbstractBasePage;
import de.lichtflut.rb.core.security.AuthModule;
import de.lichtflut.rb.core.security.AuthenticationService;
import de.lichtflut.rb.core.security.LoginData;
import de.lichtflut.rb.core.security.RBUser;
import de.lichtflut.rb.webck.common.CookieAccess;
import de.lichtflut.rb.webck.common.RBWebSession;
import de.lichtflut.rb.webck.components.login.LoginPanel;
import de.lichtflut.rb.webck.models.infra.VersionInfoModel;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.arastreju.sge.security.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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
public class LoginPage extends AbstractBasePage {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);

	@SpringBean
	private AuthModule authModule;

	@SpringBean
	private AuthenticationService authService;

	// ----------------------------------------------------

	/**
	 * Constructor.
	 */
	public LoginPage() {

		checkCookies();
		redirectIfAlreadyLoggedIn();

		add(new LoginPanel("loginPanel") {
            @Override
            public void onLogin(LoginData loginData) {
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

	@Override
	protected void onConfigure() {
		super.onConfigure();
        checkCookies();
        redirectIfAlreadyLoggedIn();
	}

	@Override
	protected boolean needsAuthentication() {
		return false;
	}
	
	// ----------------------------------------------------

	/**
	 * Try to log the user in.
	 * 
	 * @param loginData The login data.
	 */
	private void tryLogin(final LoginData loginData) {
		try {
			final RBUser user = authService.login(loginData);
			final Set<String> permissions = authModule.getUserManagement().getUserPermissions(user, user.getDomesticDomain());
			if (!permissions.contains(RBPermission.LOGIN.name())) {
				LOGGER.info("Login aborted - User {} is lack of permission: {}", user.getName(), RBPermission.LOGIN.name());
				error(getString("error.login.activation"));
			} else {
				LOGGER.info("User {} logged in using username and password.", user.getName());
				RBWebSession.get().replaceSession();
				initializeUserSession(user);
			}
			if (loginData.getStayLoggedIn()) {
				final String token = authService.createRememberMeToken(user, loginData);
                CookieAccess.getInstance().setRememberMeToken(token);
				LOGGER.info("Added 'remember-me' cookie for {}", user.getName());
			}
		} catch (LoginException e) {
			error(getString("error.login.failed"));
		}
	}

    private void checkCookies() {
        final RBUser user = getUserFromCookies();
        if (user != null) {
            final Set<String> permissions = authModule.getUserManagement().getUserPermissions(user, user.getDomesticDomain());
            if (permissions.contains(RBPermission.LOGIN.name())) {
                initializeUserSession(user);
            } else {
                LOGGER.info("Login aborted - User {} is lack of permission: {}", user.getName(), RBPermission.LOGIN.name());
                error(getString("error.login.activation"));
            }
        }
	}

    private RBUser getUserFromCookies() {
        final String sessionToken = CookieAccess.getInstance().getSessionToken();
        final String rememberMeToken = CookieAccess.getInstance().getRememberMeToken();
        if (sessionToken != null) {
            LOGGER.info("User has session token: {}", sessionToken);
            final RBUser user = authService.loginByToken(sessionToken);
            if (user != null) {
                return user;
            }
        } else if (rememberMeToken != null) {
            LOGGER.info("User has remember me token: {}", rememberMeToken);
            return authService.loginByToken(rememberMeToken);
        }
        return null;
    }
	
	// ----------------------------------------------------

	private void addVersionInfo() {
		final VersionInfoModel model = new VersionInfoModel();
		add(new Label("version", new PropertyModel<String>(model, "version")));
		add(new Label("build", new PropertyModel<String>(model, "buildTimestamp")));
	}

	private void redirectIfAlreadyLoggedIn() {
		if (isAuthenticated()) {
			if (!continueToOriginalDestination()) {
				throw new RestartResponseException(WelcomePage.class);
			}
		}
	}

	private void initializeUserSession(RBUser user) {
        String token = authService.createSessionToken(user);
        new ServiceContextInitializer().init(user, user.getDomesticDomain());

        WebRequest request = (WebRequest) RequestCycle.get().getRequest();
        HttpServletRequest httpRequest = (HttpServletRequest) request.getContainerRequest();
        httpRequest.getSession().setAttribute(AuthModule.COOKIE_SESSION_AUTH, token);
	}

}

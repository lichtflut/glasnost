/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is;

import de.lichtflut.glasnost.is.model.logic.DevOpsItem;
import de.lichtflut.glasnost.is.pages.DevOpsPage;
import de.lichtflut.rb.core.viewspec.MenuItem;
import de.lichtflut.rb.webck.components.navigation.NavigationNode;
import org.apache.wicket.Page;

import de.lichtflut.glasnost.is.conf.GlasnostLayout;
import de.lichtflut.glasnost.is.conf.GlasnostStyle;
import de.lichtflut.glasnost.is.pages.LoginPage;
import de.lichtflut.glasnost.is.pages.WelcomePage;
import de.lichtflut.rb.application.RBApplication;
import de.lichtflut.rb.application.admin.AdminBasePage;
import de.lichtflut.rb.application.admin.identitymanagment.IdentityManagementPage;
import de.lichtflut.rb.application.admin.infomgmt.InformationManagementPage;
import de.lichtflut.rb.application.admin.typesystem.TypeSystemPage;
import de.lichtflut.rb.application.base.LogoutPage;
import de.lichtflut.rb.application.base.errorpages.DefaultErrorPage;
import de.lichtflut.rb.application.base.errorpages.ExpiredErrorPage;
import de.lichtflut.rb.application.graphvis.FlowChartInfoVisPage;
import de.lichtflut.rb.application.graphvis.HierarchyInfoVisPage;
import de.lichtflut.rb.application.graphvis.PeripheryViewPage;
import de.lichtflut.rb.application.layout.Layout;
import de.lichtflut.rb.application.resourceview.EntityDetailPage;
import de.lichtflut.rb.application.styles.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Application class for Glasnost Information Server.
 * </p>
 *
 * <p>
 * 	Created May 30, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class GlasnostWebApplication extends RBApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return WelcomePage.class;
	}

	@Override
	public Class<? extends Page> getLoginPage() {
		return LoginPage.class;
	}

	@Override
	public Layout getLayout() {
		return new GlasnostLayout();
	}

	@Override
	public Style getStyle() {
		return new GlasnostStyle();
	}

	// ----------------------------------------------------

	@Override
	public void init() {
		super.init();

		getRequestCycleListeners().add(new GlasnostRequestCycleListener());

		getApplicationSettings().setPageExpiredErrorPage(ExpiredErrorPage.class);
		getApplicationSettings().setAccessDeniedPage(DefaultErrorPage.class);
		getApplicationSettings().setInternalErrorPage(DefaultErrorPage.class);

		// Front end Area
		mountPage("welcome", WelcomePage.class);
		mountPage("profile", getUserProfilePage());
		mountPage("perspective", getPerspectivePage());
		mountPage("entity", EntityDetailPage.class);
		mountPage("browse", getBrowseAndSearchPage());
		mountPage("treeview", HierarchyInfoVisPage.class);
		mountPage("periphery", PeripheryViewPage.class);
		mountPage("flowchart", FlowChartInfoVisPage.class);

		// Admin Area
		mountPage("admin-area", AdminBasePage.class);
		mountPage("type-system", TypeSystemPage.class);
		mountPage("info-management", InformationManagementPage.class);
		mountPage("identity-management", IdentityManagementPage.class);

		// Special pages
		mountPage("login", LoginPage.class);
		mountPage("logout", LogoutPage.class);

        // Glasnost specific
        mountPage("devops", DevOpsPage.class);

	}

    @Override
    public List<NavigationNode> getFirstLevelNavigation(List<MenuItem> menuItems) {
        final List<NavigationNode> nodes = new ArrayList<NavigationNode>();
        nodes.add(createPageNode(WelcomePage.class, "navigation.welcome-page"));
        nodes.add(createPageNode(DevOpsPage.class, "navigation.devops-page"));
        nodes.addAll(super.getFirstLevelNavigation(menuItems));
        return nodes;
    }
}

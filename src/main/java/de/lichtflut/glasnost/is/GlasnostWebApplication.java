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
package de.lichtflut.glasnost.is;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;

import de.lichtflut.glasnost.is.conf.GlasnostLayout;
import de.lichtflut.glasnost.is.conf.GlasnostStyle;
import de.lichtflut.glasnost.is.pages.DevOpsItemPage;
import de.lichtflut.glasnost.is.pages.DevOpsPage;
import de.lichtflut.glasnost.is.pages.FindClassInEntityPage;
import de.lichtflut.glasnost.is.pages.LoginPage;
import de.lichtflut.glasnost.is.pages.SoftwareCatalogPage;
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
import de.lichtflut.rb.core.viewspec.MenuItem;
import de.lichtflut.rb.webck.components.navigation.NavigationNode;

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
	public Class<? extends Page> getEntityDetailPage() {
		return DevOpsItemPage.class;
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
		mountPage("item", DevOpsItemPage.class);
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
	public List<NavigationNode> getFirstLevelNavigation(final List<MenuItem> menuItems) {
		final List<NavigationNode> nodes = new ArrayList<NavigationNode>();
		nodes.add(createPageNode(WelcomePage.class, "navigation.welcome-page"));
		nodes.add(createPageNode(DevOpsPage.class, "navigation.devops-page"));
		nodes.add(createPageNode(SoftwareCatalogPage.class, "navigation.catalog"));
		//nodes.add(createPageNode(FindClassInEntityPage.class, "navigation.find-class-in-entity"));
		nodes.addAll(super.getFirstLevelNavigation(menuItems));
		return nodes;
	}
}

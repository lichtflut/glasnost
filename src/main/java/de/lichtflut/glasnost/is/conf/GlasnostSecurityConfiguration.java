/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package de.lichtflut.glasnost.is.conf;

import de.lichtflut.rb.application.common.RBRole;
import de.lichtflut.rb.core.security.SecurityConfiguration;

/**
 * <p>
 *  Glasnost specific extension of security service.
 * </p>
 *
 * <p>
 * 	Created Jan 19, 2012
 * </p>
 *
 * @author Oliver Tigges
 */
public class GlasnostSecurityConfiguration implements SecurityConfiguration {

    public GlasnostSecurityConfiguration() {
    }

    // ----------------------------------------------------

    @Override
    public String[] getRolesOfDomainAdmin() {
        return new String[] {
                RBRole.ACTIVE_USER.name(),
                RBRole.IDENTITY_MANAGER.name(),
                RBRole.DOMAIN_ADMIN.name(),
        };
    }
}

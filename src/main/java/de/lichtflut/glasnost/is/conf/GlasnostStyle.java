package de.lichtflut.glasnost.is.conf;

import de.lichtflut.rb.application.styles.frugal.FrugalStyle;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * <p>
 *  Glasnost style.
 * </p>
 *
 * <p>
 *  Created 13.07.12
 * </p>
 *
 * @author Oliver Tigges
 */
public class GlasnostStyle extends FrugalStyle {

    @Override
    public void addStyle(IHeaderResponse response) {
        super.addStyle(response);
        response.renderCSSReference("css/glasnost-style-1.0.css");
    }
}

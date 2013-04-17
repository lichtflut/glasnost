package de.lichtflut.glasnost.is.conf;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

import de.lichtflut.rb.application.styles.frugal.FrugalStyle;

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
	public void addStyle(final IHeaderResponse response) {
		super.addStyle(response);
		response.render(CssHeaderItem.forUrl("css/glasnost-style-1.0.css"));
	}
}

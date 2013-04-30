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

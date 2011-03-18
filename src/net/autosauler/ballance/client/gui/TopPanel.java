/*******************************************************************************
 * Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package net.autosauler.ballance.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;

/**
 * The Class TopPanel.
 * 
 * @author alexript
 */
public class TopPanel extends Composite {

	/** The panel. */
	private final HorizontalPanel panel;

	/**
	 * Instantiates a new top panel.
	 */
	public TopPanel() {
		panel = new HorizontalPanel();
		panel.setSpacing(3);
		panel.setWidth("100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		// add today currency values

		panel.add(new CurrencyTopPanel());

		// add language flags
		InlineHTML locales = new InlineHTML(
				"&nbsp;&nbsp;<a href=\"index.html?locale=ru\"><img src=\"flags/ru.gif\"/></a>&nbsp;<a href=\"index.html?locale=en\"><img src=\"flags/gb.gif\"/></a>&nbsp;");
		locales.setWidth("40px");
		panel.add(locales);
		panel.setCellWidth(locales, "50px");

		initWidget(panel);
		this.setStyleName("topPanel");
	}
}

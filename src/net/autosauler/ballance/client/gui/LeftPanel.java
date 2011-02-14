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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class LeftPanel.
 */
public class LeftPanel extends Composite {

	/** The panel. */
	private final VerticalPanel panel = new VerticalPanel();

	/** The auth form panel. */
	public static AuthPanel authPanel = null;

	/** The menu. */
	private LeftMenu menu = null;

	/**
	 * Instantiates a new left panel.
	 */
	public LeftPanel() {
		if (authPanel == null) {
			authPanel = new AuthPanel("BalAS");
			panel.add(authPanel);
			panel.setCellHeight(authPanel, "130px");
		}

		menu = new LeftMenu();
		authPanel.setMenu(menu);
		panel.add(menu);
		panel.setCellHorizontalAlignment(menu,
				HasHorizontalAlignment.ALIGN_CENTER);

		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		initWidget(panel);
		this.setStyleName("leftPanel");

	}
}

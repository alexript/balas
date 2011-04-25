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

import net.autosauler.ballance.client.gui.images.MenuImages;
import net.autosauler.ballance.client.gui.messages.CommMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * The Class CommInfoPanel.
 */
public class CommInfoPanel extends PopupPanel {

	/** The l. */
	private static final CommMessages l = GWT.create(CommMessages.class);
	private static final MenuImages images = GWT.create(MenuImages.class);

	/**
	 * Instantiates a new comm info panel.
	 */
	public CommInfoPanel() {
		super(false);
		Image i = new Image(images.icoRefresh());
		Label msg = new Label(l.commInProgress());

		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("200px");
		panel.setHeight("32px");
		panel.add(i);
		panel.setCellWidth(i, "32px");
		panel.setSpacing(2);
		panel.add(msg);

		setWidget(panel);

		// int width = RootPanel.get().getOffsetWidth();
		// int height = RootPanel.get().getOffsetHeight();

		int width = Window.getClientWidth() - 20;
		int height = Window.getClientHeight() * 3 / 4;

		int x = 250;
		int y = 100;
		if (width > 0) {
			x = width - 320;
		}
		if (height > 0) {
			y = height;
		}
		setPopupPosition(x, y);
	}
}

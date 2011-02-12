/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client.gui;

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * The Class CommInfoPanel.
 */
public class CommInfoPanel extends PopupPanel {

	/** The l. */
	private CommMessages l = null;

	/**
	 * Instantiates a new comm info panel.
	 */
	public CommInfoPanel() {
		super(false);
		l = GWT.create(CommMessages.class);
		Label msg = new Label(l.commInProgress());
		setWidget(msg);
		this.setPopupPosition(
				Ballance_autosauler_net.mainpanel.getOffsetWidth()
						- this.getOffsetWidth() - 200,
				Ballance_autosauler_net.mainpanel.getOffsetHeight()
						- this.getOffsetHeight() - 200);
	}
}

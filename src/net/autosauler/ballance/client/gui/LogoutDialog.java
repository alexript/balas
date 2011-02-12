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

import java.util.Date;

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * The Class LogoutDialog.
 */
public class LogoutDialog extends DialogBox {

	/** The localizator. */
	private AuthMessages l = null;

	/** The Constant ONE_HOUR. */
	private final static long ONE_HOUR = 1000 * 60 * 60;

	/**
	 * Instantiates a new logout dialog.
	 */
	public LogoutDialog() {
		l = GWT.create(AuthMessages.class);

		setText(l.qtnLogout());
		setAnimationEnabled(true);
		setGlassEnabled(true);

		Button yes = new Button(l.btnYes());

		yes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainPanel.setCommInfo(true);
				Ballance_autosauler_net.authService
						.logoff(new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								MainPanel.setCommInfo(false);
								LogoutDialog.this.hide();
							}

							@Override
							public void onSuccess(Void result) {
								Ballance_autosauler_net.setLoggedInState(false);
								Ballance_autosauler_net.sessionId.reset();

								Cookies.setCookie("session", "", new Date(
										System.currentTimeMillis() + ONE_HOUR));
								LeftPanel.authPanel.logoffAction();
								MainPanel.dropMainPane();
								History.newItem("start");
								MainPanel.setCommInfo(false);
								LogoutDialog.this.hide();
							}
						});

			}

		});

		Button no = new Button(l.btnNo());
		no.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				LogoutDialog.this.hide();

			}

		});

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(yes);
		buttons.add(no);
		buttons.setSpacing(5);

		setWidget(buttons);

		no.setFocus(true);

		setPopupPosition(
				(Ballance_autosauler_net.mainpanel.getOffsetWidth() / 2 - this
						.getOffsetWidth()),
				300);
	}
}

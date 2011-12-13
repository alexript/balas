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

import java.util.Date;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.SessionId;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class AuthPanel.
 */
public class AuthPanel extends SelectionListener<ButtonEvent> implements
		IDialogYesReceiver {

	/** The auth panel. */
	private final FormPanel authPanel = new FormPanel();

	/** The login text. */
	private TextField<String> loginText = null;

	/** The password text. */
	private TextField<String> passwordText = null;

	/** The ok button. */
	private Button okButton = null;

	/** The cancel button. */
	private Button cancelButton = null;

	/** The logout button. */
	private Button logoutButton = null;

	private final FormData formData;

	/**
	 * Instantiates a new auth panel.
	 * 
	 * @param title
	 *            the title
	 */
	public AuthPanel(String title, ContentPanel cp) {
		formData = new FormData("-20");
		authPanel.setHeading(title);
		authPanel.setFrame(true);
		authPanel.setWidth("240px");
		authPanel.setButtonAlign(HorizontalAlignment.CENTER);
		authPanel.setHeight("136px");
		authPanel.setCollapsible(true);
		//

		// Window.alert("check " + Ballance_autosauler_net.isLoggedIn());

		if (Ballance_autosauler_net.isLoggedIn()) {
			constructHelloPane();
		} else {
			constructAuthForm();
		}

		cp.add(authPanel);
	}

	@Override
	public void componentSelected(ButtonEvent event) {
		if (event.getSource().equals(okButton)) { // let's auth
			onOkButton();

		} else if (event.getSource().equals(cancelButton)) { // clean form

			authPanel.reset();

		} else if (event.getSource().equals(logoutButton)) {
			new QuestionDialog(M.auth.qtnLogout(), this, "logout").show();
		}

	}

	/**
	 * Construct auth form.
	 */
	private void constructAuthForm() {
		if (Ballance_autosauler_net.menu != null) {
			Ballance_autosauler_net.menu.buildContent();
		}
		authPanel.removeAll();
		authPanel.getButtonBar().removeAll();
		logoutButton = null;

		loginText = new TextField<String>();
		loginText.setFieldLabel(M.auth.labelLogin());
		loginText.setAllowBlank(false);
		loginText.getFocusSupport().setPreviousId(
				authPanel.getButtonBar().getId());
		loginText.setMinLength(5);
		loginText.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyPress(ComponentEvent event) {
				if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
					passwordText.focus();
				}
			}

		});
		authPanel.add(loginText, formData);

		passwordText = new TextField<String>();
		passwordText.setFieldLabel(M.auth.labelPssword());
		passwordText.setPassword(true);
		passwordText.setMinLength(5);
		passwordText.setAllowBlank(false);
		passwordText.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyPress(ComponentEvent event) {
				if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
					onOkButton();
				}
			}

		});
		authPanel.add(passwordText, formData);

		okButton = new Button(M.auth.btnLogin());
		okButton.addSelectionListener(this);
		authPanel.addButton(okButton);

		cancelButton = new Button(M.auth.btnCancel());
		cancelButton.addSelectionListener(this);
		authPanel.addButton(cancelButton);

	}

	/**
	 * Construct hello pane.
	 */
	private void constructHelloPane() {
		authPanel.removeAll();
		authPanel.getButtonBar().removeAll();
		loginText = null;
		passwordText = null;
		okButton = null;
		cancelButton = null;

		Label helloLabel = new Label();
		helloLabel.setText(M.auth
				.helloUserMsg(Ballance_autosauler_net.sessionId.getUsername()));
		helloLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		authPanel.add(helloLabel);

		UserRole userrole = Ballance_autosauler_net.sessionId.getUserrole();
		RolesWidget roleswidget = new RolesWidget(userrole);
		roleswidget.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		authPanel.add(roleswidget);

		logoutButton = new Button(M.auth.btnLogout());
		logoutButton.addSelectionListener(this);
		authPanel.addButton(logoutButton);

		if (Ballance_autosauler_net.menu != null) {
			Ballance_autosauler_net.menu.buildContent();
		}

	}

	/**
	 * Login action.
	 */
	public void loginAction() {
		constructHelloPane();
		authPanel.recalculate();
		authPanel.layout(true);
		authPanel.fireEvent(Events.Refresh);
		Info.display("Auth", M.auth
				.helloUserMsg(Ballance_autosauler_net.sessionId.getUsername()));
	}

	/**
	 * Logoff action.
	 */
	public void logoffAction() {
		constructAuthForm();
		authPanel.recalculate();
		authPanel.layout(true);
		authPanel.fireEvent(Events.Refresh);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String)
	 */
	@Override
	public void onDialogYesButtonClick(String tag, Object tag2) {
		if (tag.equals("logout")) {
			MainPanel.setCommInfo(true);
			Services.auth.logoff(new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					MainPanel.setCommInfo(false);
					new AlertDialog(caught).show();

				}

				@Override
				public void onSuccess(Void result) {
					Ballance_autosauler_net.logoutSequence();
					MainPanel.setCommInfo(false);
				}
			});

		}

	}

	/**
	 * On ok button.
	 */
	private void onOkButton() {
		String login = loginText.getValue().trim();
		String password = passwordText.getValue().trim();
		if (!authPanel.isValid()) {
			return;
		}

		MainPanel.setCommInfo(true);
		Services.auth.chkAuth(login, password, new AsyncCallback<SessionId>() {

			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				Info.display("Error", M.auth.commError());
				new AlertDialog(caught).show();
			}

			@Override
			public void onSuccess(SessionId result) {
				if (result != null) {
					Ballance_autosauler_net.setLoggedInState(true);

					Cookies.setCookie("session", result.getSessionId(),
							new Date(System.currentTimeMillis()
									+ Ballance_autosauler_net.COOKIE_TIME));
					Ballance_autosauler_net.sessionId.setSessionId(result
							.getSessionId());
					Ballance_autosauler_net.sessionId.setUsername(result
							.getUsername());
					Ballance_autosauler_net.sessionId.setUserrole(result
							.getUserrole());
					loginAction();

				} else {
					Ballance_autosauler_net.setLoggedInState(false);
					logoffAction();
					Info.display("Auth error", M.auth.badAuth());
				}// end else

				MainPanel.setCommInfo(false);
			}
		});

	}

}

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.SessionId;
import net.autosauler.ballance.client.databases.CurrencyValuesStorage;
import net.autosauler.ballance.client.databases.ICurrencyValuesReceiver;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class AuthPanel.
 */
public class AuthPanel extends Composite implements ClickHandler,
		KeyPressHandler, IDialogYesReceiver, ICurrencyValuesReceiver {

	/** The auth panel. */
	private final VerticalPanel authPanel = new VerticalPanel();

	/** The login text. */
	private TextBox loginText = null;

	/** The password text. */
	private PasswordTextBox passwordText = null;

	/** The ok button. */
	private Button okButton = null;

	/** The cancel button. */
	private Button cancelButton = null;

	/** The formname. */
	private String formname = null;

	/** The logout button. */
	private Button logoutButton = null;

	/** The l18n. */
	private AuthMessages l = null;

	/** The images. */
	private MenuImages images = null;

	/** The message label. */
	private Label messageLabel = null;

	/** The Constant errorfieldstyle. */
	final private static String errorfieldstyle = "errorFieldValue";

	private final HorizontalPanel currencypanel;
	private final HorizontalPanel currvalues;
	private final HorizontalPanel currprogress;

	/** The menu. */
	private LeftMenu menu;

	/**
	 * Instantiates a new auth panel.
	 * 
	 * @param title
	 *            the title
	 */
	public AuthPanel(String title) {
		l = GWT.create(AuthMessages.class);
		images = GWT.create(MenuImages.class);
		formname = title;
		authPanel.setWidth("244px");
		authPanel.setHeight("130px");
		authPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		messageLabel = new Label();
		messageLabel.setText("");
		messageLabel.setStyleName("authMessageLabel");

		// TODO: make currency panel as widget and place in some other place
		currencypanel = new HorizontalPanel();
		currvalues = new HorizontalPanel();
		currprogress = new HorizontalPanel();

		currvalues.setSpacing(3);
		Image progress = new Image(images.progress());
		currprogress.add(progress);
		currencypanel.add(currvalues);
		currencypanel.add(currprogress);
		// effectFade(currvalues.getElement());

		// Window.alert("check " + Ballance_autosauler_net.isLoggedIn());

		if (Ballance_autosauler_net.isLoggedIn()) {
			constructHelloPane();
		} else {
			constructAuthForm();
		}

		initWidget(authPanel);
		this.setStyleName("authPanel");

	}

	/**
	 * Construct auth form.
	 */
	private void constructAuthForm() {
		if (menu != null) {
			menu.buildContent();
		}
		authPanel.clear();
		logoutButton = null;

		if ((formname != null) && !formname.isEmpty()) {
			Label title = new Label();
			title.setText(formname);
			title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			authPanel.add(title);
		}

		// login line
		HorizontalPanel loginPanel = new HorizontalPanel();
		loginPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		loginPanel.setSpacing(3);

		Label loginLabel = new Label();
		loginLabel.setText(l.labelLogin());
		loginLabel.setWidth("70px");
		loginPanel.add(loginLabel);

		loginText = new TextBox();
		passwordText = new PasswordTextBox();

		loginText.setText("");
		loginText.setWidth("150px");
		loginText.setMaxLength(50);
		loginText.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					passwordText.setFocus(true);
				}

			}

		});
		loginPanel.add(loginText);
		authPanel.add(loginPanel);

		// password line
		HorizontalPanel passwordPanel = new HorizontalPanel();
		passwordPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		passwordPanel.setSpacing(3);

		Label passwordLabel = new Label();
		passwordLabel.setText(l.labelPssword());
		passwordLabel.setWidth("70px");
		passwordPanel.add(passwordLabel);

		passwordText.setText("");
		passwordText.setWidth("150px");
		passwordText.setMaxLength(50);
		passwordText.addKeyPressHandler(this);
		passwordPanel.add(passwordText);

		authPanel.add(passwordPanel);

		// buttons line

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setSpacing(3);

		okButton = new Button();
		okButton.setText(l.btnLogin());
		okButton.addClickHandler(this);
		buttonsPanel.add(okButton);

		cancelButton = new Button();
		cancelButton.setText(l.btnCancel());
		cancelButton.addClickHandler(this);
		buttonsPanel.add(cancelButton);

		authPanel.add(buttonsPanel);

		constructMessageLabel();

	}

	/**
	 * Construct hello pane.
	 */
	private void constructHelloPane() {
		authPanel.clear();
		loginText = null;
		passwordText = null;
		okButton = null;
		cancelButton = null;

		Label helloLabel = new Label();
		helloLabel.setText(l.helloUserMsg(Ballance_autosauler_net.sessionId
				.getUsername()));
		helloLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		authPanel.add(helloLabel);

		UserRole userrole = Ballance_autosauler_net.sessionId.getUserrole();
		StringBuilder sb = new StringBuilder();
		sb.append('|');
		if (userrole.isAdmin()) {
			sb.append("Admin");
			sb.append('|');
		}
		if (userrole.isDocuments()) {
			sb.append("Documents");
			sb.append('|');
		}
		if (userrole.isFinances()) {
			sb.append("Finances");
			sb.append('|');
		}
		if (userrole.isManager()) {
			sb.append("Manager");
			sb.append('|');
		}
		String rolestext = "Guest";
		if (sb.length() > 0) {
			rolestext = sb.toString();
		}
		Label rolesLabel = new Label(rolestext);
		rolesLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rolesLabel.setTitle(l.titleRoles());
		authPanel.add(rolesLabel);

		logoutButton = new Button();
		logoutButton.setText(l.btnLogout());
		logoutButton.addClickHandler(this);
		authPanel.add(logoutButton);

		constructMessageLabel();

		if (menu != null) {
			menu.buildContent();
		}

	}

	/**
	 * Construct message label.
	 */
	private void constructMessageLabel() {
		HorizontalPanel bottomPanel = new HorizontalPanel();

		messageLabel.setText("");
		bottomPanel.add(messageLabel);

		InlineHTML locales = new InlineHTML(
				"&nbsp;&nbsp;<a href=\"index.html?locale=ru\"><img src=\"flags/ru.gif\"/></a>&nbsp;<a href=\"index.html?locale=en\"><img src=\"flags/gb.gif\"/></a>&nbsp;");
		locales.setWidth("40px");
		bottomPanel.add(locales);

		authPanel.add(bottomPanel);

		authPanel.add(currencypanel);

		startCurrencyReload();

	}

	/**
	 * Creates the currency reload image.
	 * 
	 * @return the image
	 */
	private Image createCurrencyReloadImage() {
		Image image = new Image(images.reload());
		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				startCurrencyReload();

			}
		});
		return image;
	}

	/**
	 * Creates the currency value label.
	 * 
	 * @param value
	 *            the value
	 * @return the label
	 */
	private Label createCurrencyValueLabel(Double value) {
		Label label = new Label(value.toString());
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.databases.ICurrencyValuesReceiver#
	 * doCurrencyValue(java.lang.String, java.util.Date, java.lang.Double)
	 */
	@Override
	public void doCurrencyValue(String mnemo, Date date, Double value) {

		currvalues.clear();
		currvalues.add(new Label(mnemo + ":"));
		currvalues.add(createCurrencyValueLabel(value));
		currvalues.add(createCurrencyReloadImage());
		effectFade(currprogress.getElement());
		effectAppear(currvalues.getElement());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.databases.ICurrencyValuesReceiver#
	 * doCurrencyValues(java.util.Date, java.util.HashMap)
	 */
	@Override
	public void doCurrencyValues(Date date, HashMap<String, Double> values) {

		currvalues.clear();
		Set<String> keys = values.keySet();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			String mnemo = i.next();
			currvalues.add(new Label(mnemo + ":"));
			currvalues.add(createCurrencyValueLabel(values.get(mnemo)));
		}
		currvalues.add(createCurrencyReloadImage());
		effectFade(currprogress.getElement());
		effectAppear(currvalues.getElement());

	}

	/**
	 * Effect appear.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectAppear(Element element) /*-{
		new $wnd.Effect.Appear(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Effect fade.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectFade(Element element) /*-{
		new $wnd.Effect.Fade(element, {

			queue : 'end'
		});
	}-*/;

	/**
	 * Gets the menu.
	 * 
	 * @return the menu
	 */
	public LeftMenu getMenu() {
		return menu;
	}

	/**
	 * Login action.
	 */
	public void loginAction() {
		constructHelloPane();
	}

	/**
	 * Logoff action.
	 */
	public void logoffAction() {
		constructAuthForm();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(okButton)) { // let's auth
			onOkButton();

		} else if (event.getSource().equals(cancelButton)) { // clean form

			if (passwordText != null) {
				passwordText.setText("");
				passwordText.removeStyleName(errorfieldstyle);
			}
			if (loginText != null) {
				loginText.setText("");
				loginText.setFocus(true);
				loginText.removeStyleName(errorfieldstyle);
			}
			messageLabel.setText("");
		} else if (event.getSource().equals(logoutButton)) {
			new QuestionDialog(l.qtnLogout(), this, "logout").show();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String)
	 */
	@Override
	public void onDialogYesButtonClick(String tag) {
		if (tag.equals("logout")) {
			MainPanel.setCommInfo(true);
			Ballance_autosauler_net.authService
					.logoff(new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							MainPanel.setCommInfo(false);
							new AlertDialog(l.commError(), caught.getMessage())
									.show();

						}

						@Override
						public void onSuccess(Void result) {
							Ballance_autosauler_net.logoutSequence();
							MainPanel.setCommInfo(false);
						}
					});

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.KeyPressHandler#onKeyPress(com.google
	 * .gwt.event.dom.client.KeyPressEvent)
	 */
	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getSource().equals(passwordText)) {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				onOkButton();
			}
		}

	}

	/**
	 * On ok button.
	 */
	private void onOkButton() {
		String login = loginText.getText().trim();
		String password = passwordText.getText().trim();
		boolean fieldsok = true;
		if ((login == null) || (login.length() < 5)) {
			loginText.addStyleName(errorfieldstyle);
			fieldsok = false;
		} else {
			loginText.removeStyleName(errorfieldstyle);
		}

		if ((password == null) || (password.length() < 5)) {
			passwordText.addStyleName(errorfieldstyle);
			fieldsok = false;
		} else {
			passwordText.removeStyleName(errorfieldstyle);
		}

		if (!fieldsok) {
			messageLabel.setText(l.badFieldValue());
			loginText.setFocus(true);
			return;
		}

		messageLabel.setText("");
		MainPanel.setCommInfo(true);
		Ballance_autosauler_net.authService.chkAuth(login, password,
				new AsyncCallback<SessionId>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						messageLabel.setText(l.commError());
						new AlertDialog(l.commError(), caught.getMessage())
								.show();
					}

					@Override
					public void onSuccess(SessionId result) {
						if (result != null) {
							Ballance_autosauler_net.setLoggedInState(true);

							Cookies.setCookie(
									"session",
									result.getSessionId(),
									new Date(
											System.currentTimeMillis()
													+ Ballance_autosauler_net.COOKIE_TIME));
							Ballance_autosauler_net.sessionId
									.setSessionId(result.getSessionId());
							Ballance_autosauler_net.sessionId
									.setUsername(result.getUsername());
							Ballance_autosauler_net.sessionId
									.setUserrole(result.getUserrole());
							loginAction();

						} else {
							Ballance_autosauler_net.setLoggedInState(false);
							logoffAction();
							messageLabel.setText(l.badAuth());
						}// end else

						MainPanel.setCommInfo(false);
					}
				});

	}

	/**
	 * Sets the menu.
	 * 
	 * @param menu
	 *            the menu to set
	 */
	public void setMenu(LeftMenu menu) {
		this.menu = menu;
	}

	/**
	 * Start currency reload.
	 */
	private void startCurrencyReload() {
		effectFade(currvalues.getElement());
		effectAppear(currprogress.getElement());
		currvalues.clear();

		CurrencyValuesStorage.clean();

		Set<String> set = new HashSet<String>();
		// TODO: configurable
		set.add("EUR");
		set.add("USD");
		CurrencyValuesStorage.get(this, set);
	}

}

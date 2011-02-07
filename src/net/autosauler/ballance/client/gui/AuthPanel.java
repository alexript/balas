package net.autosauler.ballance.client.gui;

import java.util.Date;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.SessionId;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class AuthPanel.
 */
public class AuthPanel extends Composite implements ClickHandler,
		KeyPressHandler {

	/** The auth panel. */
	private VerticalPanel authPanel = new VerticalPanel();

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

	/** The message label. */
	private Label messageLabel = null;

	/** The Constant errorfieldstyle. */
	final private static String errorfieldstyle = "errorFieldValue";

	/** The Constant ONE_HOUR. */
	private final static long ONE_HOUR = 1000 * 60 * 60;

	/**
	 * Instantiates a new auth panel.
	 * 
	 * @param title
	 *            the title
	 */
	public AuthPanel(String title) {
		l = GWT.create(AuthMessages.class);
		formname = title;
		authPanel.setWidth("244px");
		authPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		messageLabel = new Label();
		messageLabel.setText("");
		messageLabel.setStyleName("authMessageLabel");

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
	 * Logoff action.
	 */
	public void logoffAction() {
		constructAuthForm();
	}

	/**
	 * Login action.
	 */
	public void loginAction() {
		constructHelloPane();
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
	}

	/**
	 * Construct auth form.
	 */
	private void constructAuthForm() {
		authPanel.clear();
		logoutButton = null;

		if (formname != null && !formname.isEmpty()) {
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
	 * On ok button.
	 */
	private void onOkButton() {
		String login = loginText.getText().trim();
		String password = passwordText.getText().trim();
		boolean fieldsok = true;
		if (login == null || login.length() < 5) {
			loginText.addStyleName(errorfieldstyle);
			fieldsok = false;
		} else {
			loginText.removeStyleName(errorfieldstyle);
		}

		if (password == null || password.length() < 5) {
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
					public void onSuccess(SessionId result) {
						if (result != null) {
							Ballance_autosauler_net.setLoggedInState(true);

							Cookies.setCookie("session", result.getSessionId(),
									new Date(System.currentTimeMillis()
											+ ONE_HOUR));
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

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						messageLabel.setText(l.commError());
					}
				});

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
			new LogoutDialog().show();
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

}

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

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.UsersService;
import net.autosauler.ballance.client.UsersServiceAsync;
import net.autosauler.ballance.shared.User;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class EditUserDialog.
 * 
 * @author alexript
 */
public class EditUserDialog extends DialogBox {

	/** The receiver. */
	private final IDialogYesReceiver receiver;

	/** The l. */
	private UsersMessages l;

	/** The editlogin. */
	private String editlogin;

	/** The user. */
	private User user;

	/** The name. */
	private TextBox name;

	/** The login. */
	private TextBox login;

	/** The password. */
	private TextBox password;

	/** The isactive. */
	private CheckBox isactive;

	/** The isadmin. */
	private CheckBox isadmin;

	/** The isdocuments. */
	private CheckBox isdocuments;

	/** The isfinances. */
	private CheckBox isfinances;

	/** The ismanager. */
	private CheckBox ismanager;

	/** The Constant errorfieldstyle. */
	final private static String errorfieldstyle = "errorFieldValue";

	/** The service. */
	private UsersServiceAsync service;

	/**
	 * Instantiates a new edits the user dialog.
	 * 
	 * @param r
	 *            the r
	 */
	public EditUserDialog(IDialogYesReceiver r) {
		receiver = r;
		init(null);
	}

	/**
	 * Instantiates a new edits the user dialog.
	 * 
	 * @param login
	 *            the login
	 * @param r
	 *            the r
	 */
	public EditUserDialog(String login, IDialogYesReceiver r) {
		receiver = r;
		init(login);
	}

	/**
	 * Creates the gui.
	 * 
	 */
	private void createGUI() {

		if (editlogin == null) {
			setText(l.menuAddUser());
		} else {
			setText(l.titleEditUser());
		}
		setAnimationEnabled(true);
		setGlassEnabled(true);

		String addText = l.btnAdd();
		if (editlogin != null) {
			addText = l.btnUpdate();
		}
		Button btnAdd = new Button(addText);

		btnAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (editlogin == null) {
					createUser();
				} else {
					updateUser();
				}
			}

		});

		Button btnCancel = new Button(l.btnCancel());
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EditUserDialog.this.hide();

			}

		});

		VerticalPanel vpanel = new VerticalPanel();
		// vpanel.setWidth("400px");

		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);

		layout.setHTML(0, 0, l.fullName());
		name = new TextBox();
		name.setText(user.getUsername());
		layout.setWidget(0, 1, name);

		layout.setHTML(1, 0, l.login());
		login = new TextBox();
		login.setText(user.getLogin());
		if (editlogin != null) {
			login.setReadOnly(true);
		}
		layout.setWidget(1, 1, login);

		layout.setHTML(2, 0, l.password());
		password = new TextBox();
		password.setText("");
		layout.setWidget(2, 1, password);

		layout.setHTML(3, 0, l.isactive());
		isactive = new CheckBox();
		isactive.setValue(user.isActive());
		layout.setWidget(3, 1, isactive);

		layout.setHTML(4, 0, l.access());

		UserRole role = user.getUserrole();

		FlexTable access = new FlexTable();
		access.setCellSpacing(6);

		access.setHTML(0, 0, l.isadmin());
		isadmin = new CheckBox();
		isadmin.setValue(role.isAdmin());
		access.setWidget(0, 1, isadmin);

		access.setHTML(1, 0, l.isdocuments());
		isdocuments = new CheckBox();
		isdocuments.setValue(role.isDocuments());
		access.setWidget(1, 1, isdocuments);

		access.setHTML(2, 0, l.isfinances());
		isfinances = new CheckBox();
		isfinances.setValue(role.isFinances());
		access.setWidget(2, 1, isfinances);

		access.setHTML(3, 0, l.ismanager());
		ismanager = new CheckBox();
		ismanager.setValue(role.isManager());
		access.setWidget(3, 1, ismanager);

		layout.setWidget(4, 1, access);

		vpanel.add(layout);

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setWidth("100%");
		buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel bcontainer = new HorizontalPanel();
		bcontainer.add(btnAdd);
		bcontainer.add(btnCancel);
		bcontainer.setSpacing(5);
		buttons.add(bcontainer);

		vpanel.add(buttons);

		setWidget(vpanel);

		setPopupPosition(
				(Ballance_autosauler_net.mainpanel.getOffsetWidth() / 2 - 200),
				100);
		show();
	}

	/**
	 * Creates the user.
	 */
	private void createUser() {
		UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
		if (role.isAdmin()) {
			if (updateMembers()) {
				MainPanel.setCommInfo(true);
				service.createUser(user, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(Boolean result) {
						MainPanel.setCommInfo(false);

						if (result) {
							EditUserDialog.this.hide();
							receiver.onDialogYesButtonClick("reload", null);

						} else {
							new AlertDialog(l.logCreateError()).show();
						}
					}

				});

			}
		}
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	private void getUser() {
		user = null;
		if (editlogin == null) {
			user = new User();
			user.initAsDefault();
			createGUI();
		} else {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {
				MainPanel.setCommInfo(true);

				service.getUser(editlogin, new AsyncCallback<User>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(User result) {
						user = result;
						EditUserDialog.this.createGUI();

						MainPanel.setCommInfo(false);
					}

				});
			}
		}
	}

	/**
	 * Inits the.
	 * 
	 * @param login
	 *            the login
	 */
	private void init(String login) {
		editlogin = login;
		service = GWT.create(UsersService.class);
		l = GWT.create(UsersMessages.class);
		getUser();
	}

	/**
	 * Update members.
	 * 
	 * @return true, if successful
	 */
	private boolean updateMembers() {
		boolean cansync = true;
		name.removeStyleName(errorfieldstyle);
		if (editlogin != null) {
			login.removeStyleName(errorfieldstyle);
		}
		password.removeStyleName(errorfieldstyle);

		String sometext = name.getText().trim();
		if (sometext.isEmpty()) {
			cansync = false;
			name.addStyleName(errorfieldstyle);
		}

		user.setUsername(sometext);
		if (editlogin == null) {
			sometext = login.getText().trim();
			if (sometext.isEmpty()) {
				cansync = false;
				login.addStyleName(errorfieldstyle);
			}
			user.setLogin(sometext);
		}

		sometext = password.getText();
		if ((editlogin == null) && sometext.isEmpty()) {
			cansync = false;
			password.addStyleName(errorfieldstyle);
		}

		user.setPassword(sometext);
		user.setActive(isactive.getValue());
		UserRole role = new UserRole();
		if (isadmin.getValue()) {
			role.setAdmin();
		}

		if (isdocuments.getValue()) {
			role.setDocuments();
		}

		if (isfinances.getValue()) {
			role.setFinances();
		}

		if (ismanager.getValue()) {
			role.setManager();
		}
		user.setUserrole(role);

		return cansync;
	}

	/**
	 * Update user.
	 */
	private void updateUser() {
		if (updateMembers()) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {
				MainPanel.setCommInfo(true);
				service.updateUser(user, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(Boolean result) {
						MainPanel.setCommInfo(false);

						if (result) {
							EditUserDialog.this.hide();
							receiver.onDialogYesButtonClick("reload", null);

						} else {
							new AlertDialog(l.logCreateError()).show();
						}

					}
				});

			}

		}

	}
}

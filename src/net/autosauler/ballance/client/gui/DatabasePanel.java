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

import java.util.HashMap;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.DatabaseService;
import net.autosauler.ballance.client.DatabaseServiceAsync;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DatabasePanel.
 */
public class DatabasePanel extends Composite implements ClickHandler,
		IDialogYesReceiver, IPaneWithMenu {

	/**
	 * Gets the.
	 * 
	 * @return the database panel
	 */
	public static DatabasePanel get() {
		if (impl == null) {
			impl = new DatabasePanel();
		}
		return impl;
	}

	/** The root. */
	private VerticalPanel root = null;

	/** The l. */
	private final DatabaseMessages l;

	/** The images. */
	private final MenuImages images;

	/** The btn drop database. */
	private Button btnDropDatabase;

	/** The impl. */
	private static DatabasePanel impl = null;

	/** The settingspanel. */
	private final DecoratorPanel settingspanel;

	/** The service. */
	private final DatabaseServiceAsync service = GWT
			.create(DatabaseService.class);

	/**
	 * Instantiates a new database panel.
	 */
	private DatabasePanel() {
		l = GWT.create(DatabaseMessages.class);
		images = GWT.create(MenuImages.class);
		root = new VerticalPanel();
		root.setSpacing(5);
		root.add(createDropDatabasePanel());
		settingspanel = createSettingsPanel();
		root.add(settingspanel);
		initWidget(root);

		MainPanel.setCommInfo(true);
		service.getSettings(new AsyncCallback<HashMap<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				settingspanel.clear();
				Label err = new Label(caught.getMessage());
				err.setVisible(false);
				settingspanel.add(err);
				effectAppear(err.getElement());
				Log.error(caught.getMessage());
			}

			@Override
			public void onSuccess(HashMap<String, String> result) {
				MainPanel.setCommInfo(false);
				settingspanel.clear();
				Label err = new Label(result.toString());
				err.setVisible(false);
				settingspanel.add(err);
				effectAppear(err.getElement());
			}
		});
	}

	/**
	 * Creates the drop database panel.
	 * 
	 * @return the decorator panel
	 */
	private DecoratorPanel createDropDatabasePanel() {
		HorizontalPanel p = new HorizontalPanel();
		p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		p.setSpacing(6);
		p.add(new Label(l.msgDropDatabaseTitle()));

		btnDropDatabase = new Button(l.btnExecute());
		btnDropDatabase.addClickHandler(this);
		p.add(btnDropDatabase);
		DecoratorPanel panel = new DecoratorPanel();
		panel.setWidget(p);
		return panel;
	}

	/**
	 * Creates the settings panel.
	 * 
	 * @return the decorator panel
	 */
	private DecoratorPanel createSettingsPanel() {
		DecoratorPanel panel = new DecoratorPanel();
		Image spinner = new Image(images.spinner());
		panel.setWidget(spinner);
		return panel;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public Widget getPaneMenu() {
		return null;
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
		if (event.getSource().equals(btnDropDatabase)) {
			new QuestionDialog(l.qstDropDatabase(), this, "dropdb").show();
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
		if (tag.equals("dropdb")) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {
				MainPanel.setCommInfo(true);

				service.dropDatabase(new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						new AlertDialog("Database error", caught.getMessage())
								.show();
						MainPanel.setCommInfo(false);
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Ballance_autosauler_net.logoutSequence();
							MainPanel.setCommInfo(false);

						} else {
							MainPanel.setCommInfo(false);
							new AlertDialog("Database error!").show();
						}

					}

				});
			}
		}

	}
}

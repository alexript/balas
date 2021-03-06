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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class DatabasePanel.
 */
public class DatabasePanel extends ContentPanel implements ClickHandler,
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

	/** The btn drop database. */
	private Button btnDropDatabase;

	/** The btn dump database. */
	private Button btnDumpDatabase;

	/** The btn restore database. */
	private Button btnRestoreDatabase;

	/** The btn global script. */
	private Button btnGlobalScript;

	/** The impl. */
	private static DatabasePanel impl = null;

	/** The settingspanel. */
	private final DecoratorPanel settingspanel;

	/** The setings list. */
	private CellList<String> setingsList;

	/** The settingvalue. */
	private TextBox settingvalue;

	/** The settings. */
	private HashMap<String, String> settings = null;

	/** The dumpfile. */
	private TextBox dumpfile;

	/** The restorefile. */
	private TextBox restorefile;

	/**
	 * Instantiates a new database panel.
	 */
	private DatabasePanel() {
		super(new VBoxLayout());

		add(createGlobalScriptPanel());
		add(createDropDatabasePanel());
		add(createDumpDatabasePanel());
		settingspanel = createSettingsPanel();
		add(settingspanel);

		MainPanel.setCommInfo(true);
		Services.database
				.getSettings(new AsyncCallback<HashMap<String, String>>() {

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
						settings = result;
						settingspanel.clear();

						final Button btn = new Button();
						btn.setText(M.database.btnSoreChanges());
						btn.setEnabled(false);

						settingvalue = new TextBox();
						settingvalue.setWidth("200px");
						settingvalue.addChangeHandler(new ChangeHandler() {

							@Override
							public void onChange(ChangeEvent event) {
								btn.setEnabled(true);

							}
						});

						// create celllist
						// ============================================
						ProvidesKey<String> provider = new ProvidesKey<String>() {
							@Override
							public Object getKey(String item) {
								return item == null ? null : item;
							}
						};

						ListDataProvider<String> dataProvider = new ListDataProvider<String>();
						dataProvider.setList(new ArrayList<String>(settings
								.keySet()));

						setingsList = new CellList<String>(new TextCell() {
						}, provider);
						setingsList.setPageSize(30);
						setingsList
								.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
						setingsList
								.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

						// Add a selection model so we can select cells.
						final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>(
								provider);
						setingsList.setSelectionModel(selectionModel);
						selectionModel
								.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
									@Override
									public void onSelectionChange(
											SelectionChangeEvent event) {
										settingvalue.setText(settings
												.get(selectionModel
														.getSelectedObject()));
									}
								});

						dataProvider.addDataDisplay(setingsList);
						// celllist created
						// =======================================================

						btn.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								String name = selectionModel
										.getSelectedObject();
								String value = settingvalue.getText().trim();
								if (!value.isEmpty()) {
									HashMap<String, String> values = new HashMap<String, String>();
									values.put(name, value);
									MainPanel.setCommInfo(true);
									Services.database.setSettings(values,
											new AsyncCallback<Void>() {

												@Override
												public void onFailure(
														Throwable caught) {
													MainPanel
															.setCommInfo(false);
													new AlertDialog(caught)
															.show();
												}

												@Override
												public void onSuccess(
														Void result) {
													MainPanel
															.setCommInfo(false);
													btn.setEnabled(false);
												}
											});
								}

							}
						});

						HorizontalPanel panel = new HorizontalPanel();
						panel.setSpacing(4);
						panel.setWidth("500px");
						panel.setVisible(false);
						panel.add(setingsList);
						panel.setCellWidth(setingsList, "200px");
						panel.add(settingvalue);
						panel.add(btn);
						settingspanel.add(panel);
						effectAppear(panel.getElement());
					}
				});
	}

	/**
	 * Creates the drop database panel.
	 * 
	 * @return the decorator panel
	 */
	private FieldSet createDropDatabasePanel() {
		FieldSet fieldSet = new FieldSet();
		fieldSet.setHeading(M.database.msgDropDatabaseTitle());

		btnDropDatabase = new Button(M.database.btnExecute());
		btnDropDatabase.addClickHandler(this);

		fieldSet.add(btnDropDatabase);
		return fieldSet;
	}

	/**
	 * Creates the dump database panel.
	 * 
	 * @return the decorator panel
	 */
	private FieldSet createDumpDatabasePanel() {
		VerticalPanel v = new VerticalPanel();

		HorizontalPanel p = new HorizontalPanel();
		p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		p.setSpacing(6);
		p.add(new Label(M.database.msgDumpDatabaseTitle()));
		dumpfile = new TextBox(); // TODO: replace with list of old dump files
		p.add(dumpfile);
		btnDumpDatabase = new Button(M.database.btnExecute());
		btnDumpDatabase.addClickHandler(this);
		p.add(btnDumpDatabase);
		v.add(p);

		p = new HorizontalPanel();
		p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		p.setSpacing(6);
		p.add(new Label(M.database.msgRestoreDatabaseTitle()));
		restorefile = new TextBox();
		p.add(restorefile);
		btnRestoreDatabase = new Button(M.database.btnExecute());
		btnRestoreDatabase.addClickHandler(this);
		p.add(btnRestoreDatabase);
		v.add(p);

		FieldSet fieldSet = new FieldSet();
		fieldSet.add(v);
		return fieldSet;
	}

	/**
	 * Creates the global script panel.
	 * 
	 * @return the decorator panel
	 */
	private FieldSet createGlobalScriptPanel() {
		FieldSet fieldSet = new FieldSet();
		fieldSet.setHeading(M.database.msgGlobalScript());
		btnGlobalScript = new Button(M.database.btnGlobalScript());
		btnGlobalScript.addClickHandler(this);
		fieldSet.add(btnGlobalScript);
		return fieldSet;
	}

	/**
	 * Creates the settings panel.
	 * 
	 * @return the decorator panel
	 */
	private DecoratorPanel createSettingsPanel() {
		DecoratorPanel panel = new DecoratorPanel();
		Image spinner = new Image(Images.menu.spinner());
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

	@Override
	public List<MenuItem> getHelpItems() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public com.extjs.gxt.ui.client.widget.menu.MenuBar getPaneMenu() {
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
			new QuestionDialog(M.database.qstDropDatabase(), this, "dropdb")
					.show();
		} else if (event.getSource().equals(btnDumpDatabase)) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {
				MainPanel.setCommInfo(true);
				Services.database.dumpDatabase(dumpfile.getText().trim(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								MainPanel.setCommInfo(false);
								new AlertDialog(caught).show();

							}

							@Override
							public void onSuccess(Void result) {
								MainPanel.setCommInfo(false);

							}
						});
			}
		} else if (event.getSource().equals(btnRestoreDatabase)) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {
				MainPanel.setCommInfo(true);
				Services.database.restoreDatabase(restorefile.getText().trim(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								MainPanel.setCommInfo(false);
								new AlertDialog(caught).show();

							}

							@Override
							public void onSuccess(Void result) {
								MainPanel.setCommInfo(false);

							}
						});

			}
		} else if (event.getSource().equals(btnGlobalScript)) {
			new ScriptEditor("global", null);
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
	public void onDialogYesButtonClick(String tag, Object tag2) {
		if (tag.equals("dropdb")) {
			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();
			if (role.isAdmin()) {
				MainPanel.setCommInfo(true);

				Services.database.dropDatabase(new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						new AlertDialog(caught).show();
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

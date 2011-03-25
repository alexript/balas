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
import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.CatalogService;
import net.autosauler.ballance.client.CatalogServiceAsync;
import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class CatalogPanel.
 * 
 * @author alexript
 */
public abstract class CatalogPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The catalogname. */
	private final String catalogname;

	/** The service. */
	private static CatalogServiceAsync service = null;

	/** The images. */
	private static MenuImages images = null;

	/** The tabimage. */
	private final Image tabimage;

	/** The l. */
	private static CatalogMessages l = null;

	/** The progress. */
	private static Image progress = null;

	/** The root. */
	private AbsolutePanel root;

	/** The list. */
	private VerticalPanel list;

	/** The editor. */
	private VerticalPanel editor = null;

	/** The Constant DATEFORMATTER. */
	private static final String DATEFORMATTER = "yyyy/MM/dd HH:mm";

	/** The date formatter. */
	private static SimpleDateFormat formatter = null;

	/** The editformnumber. */
	private Long editformnumber;

	/**
	 * Instantiates a new catalog panel.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @param image
	 *            the image
	 */
	public CatalogPanel(final String catalogname, Image image) {
		this.catalogname = catalogname;
		editformnumber = -1L;
		tabimage = image;
		if (service == null) {
			service = GWT.create(CatalogService.class);
			images = GWT.create(MenuImages.class);
			l = GWT.create(CatalogMessages.class);
			progress = new Image(images.progress());
			formatter = new SimpleDateFormat(DATEFORMATTER);
		}

	}

	/**
	 * Builds the editor.
	 * 
	 * @param panel
	 *            the panel
	 */
	abstract void buildEditor(VerticalPanel panel);

	/**
	 * Builds the list row.
	 * 
	 * @param map
	 *            the map
	 * @return the widget
	 */
	abstract Widget buildListRow(HashMap<String, Object> map);

	/**
	 * Can create.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canCreate(UserRole role);

	/**
	 * Can edit.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canEdit(UserRole role);

	/**
	 * Can trash.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canTrash(UserRole role);

	/**
	 * Clean edit form.
	 */
	abstract void cleanEditForm();

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		editor = new VerticalPanel();
		editor.setVisible(false);
		editor.setSpacing(5);
		editor.add(new Label(l.titleEditor()));
		buildEditor(editor);

		HorizontalPanel buttons = new HorizontalPanel();
		Button b = new Button(l.btnSave());
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, Object> map = getEditorValues();

				if (map.size() > 0) {
					MainPanel.setCommInfo(true);
					if (editformnumber.equals(-1L)) {
						service.addRecord(catalogname, map,
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										MainPanel.setCommInfo(false);
										new AlertDialog(caught.getMessage())
												.show();

									}

									@Override
									public void onSuccess(Boolean result) {
										MainPanel.setCommInfo(false);
										if (result) {
											reloadList();
										} else {
											new AlertDialog(l.msgCreateError())
													.show();
										}

									}
								});
					} else {
						service.updateRecord(catalogname, editformnumber, map,
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										MainPanel.setCommInfo(false);
										new AlertDialog(caught.getMessage())
												.show();
									}

									@Override
									public void onSuccess(Boolean result) {
										MainPanel.setCommInfo(false);
										if (result) {
											reloadList();
										} else {
											new AlertDialog(l.msgUpdateError())
													.show();
										}

									}
								});
					}
					reloadList();
				}

			}
		});

		buttons.add(b);

		Button btnCancel = new Button(l.btnCancel());
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				reloadList();

			}
		});

		buttons.add(btnCancel);
		editor.add(buttons);
		root.add(editor, 0, 0);
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {
		list = new VerticalPanel();

		root.add(list, 0, 0);

	}

	/**
	 * Creates the list record row.
	 * 
	 * @param number
	 *            the number
	 * @return the horizontal panel
	 */
	private HorizontalPanel createListRecordRow(final Long number) {
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(5);
		panel.add(progress);

		MainPanel.setCommInfo(true);
		service.getRecord(catalogname, number,
				new AsyncCallback<HashMap<String, Object>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught.getMessage()).show();
					}

					@Override
					public void onSuccess(HashMap<String, Object> result) {
						MainPanel.setCommInfo(false);
						VerticalPanel vp = new VerticalPanel();
						Widget w = buildListRow(result);
						panel.clear();
						panel.add(new Label(number.toString()));
						vp.add(w);
						Date date = new Date((Long) result.get("createdate"));
						String day = formatter.format(date);
						vp.add(new Label(day + " " + l.labelAuthor() + ": "
								+ (String) result.get("author")));
						panel.add(vp);
						if (canEdit(Ballance_autosauler_net.sessionId
								.getUserrole())) {
							Button btnEdit = new Button(l.btnEdit());
							btnEdit.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									MainPanel.setCommInfo(true);
									service.getRecord(
											catalogname,
											number,
											new AsyncCallback<HashMap<String, Object>>() {

												@Override
												public void onFailure(
														Throwable caught) {
													MainPanel
															.setCommInfo(false);
													new AlertDialog(caught
															.getMessage())
															.show();
												}

												@Override
												public void onSuccess(
														HashMap<String, Object> result) {
													MainPanel
															.setCommInfo(false);
													editformnumber = number;
													fillEditorForm(result);
													openEditor();
												}
											});

								}
							});
							panel.add(btnEdit);
						}
						if (canTrash(Ballance_autosauler_net.sessionId
								.getUserrole())) {

							Button btnDelete = new Button(l.btnDelete());
							btnDelete.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									new QuestionDialog(l.qstDeleteRecord()
											+ " " + number, CatalogPanel.this,
											"trashrecord", number).show();

								}
							});
							panel.add(btnDelete);
						}
					}
				});

		return panel;
	}

	/**
	 * Effect hide.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectHide(Element element) /*-{
		new $wnd.Effect.DropOut(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Effect show.
	 * 
	 * @param element
	 *            the element
	 */
	private native void effectShow(Element element) /*-{
		new $wnd.Effect.SlideDown(element, {
			queue : 'end'
		});
	}-*/;

	/**
	 * Fill editor form.
	 * 
	 * @param map
	 *            the map
	 */
	abstract void fillEditorForm(HashMap<String, Object> map);

	/**
	 * Gets the editor values.
	 * 
	 * @return the editor values
	 */
	abstract HashMap<String, Object> getEditorValues();

	/**
	 * Gets the list form.
	 * 
	 * @return the list form
	 */
	public CatalogPanel getListForm() {
		root = new AbsolutePanel();
		root.setWidth("100%");
		root.setHeight("500px");

		createListForm();

		if (canEdit(Ballance_autosauler_net.sessionId.getUserrole())
				|| canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			createEditorForm();
		}
		initWidget(root);
		reloadList();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public Widget getPaneMenu() {
		MenuBar menu = new MenuBar();

		menu.addItem(l.menuReload(), new Command() { // reload users list
					@Override
					public void execute() {
						reloadList();
					}
				});

		if (canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			menu.addItem(l.menuCreate(), new Command() { // reload users list
						@Override
						public void execute() {
							editformnumber = -1L;
							cleanEditForm();
							openEditor();
						}
					});
		}
		return menu;

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
		if (tag.equals("trashrecord")) {
			MainPanel.setCommInfo(true);
			service.trashRecord(catalogname, (Long) tag2,
					new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							MainPanel.setCommInfo(false);
							new AlertDialog(caught.getMessage()).show();
						}

						@Override
						public void onSuccess(Boolean result) {
							MainPanel.setCommInfo(false);
							if (result) {
								reloadList();
							} else {
								new AlertDialog(l.msgTrashError()).show();
							}

						}
					});
		}

	}

	/**
	 * Creates the new record.
	 */
	private void openEditor() {

		effectHide(list.getElement());
		effectShow(editor.getElement());
	}

	/**
	 * Reload list.
	 */
	private void reloadList() {
		list.clear();
		list.add(progress);

		MainPanel.setCommInfo(true);
		service.getAllRecords(catalogname, new AsyncCallback<Set<Long>>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang
			 * .Throwable)
			 */
			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught.getMessage()).show();

			}

			@Override
			public void onSuccess(Set<Long> result) {
				list.clear();
				list.setSpacing(2);
				list.add(new Label(l.titleList()));
				MainPanel.setCommInfo(false);
				Iterator<Long> i = result.iterator();
				while (i.hasNext()) {
					Long number = i.next();
					list.add(createListRecordRow(number));
				}
				if (!list.isVisible() && (editor != null) && editor.isVisible()) {
					effectHide(editor.getElement());
					effectShow(list.getElement());
				}
			}
		});
	}

}

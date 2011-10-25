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
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class CatalogPanel.
 * 
 * @author alexript
 */
public class CatalogPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver, IReloadMsgReceiver, IFieldChangeHandler {

	/** The catalogname. */
	private final String catalogname;

	/** The listscroll. */
	private ScrollPanel listscroll;

	/** The tabimage. */
	private final Image tabimage;

	/** The root. */
	private AbsolutePanel root;

	/** The list. */
	private VerticalPanel list;

	/** The editor. */
	private VerticalPanel editor = null;

	/** The editformnumber. */
	private Long editformnumber;

	/** The fullname. */
	private HeaderField fullname;

	/** The linecounter. */
	private Long linecounter = 0L;

	/** The progress. */
	private final static Image progress = new Image(Images.menu.progress());

	/** The viewdata. */
	private HashMap<Long, String> viewdata = null;

	/** The fields. */
	private HashMap<String, HeaderField> fields;

	private final Description structuredescription;

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

		linecounter = 0L;

		structuredescription = StructureFactory.getDescription("catalog."
				+ this.catalogname);

		MainPanel.setCommInfo(true);
		Services.catalogs.getRecordsForView(catalogname,
				new AsyncCallback<HashMap<Long, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<Long, String> result) {
						MainPanel.setCommInfo(false);
						viewdata = result;
					}
				});

	}

	/**
	 * Adds the field.
	 * 
	 * @param name
	 *            the name
	 * @param field
	 *            the field
	 * @param type
	 *            the type
	 * @param defval
	 *            the defval
	 * @param helper
	 *            the helper
	 */
	protected void addField(String name, String field, int type, Object defval,
			Object helper) {
		HeaderField hf = DataTypeFactory.addField(name, field, type, defval,
				helper);
		String nn = Character.toUpperCase(field.charAt(0)) + field.substring(1);
		hf.setChangeHandler("On" + nn + "Change", this);
		fields.put(field, hf);
		editor.add(hf);
	}

	/**
	 * Builds the list row.
	 * 
	 * @param map
	 *            the map
	 * @return the widget
	 */
	protected Widget buildListRow(HashMap<String, Object> map) {
		return null;
	}

	/**
	 * Can create.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	protected boolean canCreate(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Can edit.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	protected boolean canEdit(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Can trash.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	protected boolean canTrash(UserRole role) {
		UserRole canrole = new UserRole(structuredescription.getRole());
		return canrole.hasAccess(role);
	}

	/**
	 * Clean edit form.
	 */
	private void cleanEditForm() {
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			HeaderField hf = fields.get(name);
			hf.reset();
		}
	}

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		editor = new VerticalPanel();
		editor.setVisible(false);
		editor.setSpacing(5);
		editor.add(new Label(M.catalog.titleEditor()));

		fullname = DataTypeFactory.addField(M.catalog.labelFullname(),
				"fullname", DataTypes.DT_STRING, "", null);
		editor.add(fullname);
		fields = new HashMap<String, HeaderField>();
		createStructure();

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(5);
		Button b = new Button(M.catalog.btnSave());
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				HashMap<String, Object> map = getEditorValues();
				if (map == null) {
					map = new HashMap<String, Object>();
				}

				String fname = ((String) fullname.getValue()).trim();
				if (fname.isEmpty()) {
					new AlertDialog(M.catalog.errEmptyFullname()).show();
				} else {
					map.put("fullname", fname);
					// Window.alert(map.toString());
					MainPanel.setCommInfo(true);
					if (editformnumber.equals(-1L)) {
						Services.catalogs.addRecord(catalogname, map,
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										MainPanel.setCommInfo(false);
										new AlertDialog(caught).show();

									}

									@Override
									public void onSuccess(Boolean result) {
										MainPanel.setCommInfo(false);
										if (result) {
											reloadList();
										} else {
											new AlertDialog(M.catalog
													.msgCreateError()).show();
										}

									}
								});
					} else {
						Services.catalogs.updateRecord(catalogname,
								editformnumber, map,
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										MainPanel.setCommInfo(false);
										new AlertDialog(caught).show();
									}

									@Override
									public void onSuccess(Boolean result) {
										MainPanel.setCommInfo(false);
										if (result) {
											reloadList();
										} else {
											new AlertDialog(M.catalog
													.msgUpdateError()).show();
										}

									}
								});
					}

				}

			}
		});

		buttons.add(b);

		Button btnCancel = new Button(M.catalog.btnCancel());
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
		listscroll = new ScrollPanel(list);
		listscroll.setSize("100%", "500px");

		root.add(listscroll, 0, 0);

	}

	/**
	 * Creates the list record row.
	 * 
	 * @param number
	 *            the number
	 * @return the horizontal panel
	 */
	private HorizontalPanel createListRecordRow(final Long number) {
		linecounter++;

		final HorizontalPanel panel = new HorizontalPanel();

		if ((linecounter % 2) == 1) {
			panel.addStyleName("EvenTableRow");
		} else {
			panel.addStyleName("OddTableRow");
		}

		panel.setSpacing(5);
		panel.add(progress);

		MainPanel.setCommInfo(true);
		Services.catalogs.getRecord(catalogname, number,
				new AsyncCallback<HashMap<String, Object>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(final HashMap<String, Object> result) {
						MainPanel.setCommInfo(false);
						VerticalPanel vp = new VerticalPanel();
						Widget w = buildListRow(result);
						panel.clear();
						panel.add(new Label(number.toString()));
						vp.add(new Label((String) result.get("fullname")));
						if (w != null) {
							vp.add(w);
						}
						Date date = new Date((Long) result.get("createdate"));
						String day = DataTypeFactory.formatter.format(date);
						vp.add(new Label(day + " " + M.catalog.labelAuthor()
								+ ": " + (String) result.get("username")));
						panel.add(vp);
						panel.setCellWidth(vp, "400px;");
						if (canEdit(Ballance_autosauler_net.sessionId
								.getUserrole())) {
							Button btnEdit = new Button(M.catalog.btnEdit());
							btnEdit.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									MainPanel.setCommInfo(true);
									Services.catalogs
											.getRecord(
													catalogname,
													number,
													new AsyncCallback<HashMap<String, Object>>() {

														@Override
														public void onFailure(
																Throwable caught) {
															MainPanel
																	.setCommInfo(false);
															new AlertDialog(
																	caught)
																	.show();
														}

														@Override
														public void onSuccess(
																HashMap<String, Object> result) {
															MainPanel
																	.setCommInfo(false);
															editformnumber = number;
															fullname.setValue(
																	result.get("fullname"),
																	true);
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

							Button btnDelete = new Button(M.catalog.btnDelete());
							btnDelete.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									new QuestionDialog(M.catalog
											.qstDeleteRecord()
											+ " "
											+ (String) result.get("fullname"),
											CatalogPanel.this, "trashrecord",
											number).show();

								}
							});
							panel.add(btnDelete);
						}
					}
				});

		return panel;
	}

	/**
	 * Creates the structure.
	 */
	private void createStructure() {
		if (structuredescription != null) {
			List<Field> fields = structuredescription.get();
			Iterator<Field> i = fields.iterator();
			while (i.hasNext()) {
				Field f = i.next();
				String helper = f.getHelper();
				String helpertype = f.getHelpertype();

				CatalogPanel h = null;
				if (helpertype.equals("catalog") && (helper != null)
						&& !helper.isEmpty()) {
					h = new CatalogPanel(helper, null);
				}

				addField(
						f.getName().getName(
								LocaleInfo.getCurrentLocale().getLocaleName()),
						f.getFieldname(), f.getType(), f.getDefval(), h);
			}
		}
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
	private void fillEditorForm(HashMap<String, Object> map) {
		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			if (map.containsKey(name)) {
				HeaderField hf = fields.get(name);
				hf.setValue(map.get(name), true);
			}
		}
	}

	/**
	 * Gets the catalogname.
	 * 
	 * @return the catalogname
	 */
	public String getCatalogname() {
		return catalogname;
	}

	/**
	 * Gets the editor values.
	 * 
	 * @return the editor values
	 */
	private HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			HeaderField hf = fields.get(name);
			map.put(name, hf.getValue());
		}
		return map;
	}

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

	/**
	 * Gets the name.
	 * 
	 * @param number
	 *            the number
	 * @return the name
	 */
	public String getName(Long number) {
		if (viewdata != null) {
			if (viewdata.containsKey(number)) {
				return viewdata.get(number);
			} else {
				return "UNCKNOWN. TRY RELOAD PAGE.";
			}
		} else {
			return "ERROR: NO DATA";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public MenuBar getPaneMenu() {
		MenuBar menu = new MenuBar();

		menu.addItem(M.catalog.menuReload(), new Command() { // reload users
																// list
					@Override
					public void execute() {
						reloadList();
					}
				});

		if (canCreate(Ballance_autosauler_net.sessionId.getUserrole())) {

			menu.addItem(M.catalog.menuCreate(), new Command() { // reload users
																	// list
						@Override
						public void execute() {
							editformnumber = -1L;
							fullname.reset();
							cleanEditForm();
							openEditor();
						}
					});
		}

		if (Ballance_autosauler_net.sessionId.getUserrole().isAdmin()) {

			menu.addItem(M.catalog.menuScript(), new Command() {
				@Override
				public void execute() {
					new ScriptEditor("catalog." + catalogname,
							CatalogPanel.this);
				}
			});
		}
		return menu;

	}

	/**
	 * Gets the select box.
	 * 
	 * @param selectednumber
	 *            the selectednumber
	 * @return the select box
	 */
	public CatalogSelector getSelectBox(Long selectednumber) {
		return new CatalogSelector(catalogname, selectednumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IFieldChangeHandler#handleFieldChange
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void handleFieldChange(String tag, String newvalue) {
		// String s = "Catalog " + catalogname + ". eval pair " + tag
		// + " with new value " + newvalue.toString();
		//

		HashMap<String, String> map = new HashMap<String, String>();

		Set<String> names = fields.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			HeaderField hf = fields.get(name);
			map.put(name, hf.getValueAsString());
		}
		String fname = ((String) fullname.getValue()).trim();
		map.put("fullname", fname);

		HashMap<String, Integer> types = new HashMap<String, Integer>();
		types.put("fullname", DataTypes.DT_STRING);
		Iterator<Field> j = structuredescription.get().iterator();
		while (j.hasNext()) {
			Field f = j.next();
			types.put(f.getFieldname(), f.getType());
		}

		// Window.alert(map.toString());

		MainPanel.setCommInfo(true);
		Services.scripts.eval("catalog." + catalogname, tag, map, types,
				new AsyncCallback<HashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);

						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {
						MainPanel.setCommInfo(false);
						if (result == null) {
							return;
						}
						Set<String> names = fields.keySet();
						Iterator<String> i = names.iterator();
						while (i.hasNext()) {
							String name = i.next();
							if (result.containsKey(name)) {
								HeaderField hf = fields.get(name);
								hf.setValue(result.get(name), true);
							}
						}

					}
				});

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
			Services.catalogs.trashRecord(catalogname, (Long) tag2,
					new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							MainPanel.setCommInfo(false);
							new AlertDialog(caught).show();
						}

						@Override
						public void onSuccess(Boolean result) {
							MainPanel.setCommInfo(false);
							if (result) {
								reloadList();
							} else {
								new AlertDialog(M.catalog.msgTrashError())
										.show();
							}

						}
					});
		}

	}

	/**
	 * Creates the new record.
	 */
	private void openEditor() {

		effectHide(listscroll.getElement());
		effectShow(editor.getElement());
	}

	/**
	 * Reload list.
	 */
	@Override
	public void reloadList() {

		list.clear();
		list.add(progress);

		MainPanel.setCommInfo(true);
		Services.catalogs.getAllRecords(catalogname,
				new AsyncCallback<Set<Long>>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.google.gwt.user.client.rpc.AsyncCallback#onFailure
					 * (java.lang .Throwable)
					 */
					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(Set<Long> result) {
						linecounter = 0L;
						list.clear();
						Label lab = new Label(M.catalog.titleList());
						list.add(lab);
						list.setCellHeight(lab, "30px");
						MainPanel.setCommInfo(false);
						Iterator<Long> i = result.iterator();
						while (i.hasNext()) {
							Long number = i.next();
							list.add(createListRecordRow(number));
						}
						if (!listscroll.isVisible() && (editor != null)
								&& editor.isVisible()) {
							effectHide(editor.getElement());
							effectShow(listscroll.getElement());
						}
					}
				});
	}
}

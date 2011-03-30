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

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.client.DocumentServiceAsync;
import net.autosauler.ballance.client.databases.DocumentsDatabase;
import net.autosauler.ballance.client.utils.SimpleDateFormat;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class DocumentPanel.
 * 
 * @author alexript
 */
public abstract class DocumentPanel extends Composite implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The documentname. */
	private final String documentname;

	/** The service. */
	private static DocumentServiceAsync service = null;

	/** The tabimage. */
	private final Image tabimage;

	/** The l. */
	private static DocumentMessages l = null;

	/** The progress. */
	private static Image progress = null;

	/** The images. */
	protected static MenuImages images = GWT.create(MenuImages.class);

	/** The Constant DATEFORMATTER. */
	private static final String DATEFORMATTER = "yyyy/MM/dd HH:mm";

	/** The formatter. */
	private static SimpleDateFormat formatter = null;

	/** The root. */
	private AbsolutePanel root;

	/** The list. */
	private VerticalPanel list;

	/** The editor. */
	private VerticalPanel editor;

	/** The cell table. */
	@UiField(provided = true)
	private CellTable<HashMap<String, Object>> cellTable;

	/** The pager. */
	@UiField(provided = true)
	private SimplePager pager;

	/** The db. */
	private DocumentsDatabase db;

	/** The Constant KEY_PROVIDER. */
	private static final ProvidesKey<HashMap<String, Object>> KEY_PROVIDER = new ProvidesKey<HashMap<String, Object>>() {

		/**
		 * Gets the key.
		 * 
		 * @param item
		 *            the item
		 * @return the key
		 */
		@Override
		public Object getKey(HashMap<String, Object> map) {
			return map == null ? null : map.get("number");
		}
	};

	/**
	 * Instantiates a new document panel.
	 * 
	 * @param docname
	 *            the docname
	 * @param image
	 *            the image
	 */
	public DocumentPanel(String docname, Image image) {
		documentname = docname;
		tabimage = image;
		if (service == null) {
			service = GWT.create(DocumentService.class);
			l = GWT.create(DocumentMessages.class);
			progress = new Image(images.progress());
			formatter = new SimpleDateFormat(DATEFORMATTER);
		}
	}

	/**
	 * Can activate.
	 * 
	 * @param role
	 *            the role
	 * @return true, if successful
	 */
	abstract boolean canActivate(UserRole role);

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
	 * Clean edit form.
	 */
	abstract void cleanEditForm();

	/**
	 * Creates the document header editor.
	 * 
	 * @return the widget
	 */
	protected abstract Widget createDocumentHeaderEditor();

	/**
	 * Creates the editor form.
	 */
	private void createEditorForm() {
		editor = new VerticalPanel();
		editor.setVisible(false);

		editor.add(createDocumentHeaderEditor());

		if (hasTablePart()) {
			// TODO: tableparts
		}

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setSpacing(3);

		buttons.add(new Button("Save"));
		buttons.add(new Button("Save and Activate"));
		buttons.add(new Button("Cancel"));

		editor.add(buttons);

		root.add(editor, 0, 0);
	}

	/**
	 * Creates the list form.
	 */
	private void createListForm() {
		db = DocumentsDatabase.get(documentname);

		list = new VerticalPanel();

		cellTable = new CellTable<HashMap<String, Object>>(KEY_PROVIDER);
		cellTable.setWidth("100%", true);

		// ListHandler<HashMap<String, Object>> sortHandler = null;
		// ListHandler<HashMap<String, Object>> sortHandler = new
		// ListHandler<HashMap<String, Object>>(
		// db.getDataProvider().getList());
		// cellTable.addColumnSortHandler(sortHandler);

		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setDisplay(cellTable);

		final SelectionModel<HashMap<String, Object>> selectionModel = new SingleSelectionModel<HashMap<String, Object>>(
				KEY_PROVIDER);

		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager
						.<HashMap<String, Object>> createCheckboxManager());

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						HashMap<String, Object> map = ((SingleSelectionModel<HashMap<String, Object>>) selectionModel)
								.getSelectedObject();
						if (map != null) {
							// edit.setEnabled(true);
							// del.setEnabled(true);
						} else {
							// edit.setEnabled(false);
							// del.setEnabled(false);

						}
					}
				});
		initTableColumns(selectionModel); // , sortHandler);
		db.addDataDisplay(cellTable);
		list.add(cellTable);
		list.add(pager);

		root.add(list, 0, 0);
	}

	/**
	 * Draw document row for list.
	 * 
	 * @param map
	 *            the map
	 * @return the string
	 */
	protected abstract String drawDocumentRowForList(HashMap<String, Object> map);

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
	 * Gets the list form.
	 * 
	 * @return the list form
	 */
	public DocumentPanel getListForm() {
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
							cleanEditForm();
							openEditor();
						}
					});
		}
		return menu;

	}

	/**
	 * Checks for table part.
	 * 
	 * @return true, if successful
	 */
	protected abstract boolean hasTablePart();

	/**
	 * Inits the table columns.
	 * 
	 * @param selectionModel
	 *            the selection model
	 */
	private void initTableColumns(
			final SelectionModel<HashMap<String, Object>> selectionModel) {
		// , ListHandler<HashMap<String, Object>> sortHandler) {

		// Checkbox column. This table will uses a checkbox column for
		// selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to
		// enable
		// mouse selection.
		Column<HashMap<String, Object>, Boolean> checkColumn = new Column<HashMap<String, Object>, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(HashMap<String, Object> map) {
				// Get the value from the selection model.
				return selectionModel.isSelected(map);
			}
		};
		cellTable.addColumn(checkColumn,
				SafeHtmlUtils.fromSafeConstant("<br/>"));
		cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Doc number.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> docNumberColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				return ((Long) map.get("number")).toString();
			}
		};
		// docNumberColumn.setSortable(true);
		// sortHandler.setComparator(docNumberColumn,
		// new Comparator<HashMap<String, Object>>() {
		// @Override
		// public int compare(HashMap<String, Object> m1,
		// HashMap<String, Object> m2) {
		// Long n1 = (Long) m1.get("number");
		// Long n2 = (Long) m2.get("number");
		// return n1.compareTo(n2);
		// }
		// });
		cellTable.addColumn(docNumberColumn, l.colNumber());

		cellTable.setColumnWidth(docNumberColumn, 50, Unit.PX);

		// Active flag.
		// ----------------------------------------------------------

		Column<HashMap<String, Object>, ImageResource> activeColumn = new Column<HashMap<String, Object>, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(HashMap<String, Object> map) {
				Boolean isactive = (Boolean) map.get("active");
				if (isactive) {
					return images.Ok();
				}
				return images.Cancel();
			}
		};

		activeColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		cellTable.addColumn(activeColumn, "");

		cellTable.setColumnWidth(activeColumn, 20, Unit.PX);

		// Doc creation date.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, Date> docDateColumn = new Column<HashMap<String, Object>, Date>(
				new DateCell()) {
			@Override
			public Date getValue(HashMap<String, Object> map) {
				return new Date((Long) map.get("createdate"));
			}
		};
		cellTable.addColumn(docDateColumn, l.colCreateDate());

		cellTable.setColumnWidth(docDateColumn, 150, Unit.PX);

		// Doc author.
		// ----------------------------------------------------------
		Column<HashMap<String, Object>, String> docAuthorColumn = new Column<HashMap<String, Object>, String>(
				new TextCell()) {
			@Override
			public String getValue(HashMap<String, Object> map) {
				return (String) map.get("author");
			}
		};
		cellTable.addColumn(docAuthorColumn, l.colAuthor());

		cellTable.setColumnWidth(docAuthorColumn, 100, Unit.PX);

		// Document title.
		// ----------------------------------------------------------

		Column<HashMap<String, Object>, SafeHtml> rolesColumn = new Column<HashMap<String, Object>, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(HashMap<String, Object> map) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant(drawDocumentRowForList(map));
				return sb.toSafeHtml();
			}

		};
		rolesColumn.setSortable(false);

		cellTable.addColumn(rolesColumn, l.colDocument());

		cellTable.setColumnWidth(rolesColumn, 300, Unit.PX);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.IDialogYesReceiver#onDialogYesButtonClick
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public void onDialogYesButtonClick(String tag, Object tag2) {
		// TODO Auto-generated method stub

	}

	/**
	 * Open editor.
	 */
	private void openEditor() {
		effectHide(list.getElement());
		effectShow(editor.getElement());
	}

	/**
	 * Reload list.
	 */
	private void reloadList() {
		db.getDataProvider().reload();
		if (!list.isVisible() && (editor != null) && editor.isVisible()) {
			effectHide(editor.getElement());
			effectShow(list.getElement());
		}
	}

}

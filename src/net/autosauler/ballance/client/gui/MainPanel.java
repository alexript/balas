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

import java.util.List;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.UserRole;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainPanel.
 */
public class MainPanel implements ValueChangeHandler<String> {

	/** The busycounter. */
	private static int busycounter = 0;

	/** The mainpane. */
	private static TabPanel mainpane;

	/** The comminfo. */
	// private static CommInfoPanel comminfo = null;

	/**
	 * Drop main pane.
	 */
	public static void dropMainPane() {
		mainpane.removeAll();
	}

	/**
	 * Sets the comm info.
	 * 
	 * @param f
	 *            the new comm info
	 */
	public static void setCommInfo(boolean f) {
		if (f) {
			busycounter++;
			if (busycounter == 1) {
				// comminfo = new CommInfoPanel();
				// comminfo.show();
				if (Ballance_autosauler_net.state != null) {
					Ballance_autosauler_net.state.setBusy(M.comm
							.commInProgress());
				}
			}
		} else {
			busycounter--;
			if (busycounter < 1) {
				// comminfo.hide();
				busycounter = 0;
				if (Ballance_autosauler_net.state != null) {
					Ballance_autosauler_net.state.clearStatus("");
				}
			}
		}
	}

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel(TabPanel maintabpane) {

		mainpane = maintabpane;

		String initToken = History.getToken();
		if (initToken.length() == 0) {
			History.newItem("start");
		}
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
	}

	private void closeTab(String tag) {
		TabItem item = mainpane.getItemByItemId(tag);
		if (item != null) {
			mainpane.remove(item);

		}
	}

	/**
	 * Construct tab pane.
	 * 
	 * @param name
	 *            the name
	 * @return the widget
	 */
	private void constructTabPane(String name) {
		TabItem item = mainpane.getItemByItemId(name);
		if (item != null) {
			mainpane.setSelection(item);
		} else {

			UserRole role = Ballance_autosauler_net.sessionId.getUserrole();

			if (name.equals("start")) {
				constructTabPaneContent(HelloPanel.get(),
						M.menu.itemHelloToAll(), Images.menu.icoInfo(), name);
			} else if (name.equals("dbpane") && role.isAdmin()) {
				constructTabPaneContent(DatabasePanel.get(),
						M.menu.itemDatabase(), Images.menu.icoDatabase(), name);

			} else if (name.equals("editusers") && role.isAdmin()) {
				constructTabPaneContent(UsersPanel.get(), M.menu.itemUsers(),
						Images.menu.icoUser(), name);
			} else if (name.equals("editstruct") && role.isAdmin()) {
				constructTabPaneContent(StructurePanel.get(),
						M.menu.itemStructure(), Images.menu.Structure(), name);
			} else if (name.equals("editdoc") && role.isAdmin()) {
				constructTabPaneContent(DocumentationPanel.get(),
						M.menu.itemDocumentation(), Images.menu.Structure(),
						name);
			} else if (name.equals("license")) {
				constructTabPaneContent(LicensePanel.get(),
						M.menu.itemLicense(), Images.menu.icoCopyright(), name);
			} else if (name.equals("changelog") && !role.isGuest()) {
				constructTabPaneContent(ChangeLogPanel.get(),
						M.menu.itemChangelog(), Images.menu.icoChangelog(),
						name);
			} else if (name.equals("currval") && !role.isGuest()) {
				constructTabPaneContent(new ReportPanel("report.currvalues"),
						M.menu.itemReportCurrval(), Images.menu.icoCurrval(),
						name);

			} else {
				Description d = StructureFactory.getDescription(name);
				if (d != null) {
					if ((new UserRole(d.getRole())).hasAccess(role)) {
						String itemname = d.getName().getName(
								LocaleInfo.getCurrentLocale().getLocaleName());

						int type = StructureFactory.getPanelType(name);
						if (type == 0) {
							constructTabPaneContent(new CatalogPanel(name),
									itemname,
									StructureFactory.getMenuIcon(name), name);
						} else if (type == 1) {
							constructTabPaneContent(new DocumentPanel(name),
									itemname,
									StructureFactory.getMenuIcon(name), name);
						} else if (type == 2) {
							constructTabPaneContent(new ReportPanel(name),
									itemname,
									StructureFactory.getMenuIcon(name), name);
						}
					}
				} else {
					new AlertDialog("Uncknown command", name).show();
				}
			}
		}
	}

	/**
	 * Construct tab pane content.
	 * 
	 * @param realpane
	 *            the realpane
	 * @param title
	 *            the title
	 * @param ico
	 *            the ico
	 * @param tag
	 *            the tag
	 * @return the vertical panel
	 */
	private Widget constructTabPaneContent(IPaneWithMenu realpane,
			String title, ImageResource ico, final String tag) {
		ContentPanel panel = new ContentPanel(new BorderLayout());
		panel.setBodyStyle("background-color:#ffffff;");
		panel.setHeaderVisible(false);

		MenuBar menu = realpane.getPaneMenu();
		if (menu == null) {
			menu = new MenuBar();
		}

		Menu sysmenu = new Menu();

		sysmenu.add(new MenuItem(M.menu.icoReloadPane(), AbstractImagePrototype
				.create(Images.menu.icoReload()),
				new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						closeTab(tag);
						openTab(tag);

					}
				}));

		sysmenu.add(new MenuItem(M.menu.icoClosePane(), AbstractImagePrototype
				.create(Images.menu.icoClose()),
				new SelectionListener<MenuEvent>() {

					@Override
					public void componentSelected(MenuEvent ce) {
						closeTab(tag);

					}
				}));

		menu.add(new MenuBarItem(M.menu.menuPanelmenu(), sysmenu));

		List<MenuItem> helpitems = realpane.getHelpItems();
		if ((helpitems != null) && (helpitems.size() > 0)) {
			Menu helpmenu = new Menu();

			for (MenuItem helpitem : helpitems) {
				helpmenu.add(helpitem);
			}

			helpmenu.add(new SeparatorMenuItem());
			helpmenu.add(new MenuItem(M.menu.helpIndex(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							HelpDialog d = new HelpDialog(M.menu.helpIndex());
							d.loadHelpText("doc", "index.html");

						}
					}));
			menu.add(new MenuBarItem(M.menu.menuPanelHelpmenu(), helpmenu));

		}
		panel.add(menu, new BorderLayoutData(LayoutRegion.NORTH, 30));

		BorderLayoutData center = new BorderLayoutData(LayoutRegion.CENTER);
		panel.setScrollMode(Scroll.AUTO);

		panel.add((Widget) realpane, center);

		TabItem tabitem = new TabItem(title);
		tabitem.setIcon(AbstractImagePrototype.create(ico));
		tabitem.setItemId(tag);
		tabitem.setClosable(true);
		tabitem.setLayout(new FitLayout());
		tabitem.add(panel);

		mainpane.add(tabitem);
		mainpane.setSelection(tabitem);

		return tabitem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(
	 * com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String eventvalue = event.getValue();
		openTab(eventvalue);

	}

	/**
	 * Open tab.
	 * 
	 * @param tag
	 *            the tag
	 */
	public void openTab(String tag) {
		if ((tag == null) || tag.isEmpty()) {
			tag = "start";
		}
		if ((tag != null) && !tag.isEmpty()) {
			tag = tag.trim().toLowerCase();

			constructTabPane(tag);

			String initToken = History.getToken();
			if (initToken.length() == 0) {
				History.newItem("start");
			}
			if (!initToken.equals(tag)) {
				History.newItem(tag);
				History.fireCurrentHistoryState();
			}

		}
	}
}

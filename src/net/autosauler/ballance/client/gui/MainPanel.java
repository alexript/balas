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
import java.util.Set;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainPanel.
 */
public class MainPanel extends Composite implements ValueChangeHandler<String> {

	/** The busycounter. */
	private static int busycounter = 0;

	/**
	 * Close tab.
	 * 
	 * @param tag
	 *            the tag
	 */
	public static void closeTab(String tag) {
		// There are some magic. It's working, but I don't know why.
		if (tabsIndexes.containsKey(tag)) {
			String newselection = "start";

			int index = tabsIndexes.get(tag);

			VerticalPanel w = (VerticalPanel) mainpane.getWidget(index);
			mainpane.remove(index);
			w.clear();
			w = null;
			tabsIndexes.remove(tag);
			Set<String> keys = tabsIndexes.keySet();
			for (String key : keys) {
				int i = tabsIndexes.get(key);
				if (i > index) {
					tabsIndexes.put(key, i - 1);
				}
				if ((i - 1 == index) || (i == index - 1)) {
					newselection = key;
				}
			}

			if (index == 0) {
				if (mainpane.getWidgetCount() < 1) {
					dropMainPane();
					History.newItem("start");
					History.fireCurrentHistoryState();
				} else {
					mainpane.selectTab(0);
				}
			} else {
				mainpane.selectTab(index - 1);
			}

			String initToken = History.getToken();
			if (initToken.length() == 0) {
				History.newItem("start");
			}
			if (!newselection.isEmpty() && !initToken.equals(newselection)) {
				History.newItem(newselection);
				History.fireCurrentHistoryState();
			}

		}
	}

	/**
	 * Drop main pane.
	 */
	public static void dropMainPane() {
		mainpane.clear();
		tabsIndexes.clear();
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
				comminfo = new CommInfoPanel();
				comminfo.show();
			}
		} else {
			busycounter--;
			if (busycounter < 1) {
				comminfo.hide();
				busycounter = 0;
			}
		}
	}

	/** The panel. */
	private final DockLayoutPanel panel = new DockLayoutPanel(Unit.PX);

	/** The left column panel. */
	private final LeftPanel leftPanel = new LeftPanel();

	/** The mainpane. */
	private static DecoratedTabPanel mainpane = new DecoratedTabPanel();

	/** The tabs indexes. */
	private static HashMap<String, Integer> tabsIndexes = new HashMap<String, Integer>();

	/** The comminfo. */
	private static CommInfoPanel comminfo = null;

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		panel.addWest(leftPanel, 250);
		TopPanel toppanel = new TopPanel();
		panel.addNorth(toppanel, 32);
		mainpane.setWidth("100%");
		mainpane.setHeight("100%");
		panel.add(mainpane);
		panel.setHeight("100%");
		initWidget(panel);
		this.setStyleName("mainPanel");

		String initToken = History.getToken();
		if (initToken.length() == 0) {
			History.newItem("start");
		}
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
		toppanel.startAnimation();
	}

	/**
	 * Construct tab pane.
	 * 
	 * @param name
	 *            the name
	 * @return the widget
	 */
	private VerticalPanel constructTabPane(String name) {

		VerticalPanel w = null;
		UserRole role = Ballance_autosauler_net.sessionId.getUserrole();

		if (name.equals("start")) {
			w = constructTabPaneContent(HelloPanel.get(),
					M.menu.itemHelloToAll(), Images.menu.icoInfo(), name);
		} else if (name.equals("dbpane") && role.isAdmin()) {
			w = constructTabPaneContent(DatabasePanel.get(),
					M.menu.itemDatabase(), Images.menu.icoDatabase(), name);

		} else if (name.equals("editusers") && role.isAdmin()) {
			w = constructTabPaneContent(UsersPanel.get(), M.menu.itemUsers(),
					Images.menu.icoUser(), name);
		} else if (name.equals("license")) {
			w = constructTabPaneContent(LicensePanel.get(),
					M.menu.itemLicense(), Images.menu.icoCopyright(), name);
		} else if (name.equals("partners")
				&& (new UserRole(StructureFactory.getDescription(
						"catalog.partners").getRole())).hasAccess(role)) {
			w = constructTabPaneContent(new CatalogPanel("partners", new Image(
					Images.menu.icoPartners())).getListForm(),
					M.menu.itemPartners(), Images.menu.icoPartners(), name);
		} else if (name.equals("tarifs")
				&& (new UserRole(StructureFactory.getDescription(
						"catalog.tarifs").getRole())).hasAccess(role)) {
			w = constructTabPaneContent(new CatalogPanel("tarifs", new Image(
					Images.menu.icoTarif())).getListForm(), M.menu.itemTarif(),
					Images.menu.icoTarif(), name);
		} else if (name.equals("paymethod")
				&& (new UserRole(StructureFactory.getDescription(
						"catalog.paymethod").getRole())).hasAccess(role)) {
			w = constructTabPaneContent(new CatalogPanel("paymethod",
					new Image(Images.menu.icoPaymethod())).getListForm(),
					M.menu.itemPaymethod(), Images.menu.icoPaymethod(), name);
		} else if (name.equals("incpay")
				&& (new UserRole(StructureFactory.getDescription(
						"document.inpay").getRole())).hasAccess(role)) {
			w = constructTabPaneContent(new DocumentPanel("inpay", new Image(
					Images.menu.icoIncPay())).getListForm(),
					M.menu.itemIncPay(), Images.menu.icoIncPay(), name);
		} else if (name.equals("ingoods")
				&& (new UserRole(StructureFactory.getDescription(
						"document.ingoods").getRole())).hasAccess(role)) {
			w = constructTabPaneContent(new DocumentPanel("ingoods", new Image(
					Images.menu.icoInGoods())).getListForm(),
					M.menu.itemInGoods(), Images.menu.icoInGoods(), name);
		} else if (name.equals("changelog") && !role.isGuest()) {
			w = constructTabPaneContent(ChangeLogPanel.get(),
					M.menu.itemChangelog(), Images.menu.icoChangelog(), name);
		} else {
			new AlertDialog("Uncknown command", name).show();
		}

		return w;
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
	private VerticalPanel constructTabPaneContent(IPaneWithMenu realpane,
			String title, ImageResource ico, final String tag) {
		VerticalPanel panel = new VerticalPanel();

		HorizontalPanel panemenu = new HorizontalPanel();
		panemenu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panemenu.setHeight("20px");
		panemenu.setWidth("100%");

		MenuBar menu = realpane.getPaneMenu();
		if (menu == null) {
			menu = new MenuBar();
		}

		MenuBar sysmenu = new MenuBar();

		sysmenu.addItem(M.menu.icoReloadPane(), new Command() { // reload users
																// list
					@Override
					public void execute() {
						MainPanel.closeTab(tag);
						openTab(tag);
					}
				});

		sysmenu.addItem(M.menu.icoClosePane(), new Command() { // reload users
																// list
					@Override
					public void execute() {
						MainPanel.closeTab(tag);
					}
				});

		menu.addItem(M.menu.menuPanelmenu(), sysmenu);

		panemenu.add(menu);
		panemenu.setCellHorizontalAlignment(menu,
				HasHorizontalAlignment.ALIGN_LEFT);

		Image reloadImage = new Image(Images.menu.icoReload());
		reloadImage.setTitle(M.menu.icoReloadPane());
		reloadImage.setAltText(M.menu.icoReloadPane());
		reloadImage.addClickHandler(new ClickHandler() {
			private final String mytag = tag;

			@Override
			public void onClick(ClickEvent event) {
				MainPanel.closeTab(mytag);
				openTab(mytag);
			}

		});
		panemenu.add(reloadImage);
		panemenu.setCellWidth(reloadImage, "20px");

		Image closeImage = new Image(Images.menu.icoClose());
		closeImage.setTitle(M.menu.icoClosePane());
		closeImage.setAltText(M.menu.icoClosePane());
		closeImage.addClickHandler(new ClickHandler() {
			private final String mytag = tag;

			@Override
			public void onClick(ClickEvent event) {
				MainPanel.closeTab(mytag);

			}

		});
		panemenu.add(closeImage);
		panemenu.setCellWidth(closeImage, "20px");

		panel.add(panemenu);
		ScrollPanel scroll = new ScrollPanel((Widget) realpane);
		scroll.setSize("100%", "500px");
		panel.add(scroll);
		panel.setHeight("100%");
		mainpane.add(panel, getTabHeaderString(title, ico), true);
		return panel;
	}

	/**
	 * Gets the tab header string.
	 * 
	 * @param text
	 *            the text
	 * @param image
	 *            the image
	 * @return the tab header string
	 */
	private String getTabHeaderString(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(2);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.add(new Image(image));
		HTML headerText = new HTML(text);
		hPanel.add(headerText);

		// Return the HTML string for the panel
		return hPanel.getElement().getString();
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
	private void openTab(String tag) {
		if ((tag == null) || tag.isEmpty()) {
			tag = "start";
		}
		if ((tag != null) && !tag.isEmpty()) {
			tag = tag.trim().toLowerCase();
			int paneindex = -1;
			if (tabsIndexes.containsKey(tag)) {
				paneindex = tabsIndexes.get(tag);
			}

			if (paneindex < 0) {
				VerticalPanel w = constructTabPane(tag);
				if (w != null) {
					paneindex = mainpane.getWidgetIndex(w);
					tabsIndexes.put(tag, paneindex);
				}
			}
			mainpane.selectTab(paneindex);
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

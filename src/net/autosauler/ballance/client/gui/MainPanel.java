/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client.gui;

import java.util.HashMap;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainPanel.
 */
public class MainPanel extends Composite implements ValueChangeHandler<String> {

	/** The panel. */
	private HorizontalPanel panel = new HorizontalPanel();

	/** The left column panel. */
	private LeftPanel leftPanel = new LeftPanel();

	/** The mainpane. */
	private static DecoratedTabPanel mainpane = new DecoratedTabPanel();

	/** The tabs indexes. */
	private static HashMap<String, Integer> tabsIndexes = new HashMap<String, Integer>();

	/** The comminfo. */
	private static CommInfoPanel comminfo = null;

	/** The l. */
	private MenuMessages l;

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		l = GWT.create(MenuMessages.class);
		panel.add(leftPanel);
		panel.setCellWidth(leftPanel, "244px");
		mainpane.setWidth("100%");
		mainpane.setHeight("100%");
		panel.add(mainpane);
		initWidget(panel);
		this.setStyleName("mainPanel");

		String initToken = History.getToken();
		if (initToken.length() == 0) {
			History.newItem("start");
		}
		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();

	}

	/**
	 * Sets the comm info.
	 * 
	 * @param f
	 *            the new comm info
	 */
	public static void setCommInfo(boolean f) {
		if (f) {
			comminfo = new CommInfoPanel();
			comminfo.show();
		} else {
			comminfo.hide();
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
	 * Construct tab pane.
	 * 
	 * @param name
	 *            the name
	 * @return the widget
	 */
	private Widget constructTabPane(String name) {
		Widget w = null;
		UserRole role = Ballance_autosauler_net.sessionId.getUserrole();

		if (name.equals("start")) {
			w = new HelloPanel();
			mainpane.add(w, l.itemHelloToAll());

		} else if (name.equals("dbpane") && role.isAdmin()) {
			w = new HTML("Database");
			mainpane.add(w, l.itemDatabase());

		} else if (name.equals("editusers") && role.isAdmin()) {
			w = new HTML("Edit users");
			mainpane.add(w, l.itemUsers());
		} else if (name.equals("license")) {
			w = new LicensePanel();
			mainpane.add(w, l.itemLicense());
		} else {
			Window.alert("Error #404");
		}

		return w;
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
		if (eventvalue != null && !eventvalue.isEmpty()) {
			eventvalue = eventvalue.trim().toLowerCase();
			int paneindex = -1;
			if (tabsIndexes.containsKey(eventvalue)) {
				paneindex = tabsIndexes.get(eventvalue);
			}

			if (paneindex < 0) {
				Widget w = constructTabPane(eventvalue);
				if (w != null) {
					paneindex = mainpane.getWidgetIndex(w);
					tabsIndexes.put(eventvalue, paneindex);
				}
			}
			mainpane.selectTab(paneindex);
		}

	}
}

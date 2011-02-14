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
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MainPanel.
 */
public class MainPanel extends Composite implements ValueChangeHandler<String> {

	/**
	 * Close tab.
	 * 
	 * @param tag
	 *            the tag
	 */
	public static void closeTab(String tag) {
		if (tabsIndexes.containsKey(tag)) {
			String newselection = "";

			int index = tabsIndexes.get(tag);
			mainpane.remove(index);
			tabsIndexes.remove(tag);
			Set<String> keys = tabsIndexes.keySet();
			for (String key : keys) {
				int i = tabsIndexes.get(key);
				if (i > index) {
					tabsIndexes.put(key, i - 1);
				}
				if (i == index) {
					newselection = key;
				}
			}

			if (index == 0) {
				if (mainpane.getWidgetCount() < 1) {
					dropMainPane();
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
			comminfo = new CommInfoPanel();
			comminfo.show();
		} else {
			comminfo.hide();
		}
	}

	/** The panel. */
	private final HorizontalPanel panel = new HorizontalPanel();

	/** The left column panel. */
	private final LeftPanel leftPanel = new LeftPanel();

	/** The mainpane. */
	private static DecoratedTabPanel mainpane = new DecoratedTabPanel();

	/** The tabs indexes. */
	private static HashMap<String, Integer> tabsIndexes = new HashMap<String, Integer>();

	/** The comminfo. */
	private static CommInfoPanel comminfo = null;

	/** The l. */
	private final MenuMessages l;

	/** The images. */
	private final MenuImages images;

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		l = GWT.create(MenuMessages.class);
		images = (MenuImages) GWT.create(MenuImages.class);
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
			w = constructTabPaneContent(new HelloPanel(), l.itemHelloToAll(),
					images.icoInfo(), name);
		} else if (name.equals("dbpane") && role.isAdmin()) {
			w = constructTabPaneContent(new DatabasePanel(), l.itemDatabase(),
					images.icoDatabase(), name);

		} else if (name.equals("editusers") && role.isAdmin()) {
			w = constructTabPaneContent(new UsersPanel(), l.itemUsers(),
					images.icoUser(), name);
		} else if (name.equals("license")) {
			w = constructTabPaneContent(new LicensePanel(), l.itemLicense(),
					images.icoCopyright(), name);
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
	private VerticalPanel constructTabPaneContent(Widget realpane,
			String title, ImageResource ico, final String tag) {
		VerticalPanel panel = new VerticalPanel();

		HorizontalPanel panemenu = new HorizontalPanel();
		panemenu.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panemenu.setHeight("20px");
		panemenu.setWidth("100%");

		Image closeImage = new Image(images.icoClose());
		closeImage.setTitle(l.icoClosePane());
		closeImage.setAltText(l.icoClosePane());
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
		panel.add(realpane);
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
		if ((eventvalue == null) || eventvalue.isEmpty()) {
			eventvalue = "start";
		}
		if ((eventvalue != null) && !eventvalue.isEmpty()) {
			eventvalue = eventvalue.trim().toLowerCase();
			int paneindex = -1;
			if (tabsIndexes.containsKey(eventvalue)) {
				paneindex = tabsIndexes.get(eventvalue);
			}

			if (paneindex < 0) {
				VerticalPanel w = constructTabPane(eventvalue);
				if (w != null) {
					paneindex = mainpane.getWidgetIndex(w);
					tabsIndexes.put(eventvalue, paneindex);
				}
			}
			mainpane.selectTab(paneindex);
			String initToken = History.getToken();
			if (initToken.length() == 0) {
				History.newItem("start");
			}
			if (!initToken.equals(eventvalue)) {
				History.newItem(eventvalue);
				History.fireCurrentHistoryState();
			}

		}

	}
}

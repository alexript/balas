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
import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.MenuMessages;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class LeftMenu.
 */
public class LeftMenu extends Composite {

	/** The panel. */
	private final DecoratedStackPanel panel;

	/** The role. */
	private UserRole role = null;

	/** The l. */
	private static final MenuMessages l = GWT.create(MenuMessages.class);

	/**
	 * Instantiates a new left menu.
	 */
	public LeftMenu() {
		panel = new DecoratedStackPanel();
		panel.setWidth("244px");

		buildContent();
		initWidget(panel);
	}

	/**
	 * Builds the admin pane.
	 */
	private void buildAdminPane() {
		if (role.isAdmin()) {
			VerticalPanel adminpanel = new VerticalPanel();

			adminpanel.add(getMenuItem(l.itemDatabase(), "dbpane",
					Images.menu.icoDatabase()));
			adminpanel.add(getMenuItem(l.itemUsers(), "editusers",
					Images.menu.icoUser()));

			String adminHeader = getHeaderString(l.adminPanel(),
					Images.menu.adminPanel());

			panel.add(adminpanel, adminHeader, true);

		}
	}

	/**
	 * Builds the content.
	 */
	public void buildContent() {
		clear();
		role = Ballance_autosauler_net.sessionId.getUserrole();
		buildAdminPane();
		buildDocumentsPane();
		buildFinancesPane();
		buildManagerPane();
		buildForAllPane();
		buildGuestPane();
	}

	/**
	 * Builds the documents pane.
	 */
	private void buildDocumentsPane() {
		if (role.isAdmin() || role.isDocuments()) {
			VerticalPanel documentspanel = new VerticalPanel();

			documentspanel.add(getMenuItem(l.itemInGoods(), "ingoods",
					Images.menu.icoInGoods()));

			String documentsHeader = getHeaderString(l.documentsPanel(),
					Images.menu.documentsPanel());
			panel.add(documentspanel, documentsHeader, true);
		}
	}

	/**
	 * Builds the finances pane.
	 */
	private void buildFinancesPane() {
		if (role.isAdmin() || role.isFinances()) {
			VerticalPanel financesspanel = new VerticalPanel();

			financesspanel.add(getMenuItem(l.itemIncPay(), "incpay",
					Images.menu.icoIncPay()));
			financesspanel.add(getMenuItem(l.itemInGoods(), "ingoods",
					Images.menu.icoInGoods()));
			financesspanel.add(getMenuItem(l.itemTarif(), "tarifs",
					Images.menu.icoTarif()));

			String financesHeader = getHeaderString(l.financesPanel(),
					Images.menu.financesPanel());
			panel.add(financesspanel, financesHeader, true);
		}
	}

	/**
	 * Builds the for all pane.
	 */
	private void buildForAllPane() {
		if (!role.isGuest()) {
			VerticalPanel allpanel = new VerticalPanel();

			allpanel.add(getMenuItem(l.itemChangelog(), "changelog",
					Images.menu.icoChangelog()));

			String forAllHeader = getHeaderString(l.forAllPanel(),
					Images.menu.forAllPanel());
			panel.add(allpanel, forAllHeader, true);
		}
	}

	/**
	 * Builds the guest pane.
	 */
	private void buildGuestPane() {

		VerticalPanel guestpanel = new VerticalPanel();

		guestpanel.add(getMenuItem(l.itemHelloToAll(), "start",
				Images.menu.icoInfo()));

		guestpanel.add(getMenuItem(l.itemLicense(), "license",
				Images.menu.icoCopyright()));

		String guestHeader = getHeaderString(l.guestPanel(),
				Images.menu.guestPanel());
		panel.add(guestpanel, guestHeader, true);

	}

	/**
	 * Builds the manager pane.
	 */
	private void buildManagerPane() {
		if (role.isAdmin() || role.isManager()) {
			VerticalPanel managerpanel = new VerticalPanel();

			managerpanel.add(getMenuItem(l.itemPaymethod(), "paymethod",
					Images.menu.icoPaymethod()));

			managerpanel.add(getMenuItem(l.itemPartners(), "partners",
					Images.menu.icoPartners()));

			managerpanel.add(getMenuItem(l.itemTarif(), "tarifs",
					Images.menu.icoTarif()));

			String managerHeader = getHeaderString(l.managerPanel(),
					Images.menu.managerPanel());
			panel.add(managerpanel, managerHeader, true);
		}
	}

	/**
	 * Clear.
	 */
	public void clear() {
		panel.clear();
		role = null;
	}

	/**
	 * Gets the header string.
	 * 
	 * @param text
	 *            the text
	 * @param image
	 *            the image
	 * @return the header string
	 */
	private String getHeaderString(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(0);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.add(new Image(image));
		HTML headerText = new HTML(text);
		headerText.setStyleName("cw-StackPanelHeader");
		hPanel.add(headerText);

		// Return the HTML string for the panel
		return hPanel.getElement().getString();
	}

	/**
	 * Gets the menu item.
	 * 
	 * @param text
	 *            the text
	 * @param tag
	 *            the tag
	 * @param image
	 *            the image
	 * @return the menu item
	 */
	private HorizontalPanel getMenuItem(String text, String tag,
			ImageResource image) {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hPanel.add(new Image(image));
		Hyperlink link = new Hyperlink(text, tag);
		hPanel.add(link);

		return hPanel;
	}
}

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

import net.autosauler.ballance.client.Ballance_autosauler_net;
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
	private DecoratedStackPanel panel;

	/** The role. */
	private UserRole role = null;

	/** The l. */
	private MenuMessages l;

	/** The images. */
	private MenuImages images;

	/**
	 * Instantiates a new left menu.
	 */
	public LeftMenu() {
		l = GWT.create(MenuMessages.class);
		images = (MenuImages) GWT.create(MenuImages.class);
		panel = new DecoratedStackPanel();
		panel.setWidth("244px");

		buildContent();
		initWidget(panel);
	}

	/**
	 * Clear.
	 */
	public void clear() {
		panel.clear();
		role = null;
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
	 * Builds the admin pane.
	 */
	private void buildAdminPane() {
		if (role.isAdmin()) {
			VerticalPanel adminpanel = new VerticalPanel();

			Hyperlink dbmanage = new Hyperlink(l.itemDatabase(), "dbpane");
			adminpanel.add(dbmanage);

			Hyperlink usersmanage = new Hyperlink(l.itemUsers(), "editusers");
			adminpanel.add(usersmanage);

			String adminHeader = getHeaderString(l.adminPanel(),
					images.adminPanel());

			panel.add(adminpanel, adminHeader, true);

		}
	}

	/**
	 * Builds the documents pane.
	 */
	private void buildDocumentsPane() {
		if (role.isAdmin() || role.isDocuments()) {
			VerticalPanel documentspanel = new VerticalPanel();

			String documentsHeader = getHeaderString(l.documentsPanel(),
					images.documentsPanel());
			panel.add(documentspanel, documentsHeader, true);
		}
	}

	/**
	 * Builds the finances pane.
	 */
	private void buildFinancesPane() {
		if (role.isAdmin() || role.isFinances()) {
			VerticalPanel financesspanel = new VerticalPanel();

			String financesHeader = getHeaderString(l.financesPanel(),
					images.financesPanel());
			panel.add(financesspanel, financesHeader, true);
		}
	}

	/**
	 * Builds the manager pane.
	 */
	private void buildManagerPane() {
		if (role.isAdmin() || role.isManager()) {
			VerticalPanel managerpanel = new VerticalPanel();

			String managerHeader = getHeaderString(l.managerPanel(),
					images.managerPanel());
			panel.add(managerpanel, managerHeader, true);
		}
	}

	/**
	 * Builds the for all pane.
	 */
	private void buildForAllPane() {
		if (!role.isGuest()) {
			VerticalPanel allpanel = new VerticalPanel();

			String forAllHeader = getHeaderString(l.forAllPanel(),
					images.forAllPanel());
			panel.add(allpanel, forAllHeader, true);
		}
	}

	/**
	 * Builds the guest pane.
	 */
	private void buildGuestPane() {

		VerticalPanel guestpanel = new VerticalPanel();

		Hyperlink hellotoall = new Hyperlink(l.itemHelloToAll(), "start");
		guestpanel.add(hellotoall);

		Hyperlink license = new Hyperlink(l.itemLicense(), "license");
		guestpanel.add(license);

		String guestHeader = getHeaderString(l.guestPanel(),
				images.guestPanel());
		panel.add(guestpanel, guestHeader, true);

	}
}

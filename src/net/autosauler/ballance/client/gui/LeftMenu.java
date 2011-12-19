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
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class LeftMenu.
 */
public class LeftMenu {

	/** The panel. */
	private final ContentPanel panel;

	/** The role. */
	private UserRole role = null;

	/**
	 * Instantiates a new left menu.
	 */
	public LeftMenu(ContentPanel parent) {
		panel = new ContentPanel();
		panel.setHeaderVisible(false);
		panel.setLayout(new AccordionLayout());
		panel.setWidth("240px");

		buildContent();

		parent.add(panel);
	}

	/**
	 * Builds the admin pane.
	 */
	private void buildAdminPane() {
		if (role.isAdmin()) {
			ContentPanel cp = new ContentPanel();
			cp.setAnimCollapse(true);
			cp.setHeading(M.menu.adminPanel());
			cp.setIcon(AbstractImagePrototype.create(Images.menu.adminPanel()));
			cp.setLayout(new FitLayout());

			VerticalPanel adminpanel = new VerticalPanel();

			adminpanel.add(getMenuItem(M.menu.itemDatabase(), "dbpane",
					Images.menu.icoDatabase()));
			adminpanel.add(getMenuItem(M.menu.itemUsers(), "editusers",
					Images.menu.icoUser()));
			cp.add(adminpanel);

			panel.add(cp);

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

		panel.recalculate();
		panel.layout(true); // hack for forced redraw after rebuild
	}

	/**
	 * Builds the documents pane.
	 */
	private void buildDocumentsPane() {
		if (role.isAdmin() || role.isDocuments()) {
			ContentPanel cp = new ContentPanel();
			cp.setAnimCollapse(true);
			cp.setHeading(M.menu.documentsPanel());
			cp.setIcon(AbstractImagePrototype.create(Images.menu
					.documentsPanel()));
			cp.setLayout(new FitLayout());

			VerticalPanel documentspanel = new VerticalPanel();

			documentspanel.add(getMenuItem(M.menu.itemInGoods(), "ingoods",
					Images.menu.icoInGoods()));

			documentspanel.add(getMenuItem(M.menu.itemPartners(), "partners",
					Images.menu.icoPartners()));

			documentspanel.add(getMenuItem(M.menu.itemCars(), "cars",
					Images.menu.icoCar()));

			documentspanel.add(getMenuItem(M.menu.itemDrivers(), "drivers",
					Images.menu.icoMan()));

			cp.add(documentspanel);
			panel.add(cp);
		}
	}

	/**
	 * Builds the finances pane.
	 */
	private void buildFinancesPane() {
		if (role.isAdmin() || role.isFinances()) {
			ContentPanel cp = new ContentPanel();
			cp.setAnimCollapse(true);
			cp.setHeading(M.menu.financesPanel());
			cp.setIcon(AbstractImagePrototype.create(Images.menu
					.financesPanel()));
			cp.setLayout(new FitLayout());

			VerticalPanel financesspanel = new VerticalPanel();

			financesspanel.add(getMenuItem(M.menu.itemIncPay(), "incpay",
					Images.menu.icoIncPay()));
			financesspanel.add(getMenuItem(M.menu.itemInGoods(), "ingoods",
					Images.menu.icoInGoods()));
			financesspanel.add(getMenuItem(M.menu.itemTarif(), "tarifs",
					Images.menu.icoTarif()));

			cp.add(financesspanel);
			panel.add(cp);
		}
	}

	/**
	 * Builds the for all pane.
	 */
	private void buildForAllPane() {
		if (!role.isGuest()) {
			ContentPanel cp = new ContentPanel();
			cp.setAnimCollapse(true);
			cp.setHeading(M.menu.forAllPanel());
			cp.setIcon(AbstractImagePrototype.create(Images.menu.forAllPanel()));
			cp.setLayout(new FitLayout());

			VerticalPanel allpanel = new VerticalPanel();

			allpanel.add(getMenuItem(M.menu.itemReportCurrval(), "currval",
					Images.menu.icoCurrval()));

			allpanel.add(getMenuItem(M.menu.itemChangelog(), "changelog",
					Images.menu.icoChangelog()));

			cp.add(allpanel);
			panel.add(cp);
		}
	}

	/**
	 * Builds the guest pane.
	 */
	private void buildGuestPane() {
		ContentPanel cp = new ContentPanel();
		cp.setAnimCollapse(true);
		cp.setHeading(M.menu.guestPanel());
		cp.setIcon(AbstractImagePrototype.create(Images.menu.guestPanel()));
		cp.setLayout(new FitLayout());

		VerticalPanel guestpanel = new VerticalPanel();

		guestpanel.add(getMenuItem(M.menu.itemHelloToAll(), "start",
				Images.menu.icoInfo()));

		guestpanel.add(getMenuItem(M.menu.itemLicense(), "license",
				Images.menu.icoCopyright()));

		cp.add(guestpanel);
		panel.add(cp);

	}

	/**
	 * Builds the manager pane.
	 */
	private void buildManagerPane() {
		if (role.isAdmin() || role.isManager()) {
			ContentPanel cp = new ContentPanel();
			cp.setAnimCollapse(true);
			cp.setHeading(M.menu.managerPanel());
			cp.setIcon(AbstractImagePrototype.create(Images.menu.managerPanel()));
			cp.setLayout(new FitLayout());

			VerticalPanel managerpanel = new VerticalPanel();

			managerpanel.add(getMenuItem(M.menu.itemPaymethod(), "paymethod",
					Images.menu.icoPaymethod()));

			managerpanel.add(getMenuItem(M.menu.itemPartners(), "partners",
					Images.menu.icoPartners()));

			managerpanel.add(getMenuItem(M.menu.itemTarif(), "tarifs",
					Images.menu.icoTarif()));

			managerpanel.add(getMenuItem(M.menu.itemCars(), "cars",
					Images.menu.icoCar()));

			managerpanel.add(getMenuItem(M.menu.itemDrivers(), "drivers",
					Images.menu.icoMan()));

			cp.add(managerpanel);
			panel.add(cp);
		}
	}

	/**
	 * Clear.
	 */
	public void clear() {
		panel.removeAll();
		ContentPanel cp = new ContentPanel();
		panel.add(cp); // hack for hidden first panel
		cp.hide();
		role = null;
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

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

import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * The Class RolesWidget.
 * 
 * @author alexript
 */
public class RolesWidget extends Composite {

	/** The panel. */
	private final HorizontalPanel panel;

	/** The images. */
	private static RolesImages images = null;

	/** The img guest. */
	private static Image imgGuest;

	/** The img admin. */
	private static Image imgAdmin;

	/** The img documents. */
	private static Image imgDocuments;

	/** The img finances. */
	private static Image imgFinances;

	/** The img manager. */
	private static Image imgManager;

	/** The l. */
	private static UsersMessages l;

	/**
	 * Instantiates a new roles widget.
	 * 
	 * @param role
	 *            the role
	 */
	public RolesWidget(UserRole role) {

		chkResources();

		panel = new HorizontalPanel();
		panel.setWidth("86px");
		panel.setSpacing(1);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		drawIcons(role);

		initWidget(panel);
	}

	/**
	 * Chk resources.
	 */
	private void chkResources() {
		if (images == null) {
			l = GWT.create(UsersMessages.class);
			images = GWT.create(RolesImages.class);
			imgGuest = new Image(images.isGuest());
			imgGuest.setTitle(l.isguest());
			imgGuest.setAltText(l.isguest());

			imgAdmin = new Image(images.isAdmin());
			imgAdmin.setTitle(l.isadmin());
			imgAdmin.setAltText(l.isadmin());

			imgDocuments = new Image(images.isDocuments());
			imgDocuments.setTitle(l.isdocuments());
			imgDocuments.setAltText(l.isdocuments());

			imgFinances = new Image(images.isFinances());
			imgFinances.setTitle(l.isfinances());
			imgFinances.setAltText(l.isfinances());

			imgManager = new Image(images.isManager());
			imgManager.setTitle(l.ismanager());
			imgManager.setAltText(l.ismanager());
		}
	}

	/**
	 * Draw icons.
	 * 
	 * @param role
	 *            the role
	 */
	private void drawIcons(UserRole role) {
		if (role.isGuest()) {
			panel.add(imgGuest);
		}

		if (role.isAdmin()) {
			panel.add(imgAdmin);
		}

		if (role.isDocuments()) {
			panel.add(imgDocuments);
		}

		if (role.isFinances()) {
			panel.add(imgFinances);
		}

		if (role.isManager()) {
			panel.add(imgManager);
		}
	}
}

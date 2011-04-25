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

import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;

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

	/** The img guest. */
	private Image imgGuest;

	/** The img admin. */
	private Image imgAdmin;

	/** The img documents. */
	private Image imgDocuments;

	/** The img finances. */
	private Image imgFinances;

	/** The img manager. */
	private Image imgManager;

	/**
	 * Instantiates a new roles widget.
	 * 
	 * @param role
	 *            the role
	 */
	public RolesWidget(UserRole role) {

		chkResources();

		panel = new HorizontalPanel();

		panel.setSpacing(1);
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		drawIcons(role);

		initWidget(panel);
	}

	/**
	 * Chk resources.
	 */
	private void chkResources() {

		imgGuest = new Image(Images.roles.isGuest());
		imgGuest.setTitle(M.users.isguest());
		imgGuest.setAltText(M.users.isguest());

		imgAdmin = new Image(Images.roles.isAdmin());
		imgAdmin.setTitle(M.users.isadmin());
		imgAdmin.setAltText(M.users.isadmin());

		imgDocuments = new Image(Images.roles.isDocuments());
		imgDocuments.setTitle(M.users.isdocuments());
		imgDocuments.setAltText(M.users.isdocuments());

		imgFinances = new Image(Images.roles.isFinances());
		imgFinances.setTitle(M.users.isfinances());
		imgFinances.setAltText(M.users.isfinances());

		imgManager = new Image(Images.roles.isManager());
		imgManager.setTitle(M.users.ismanager());
		imgManager.setAltText(M.users.ismanager());

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

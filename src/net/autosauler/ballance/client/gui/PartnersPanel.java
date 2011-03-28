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

import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PartnersPanel.
 * 
 * @author alexript
 */
public class PartnersPanel extends CatalogPanel implements IPaneWithMenu,
		IDialogYesReceiver {

	private static MenuImages images = GWT.create(MenuImages.class);
	private TextBox email;

	public PartnersPanel() {
		super("partners", new Image(images.icoPartners()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.CatalogPanel#buildEditor(com.google
	 * .gwt.user.client.ui.VerticalPanel)
	 */
	@Override
	void buildEditor(VerticalPanel panel) {
		HorizontalPanel p = new HorizontalPanel();
		p.add(new Label("email"));
		email = new TextBox();
		p.add(email);
		panel.add(p);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.CatalogPanel#buildListRow(java.util
	 * .HashMap)
	 */
	@Override
	Widget buildListRow(HashMap<String, Object> map) {
		return new Label((String) map.get("email"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.CatalogPanel#canCreate(net.autosauler
	 * .ballance.shared.UserRole)
	 */
	@Override
	boolean canCreate(UserRole role) {
		return role.isAdmin() || role.isManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.CatalogPanel#canEdit(net.autosauler
	 * .ballance.shared.UserRole)
	 */
	@Override
	boolean canEdit(UserRole role) {
		return role.isAdmin() || role.isManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.CatalogPanel#canTrash(net.autosauler
	 * .ballance.shared.UserRole)
	 */
	@Override
	boolean canTrash(UserRole role) {
		return role.isAdmin() || role.isManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.CatalogPanel#cleanEditForm()
	 */
	@Override
	void cleanEditForm() {
		email.setText("");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.CatalogPanel#fillEditorForm(java.util
	 * .HashMap)
	 */
	@Override
	void fillEditorForm(HashMap<String, Object> map) {
		email.setText((String) map.get("email"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.CatalogPanel#getEditorValues()
	 */
	@Override
	HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", email.getText().trim());
		return map;
	}

}

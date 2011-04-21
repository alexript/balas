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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author alexript
 * 
 */
public class TarifPanel extends CatalogPanel implements IPaneWithMenu,
		IDialogYesReceiver {

	private TextBox script;

	public TarifPanel() {
		super("tarifs", new Image(images.icoTarif()));
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

		p.add(new Label("Script"));
		script = new TextBox(); // TODO: big field
		p.add(script);
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

		return null;
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

		return (role.isAdmin() || role.isFinances());
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
		return (role.isAdmin() || role.isFinances());

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
		return (role.isAdmin() || role.isFinances());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.CatalogPanel#cleanEditForm()
	 */
	@Override
	void cleanEditForm() {
		script.setText(""); // TODO: default script

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
		script.setText((String) map.get("scrpt"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.CatalogPanel#getEditorValues()
	 */
	@Override
	HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scrpt", script.getText());
		return map;
	}

}

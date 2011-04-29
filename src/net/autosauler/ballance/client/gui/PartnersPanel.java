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

import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PartnersPanel.
 * 
 * @author alexript
 */
public class PartnersPanel extends CatalogPanel implements IPaneWithMenu,
		IDialogYesReceiver {

	private HeaderField email;
	private HeaderField paymethod;
	private HeaderField currency;
	private HeaderField tarif;

	public PartnersPanel() {
		super("partners", new Image(Images.menu.icoPartners()));
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

		email = DataTypeFactory.addField(M.partners.lblEmail(), "email",
				DataTypes.DT_STRING, "", null);
		panel.add(email);

		paymethod = DataTypeFactory.addField(M.partners.lblPaymethod(),
				"paymethod", DataTypes.DT_CATALOGRECORD, null,
				new PayMethodPanel());
		panel.add(paymethod);

		currency = DataTypeFactory.addField(M.partners.lblCurrency(),
				"currency", DataTypes.DT_CURRENCY, "RUR", null);
		panel.add(currency);

		tarif = DataTypeFactory.addField(M.partners.lblTarif(), "tarif",
				DataTypes.DT_CATALOGRECORD, null, new TarifPanel());
		panel.add(tarif);

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
		email.reset();
		paymethod.reset();
		currency.reset();
		tarif.reset();
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
		email.setValue(map.get("email"));
		paymethod.setValue(map.get("paymethod"));
		currency.setValue(map.get("currency"));
		tarif.setValue(map.get("tarif"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.CatalogPanel#getEditorValues()
	 */
	@Override
	HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", ((String) email.getValue()).trim());
		map.put("paymethod", paymethod.getValue());
		map.put("currency", currency.getValue());
		map.put("tarif", tarif.getValue());
		return map;
	}

}

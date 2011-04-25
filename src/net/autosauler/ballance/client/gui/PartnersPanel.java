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

import net.autosauler.ballance.client.gui.messages.PartnersMessages;
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

	private TextBox email;
	private CatalogSelector paymethod;
	private static final PartnersMessages l = GWT
			.create(PartnersMessages.class);
	private static PayMethodPanel methods = new PayMethodPanel();
	private CurrencySelector currency;
	private static TarifPanel tarifs = new TarifPanel();
	private CatalogSelector tarif;

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

		p.add(new Label(l.lblEmail()));
		email = new TextBox();
		p.add(email);
		panel.add(p);

		p = new HorizontalPanel();
		p.add(new Label(l.lblPaymethod()));
		paymethod = methods.getSelectBox(null);
		p.add(paymethod);
		panel.add(p);

		p = new HorizontalPanel();
		p.add(new Label(l.lblCurrency()));
		currency = new CurrencySelector(null);
		p.add(currency);
		panel.add(p);

		p = new HorizontalPanel();
		p.add(new Label(l.lblTarif()));
		tarif = tarifs.getSelectBox(null);
		p.add(tarif);
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
		email.setText((String) map.get("email"));
		paymethod.select((Long) map.get("paymethod"));
		currency.select((String) map.get("currency"));
		tarif.select((Long) map.get("tarif"));
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
		map.put("paymethod", paymethod.getValue());
		map.put("currency", currency.getValue());
		map.put("tarif", tarif.getValue());
		return map;
	}

}

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

import java.util.Date;
import java.util.HashMap;

import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class IncomingGoodsPanel.
 * 
 * @author alexript
 */
public class IncomingGoodsPanel extends DocumentPanel implements IPaneWithMenu {

	/** The pp. */
	private static PartnersPanel pp = new PartnersPanel();

	/**
	 * Instantiates a new incoming goods panel.
	 */
	public IncomingGoodsPanel() {
		super("ingoods", new Image(Images.menu.icoInGoods()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#canActivate(net.autosauler
	 * .ballance.shared.UserRole)
	 */
	@Override
	boolean canActivate(UserRole role) {
		return role.isAdmin() || role.isDocuments() || role.isFinances();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#canCreate(net.autosauler
	 * .ballance.shared.UserRole)
	 */
	@Override
	boolean canCreate(UserRole role) {

		return role.isAdmin() || role.isDocuments() || role.isFinances();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#canEdit(net.autosauler
	 * .ballance.shared.UserRole)
	 */
	@Override
	boolean canEdit(UserRole role) {

		return role.isAdmin() || role.isDocuments() || role.isFinances();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#createStructure()
	 */
	@Override
	protected void createStructure() {
		addField(M.incominggoods.lblPartner(), "partner",
				DataTypes.DT_CATALOGRECORD, null, pp);

		addField(M.incominggoods.lblSendDate(), "senddate", DataTypes.DT_DATE,
				(new Date()).getTime(), null);

		addField(M.incominggoods.lblGoodsNum(), "gnum", DataTypes.DT_INT, "0",
				null);

		addField(M.incominggoods.lblInvoice(), "invoice", DataTypes.DT_STRING,
				"", null);

		addField(M.incominggoods.lblAWB(), "awb", DataTypes.DT_STRING, "", null);

		addField(M.incominggoods.lblGTD(), "gtd", DataTypes.DT_STRING, "", null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#drawDocumentRowForList
	 * (java.util.HashMap)
	 */
	@Override
	protected String drawDocumentRowForList(HashMap<String, Object> map) {
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setSpacing(2);

		HorizontalPanel row = new HorizontalPanel();
		row.setSpacing(4);
		row.add(new Label(pp.getName((Long) map.get("partner"))));
		row.add(new Label(DataTypeFactory.dateFormat.format(new Date((Long) map
				.get("senddate"))) + " " + map.get("gnum")));
		panel.add(row);

		return panel.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#hasTablePart()
	 */
	@Override
	protected boolean hasTablePart() {

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#initTableParts(net.
	 * autosauler.ballance.client.gui.DocumentTableParts)
	 */
	@Override
	protected void initTableParts(final DocumentTableParts parts) {
		GoodsTablePart part = new GoodsTablePart(M.incominggoods.tableGoods());
		parts.addPart("goods", part);

		AdditionalPaysTablePart part2 = new AdditionalPaysTablePart(
				M.incominggoods.tableAddPays());
		parts.addPart("goodsaddpay", part2);

	}

}

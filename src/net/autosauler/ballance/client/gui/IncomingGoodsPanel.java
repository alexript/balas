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
import net.autosauler.ballance.client.gui.messages.IncomingGoodsMessages;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * The Class IncomingGoodsPanel.
 * 
 * @author alexript
 */
public class IncomingGoodsPanel extends DocumentPanel implements IPaneWithMenu {

	/** The l. */
	private static IncomingGoodsMessages l = GWT
			.create(IncomingGoodsMessages.class);

	/** The date format. */
	private static DateTimeFormat dateFormat = DateTimeFormat
			.getFormat("yyyy/MM/dd");

	/** The pp. */
	private static PartnersPanel pp = new PartnersPanel();

	/** The partner. */
	private CatalogSelector partner;

	/** The senddate. */
	private DateBox senddate;

	/** The goodsnumber. */
	private TextBox goodsnumber;

	/** The invoicenum. */
	private TextBox invoicenum;

	/** The awb. */
	private TextBox awb;

	/** The gtd. */
	private TextBox gtd;

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
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#cleanEditForm()
	 */
	@Override
	void cleanEditForm() {
		partner.reset();
		senddate.setValue(new Date());
		goodsnumber.setText("");
		invoicenum.setText("");
		awb.setText("");
		gtd.setText("");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#createDocumentHeaderEditor
	 * ()
	 */
	@Override
	protected Widget createDocumentHeaderEditor() {
		Grid header = new Grid(6, 2);

		header.setWidget(0, 0, new Label(l.lblPartner()));

		partner = pp.getSelectBox(0L);
		header.setWidget(0, 1, partner);

		header.setWidget(1, 0, new Label(l.lblSendDate()));
		senddate = new DateBox();
		senddate.setFormat(new DateBox.DefaultFormat(dateFormat));
		header.setWidget(1, 1, senddate);

		header.setWidget(2, 0, new Label(l.lblGoodsNum()));
		goodsnumber = new TextBox();
		header.setWidget(2, 1, goodsnumber);

		header.setWidget(3, 0, new Label(l.lblInvoice()));
		invoicenum = new TextBox();
		header.setWidget(3, 1, invoicenum);

		header.setWidget(4, 0, new Label(l.lblAWB()));
		awb = new TextBox();
		header.setWidget(4, 1, awb);

		header.setWidget(5, 0, new Label(l.lblGTD()));
		gtd = new TextBox();
		header.setWidget(5, 1, gtd);

		return header;
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
		row.add(new Label(
				dateFormat.format(new Date((Long) map.get("senddate"))) + " "
						+ map.get("gnum")));
		panel.add(row);

		return panel.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#fillEditor(java.util
	 * .HashMap)
	 */
	@Override
	protected void fillEditor(HashMap<String, Object> map) {
		partner.select((Long) map.get("partner"));
		senddate.setValue(new Date((Long) map.get("senddate")));

		goodsnumber.setText((String) map.get("gnum"));
		invoicenum.setText((String) map.get("invoice"));
		awb.setText((String) map.get("awb"));
		gtd.setText((String) map.get("gtd"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#getEditorValues()
	 */
	@Override
	protected HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("partner", partner.getValue());

		Date d = senddate.getValue();
		map.put("senddate", d.getTime());

		map.put("gnum", goodsnumber.getText());

		map.put("invoice", invoicenum.getText());

		map.put("awb", awb.getText());
		map.put("gtd", gtd.getText());

		return map;
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
		GoodsTablePart part = new GoodsTablePart(l.tableGoods());
		parts.addPart("goods", part);

		AdditionalPaysTablePart part2 = new AdditionalPaysTablePart(
				l.tableAddPays());
		parts.addPart("goodsaddpay", part2);

	}

}

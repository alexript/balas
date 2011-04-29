/**
 * 
 */
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
 * The Class IncomingPayPanel.
 * 
 * @author alexript
 */
public class IncomingPayPanel extends DocumentPanel implements IPaneWithMenu {

	/** The pp. */
	private static PartnersPanel pp = new PartnersPanel();
	/** The pmp. */
	private static PayMethodPanel pmp = new PayMethodPanel();

	/**
	 * Instantiates a new incoming pay panel.
	 * 
	 */
	public IncomingPayPanel() {
		super("inpay", new Image(Images.menu.icoIncPay()));
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

		return role.isAdmin() || role.isFinances();
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

		return role.isAdmin() || role.isFinances();
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

		return role.isAdmin() || role.isFinances();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#createStructure()
	 */
	@Override
	protected void createStructure() {

		addField(M.incomingpay.lblPartner(), "partner",
				DataTypes.DT_CATALOGRECORD, 0L, pp);

		addField(M.incomingpay.lblPayDate(), "paydate", DataTypes.DT_DATE,
				(new Date()).getTime(), null);

		addField(M.incomingpay.lblCurrency(), "currency",
				DataTypes.DT_CURRENCY, "RUR", null);

		addField(M.incomingpay.lblValue(), "payvalue", DataTypes.DT_MONEY,
				"0.0", null);

		addField(M.incomingpay.lblMethod(), "paymethod",
				DataTypes.DT_CATALOGRECORD, 0L, pmp);

		addField(M.incomingpay.lblComments(), "comments", DataTypes.DT_STRING,
				"", null);

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
		row.add(new Label(map.get("payvalue") + " "
				+ (String) map.get("currency")));
		panel.add(row);

		panel.add(new Label(pmp.getName((Long) map.get("paymethod"))));

		panel.add(new Label((String) map.get("comments")));

		return panel.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#hasTablePart()
	 */
	@Override
	protected boolean hasTablePart() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#initTableParts(net.
	 * autosauler.ballance.client.gui.DocumentTableParts)
	 */
	@Override
	protected void initTableParts(DocumentTableParts parts) {
		return;

	}

}

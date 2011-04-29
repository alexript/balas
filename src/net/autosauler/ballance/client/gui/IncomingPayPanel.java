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
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class IncomingPayPanel.
 * 
 * @author alexript
 */
public class IncomingPayPanel extends DocumentPanel implements IPaneWithMenu {

	/** The partner. */
	private HeaderField partner;

	/** The paydate. */
	private HeaderField paydate;

	/** The currency. */
	private HeaderField currency;

	/** The payvalue. */
	private HeaderField payvalue;

	/** The paymethod. */
	private HeaderField paymethod;

	/** The comments. */
	private HeaderField comments;

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
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#cleanEditForm()
	 */
	@Override
	void cleanEditForm() {
		partner.reset();
		paydate.reset();
		currency.reset();
		payvalue.reset();
		paymethod.reset();
		comments.reset();

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
		VerticalPanel p = new VerticalPanel();
		partner = DataTypeFactory.addField(M.incomingpay.lblPartner(),
				"partner", DataTypes.DT_CATALOGRECORD, 0L, pp);
		p.add(partner);

		paydate = DataTypeFactory.addField(M.incomingpay.lblPayDate(),
				"paydate", DataTypes.DT_DATE, (new Date()).getTime(), null);
		p.add(paydate);

		currency = DataTypeFactory.addField(M.incomingpay.lblCurrency(),
				"currency", DataTypes.DT_CURRENCY, "RUR", null);
		p.add(currency);

		payvalue = DataTypeFactory.addField(M.incomingpay.lblValue(),
				"payvalue", DataTypes.DT_MONEY, "0.0", null);
		p.add(payvalue);

		paymethod = DataTypeFactory.addField(M.incomingpay.lblMethod(),
				"paymethod", DataTypes.DT_CATALOGRECORD, 0L, pmp);
		p.add(paymethod);

		comments = DataTypeFactory.addField(M.incomingpay.lblComments(),
				"comments", DataTypes.DT_STRING, "", null);
		p.add(comments);

		return p;
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
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentPanel#fillEditor(java.lang
	 * .Long)
	 */
	@Override
	protected void fillEditor(HashMap<String, Object> map) {
		partner.setValue(map.get("partner"));
		paydate.setValue(map.get("paydate"));
		currency.setValue(map.get("currency"));
		payvalue.setValue(map.get("payvalue"));
		paymethod.setValue(map.get("paymethod"));
		comments.setValue(map.get("comments"));

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
		map.put("paydate", paydate.getValue());
		map.put("currency", currency.getValue());
		map.put("payvalue", payvalue.getValue());
		map.put("paymethod", paymethod.getValue());
		map.put("comments", comments.getValue());

		return map;
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

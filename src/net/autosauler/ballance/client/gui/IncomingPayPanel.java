/**
 * 
 */
package net.autosauler.ballance.client.gui;

import java.util.Date;
import java.util.HashMap;

import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
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
 * The Class IncomingPayPanel.
 * 
 * @author alexript
 */
public class IncomingPayPanel extends DocumentPanel implements IPaneWithMenu,
		IDialogYesReceiver {

	/** The partner. */
	private CatalogSelector partner;

	/** The date format. */
	private static DateTimeFormat dateFormat = DateTimeFormat
			.getFormat("dd/MM/yyyy");

	/** The paydate. */
	private DateBox paydate;

	/** The currency. */
	private CurrencySelector currency;

	/** The payvalue. */
	private TextBox payvalue;

	/** The paymethod. */
	private CatalogSelector paymethod;

	/** The comments. */
	private TextBox comments;

	private static IncomingPayMessages l = GWT
			.create(IncomingPayMessages.class);

	private static PartnersPanel pp = new PartnersPanel();
	private static PayMethodPanel pmp = new PayMethodPanel();

	/**
	 * Instantiates a new incoming pay panel.
	 * 
	 */
	public IncomingPayPanel() {
		super("inpay", new Image(images.icoIncPay()));
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
		paydate.setValue(new Date());

		currency.reset();
		payvalue.setText("0.0");
		paymethod.reset();
		comments.setText("");

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

		header.setWidget(1, 0, new Label(l.lblPayDate()));
		paydate = new DateBox();
		paydate.setFormat(new DateBox.DefaultFormat(dateFormat));
		header.setWidget(1, 1, paydate);

		header.setWidget(2, 0, new Label(l.lblCurrency()));
		currency = new CurrencySelector(null);
		header.setWidget(2, 1, currency);

		header.setWidget(3, 0, new Label(l.lblValue()));
		payvalue = new TextBox();
		header.setWidget(3, 1, payvalue);

		header.setWidget(4, 0, new Label(l.lblMethod()));

		paymethod = pmp.getSelectBox(0L);
		header.setWidget(4, 1, paymethod);

		header.setWidget(5, 0, new Label(l.lblComments()));
		comments = new TextBox();
		header.setWidget(5, 1, comments);

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
	 * @see net.autosauler.ballance.client.gui.DocumentPanel#getEditorValues()
	 */
	@Override
	protected HashMap<String, Object> getEditorValues() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("partner", partner.getValue());

		Date d = paydate.getValue();

		map.put("paydate", d.getTime());

		map.put("currency", currency.getValue());

		String val = payvalue.getText();
		val = val.trim().replace(',', '.');
		Double payvalue = new Double(0.0D);
		try {
			payvalue = Double.parseDouble(val);
		} catch (Exception e) {
			Log.error(e.getMessage());
		}
		map.put("payvalue", payvalue.toString());

		map.put("paymethod", paymethod.getValue());
		map.put("comments", comments.getText());

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

}

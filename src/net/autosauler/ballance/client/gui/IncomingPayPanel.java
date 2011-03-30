/**
 * 
 */
package net.autosauler.ballance.client.gui;

import java.util.Date;
import java.util.HashMap;

import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author alexript
 * 
 */
public class IncomingPayPanel extends DocumentPanel implements IPaneWithMenu,
		IDialogYesReceiver {

	private CatalogSelector partner;

	private static DateTimeFormat dateFormat = DateTimeFormat
			.getFormat("dd/MM/yyyy");

	private DateBox paydate;
	private CurrencySelector currency;
	private TextBox payvalue;

	/**
	 * @param docname
	 * @param image
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
		Grid header = new Grid(4, 2);

		header.setWidget(0, 0, new Label("Partner"));
		PartnersPanel pp = new PartnersPanel();
		partner = pp.getSelectBox(0L);
		header.setWidget(0, 1, partner);

		header.setWidget(1, 0, new Label("Pay date"));
		paydate = new DateBox();
		paydate.setFormat(new DateBox.DefaultFormat(dateFormat));
		header.setWidget(1, 1, paydate);

		header.setWidget(2, 0, new Label("Currency"));
		currency = new CurrencySelector(null);
		header.setWidget(2, 1, currency);

		header.setWidget(3, 0, new Label("Value"));
		payvalue = new TextBox();
		header.setWidget(3, 1, payvalue);

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
		SafeHtmlBuilder sb = new SafeHtmlBuilder();

		// TODO:
		sb.appendHtmlConstant(new Label("Partner is " + map.get("partner"))
				.toString());

		// TODO: fix this shit
		return sb.toString();
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

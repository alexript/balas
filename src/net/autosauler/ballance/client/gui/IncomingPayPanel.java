/**
 * 
 */
package net.autosauler.ballance.client.gui;

import java.util.HashMap;

import net.autosauler.ballance.client.gui.images.Images;

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

}

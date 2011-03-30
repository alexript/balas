/**
 * 
 */
package net.autosauler.ballance.client.gui;

import java.util.HashMap;

import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.client.ui.Image;

/**
 * @author alexript
 * 
 */
public class IncomingPayPanel extends DocumentPanel implements IPaneWithMenu,
		IDialogYesReceiver {

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
		// TODO Auto-generated method stub

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
		StringBuilder sb = new StringBuilder();

		// TODO:
		sb.append("Partner is " + map.get("partner"));

		return sb.toString();
	}

}

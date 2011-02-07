package net.autosauler.ballance.client.gui;

import com.google.gwt.i18n.client.Messages;

/**
 * The Interface AuthMessages (localization methods).
 */
public interface AuthMessages extends Messages {
	String helloUserMsg(String username);
	String btnLogout();
	String labelLogin();
	String labelPssword();
	String btnLogin();
	String btnCancel();
	String qtnLogout();
	String btnYes();
	String btnNo();
	String badFieldValue();
	String badAuth();
	String commError();
}

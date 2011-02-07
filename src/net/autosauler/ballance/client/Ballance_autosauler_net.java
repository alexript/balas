package net.autosauler.ballance.client;

import net.autosauler.ballance.client.gui.MainPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ballance_autosauler_net implements EntryPoint {

	/** The isloggedin. */
	static private boolean isloggedin = false;
	private MainPanel mainpanel = new MainPanel();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get().add(mainpanel);
		RootPanel.get("spinner").setVisible(false);
	}
	
	/**
	 * Checks if is logged in.
	 *
	 * @return true, if is logged in
	 */
	public static boolean isLoggedIn() {
		return isloggedin;
	}
}

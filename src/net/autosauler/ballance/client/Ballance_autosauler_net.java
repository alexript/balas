package net.autosauler.ballance.client;

import net.autosauler.ballance.client.gui.MainPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ballance_autosauler_net implements EntryPoint {

	/** The isloggedin. */
	static private boolean isloggedin = false;
	
	/** The main panel. */
	public static MainPanel mainpanel = new MainPanel();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		mainpanel.setWidth("100%");
		mainpanel.setHeight(Window.getClientHeight() + "px");
		   Window.addResizeHandler(new ResizeHandler() {
		     public void onResize(ResizeEvent event) {
		       mainpanel.setHeight(event.getHeight() + "px");
		       mainpanel.setWidth(event.getWidth() + "px");
		     }
		   });
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

package net.autosauler.ballance.client;

import net.autosauler.ballance.client.gui.MainPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ballance_autosauler_net implements EntryPoint {

	/** The isloggedin. */
	static private boolean isloggedin = false;
	
	/** The main panel. */
	public static MainPanel mainpanel = new MainPanel();
	
	/** The auth service. */
	public static AuthServiceAsync authService = (AuthServiceAsync) GWT.create(AuthService.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		authService.isSessionAuth(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				isloggedin = false;
			}

			@Override
			public void onSuccess(Boolean result) {
				isloggedin = result;
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
		});
		
	}
	
	/**
	 * Checks if is logged in.
	 *
	 * @return true, if is logged in
	 */
	public static boolean isLoggedIn() {
		return isloggedin;
	}
	
	/**
	 * Sets the logged in state.
	 *
	 * @param f the new logged in state
	 */
	public static void setLoggedInState(boolean f) {
		isloggedin = f;
	}
}

/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client;

import net.autosauler.ballance.client.gui.MainPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ballance_autosauler_net implements EntryPoint {

	/** The Constant sessionId. */
	public static final SessionId sessionId = new SessionId();

	/** The isloggedin. */
	static private boolean isloggedin = false;

	/** The main panel. */
	public static MainPanel mainpanel = null;

	/** The auth service. */
	public static AuthServiceAsync authService = (AuthServiceAsync) GWT
			.create(AuthService.class);

	{
		sessionId.setSessionId(Cookies.getCookie("session"));
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		AsyncCallback<SessionId> asyncCallback = new AsyncCallback<SessionId>() {
			public void onFailure(Throwable caught) {
				isloggedin = false;
			}// end onFailure

			public void onSuccess(SessionId result) {
				// Window.alert(sessionId.getSessionId() + " --- " +
				// result.getSessionId() + ": " +
				// sessionId.getSessionId().equals(
				// result.getSessionId()));
				if (result == null
						|| !sessionId.getSessionId().equals(
								result.getSessionId())) {
					isloggedin = false;
				} else if (sessionId.getSessionId().equals(
						result.getSessionId())) {
					isloggedin = true;
					sessionId.setSession(result);
				}

				mainpanel = new MainPanel();

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

			}// end onSucess
		};// end AsyncCallback<String> asyncCallback = new
			// AsyncCallback<String>()
		authService.session(sessionId, asyncCallback);

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
	 * @param f
	 *            the new logged in state
	 */
	public static void setLoggedInState(boolean f) {
		isloggedin = f;
	}
}

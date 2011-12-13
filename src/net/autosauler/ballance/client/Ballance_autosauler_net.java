/*******************************************************************************
 * Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package net.autosauler.ballance.client;

import java.util.Date;

import net.autosauler.ballance.client.databases.StructureFactory;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.AuthPanel;
import net.autosauler.ballance.client.gui.LeftMenu;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.client.gui.TopPanel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
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

	/** The Constant COOKIE_TIME. */
	public final static long COOKIE_TIME = 1000 * 60 * 60;

	private static AuthPanel authPanel;
	public static LeftMenu menu;
	public static Status state;

	/**
	 * Checks if is logged in.
	 * 
	 * @return true, if is logged in
	 */
	public static boolean isLoggedIn() {
		return isloggedin;
	}

	/**
	 * Logout sequence.
	 */
	public static void logoutSequence() {
		Ballance_autosauler_net.setLoggedInState(false);
		Ballance_autosauler_net.sessionId.reset();

		Cookies.setCookie("session", "", new Date(System.currentTimeMillis()
				+ COOKIE_TIME));
		authPanel.logoffAction();
		MainPanel.dropMainPane();
		History.newItem("start");
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

	private Viewport viewport;

	{
		sessionId.setSessionId(Cookies.getCookie("session"));
	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {

		AsyncCallback<SessionId> asyncCallback = new AsyncCallback<SessionId>() {
			@Override
			public void onFailure(Throwable caught) {
				isloggedin = false;
				new AlertDialog(caught).show();
			}// end onFailure

			@Override
			public void onSuccess(SessionId result) {
				// Window.alert(sessionId.getSessionId() + " --- " +
				// result.getSessionId() + ": " +
				// sessionId.getSessionId().equals(
				// result.getSessionId()));
				if ((result == null)
						|| !sessionId.getSessionId().equals(
								result.getSessionId())) {
					isloggedin = false;
				} else if (sessionId.getSessionId().equals(
						result.getSessionId())) {
					isloggedin = true;
					sessionId.setSession(result);
				}

				viewport = new Viewport();
				viewport.setLayout(new BorderLayout());

				TopPanel toppanel = new TopPanel();
				viewport.add(toppanel, new BorderLayoutData(LayoutRegion.NORTH,
						32));
				toppanel.startAnimation();

				ContentPanel leftPanel = new ContentPanel();
				leftPanel.setLayout(new VBoxLayout());

				authPanel = new AuthPanel("BalAS", leftPanel);

				menu = new LeftMenu(leftPanel);

				BorderLayoutData westData = new BorderLayoutData(
						LayoutRegion.WEST, 242, 242, 300);
				westData.setMargins(new Margins(5, 5, 5, 5));
				westData.setSplit(true);
				westData.setCollapsible(true);

				viewport.add(leftPanel, westData);

				TabPanel mainpane = new TabPanel();
				mainpane.setAnimScroll(true);
				mainpane.setTabScroll(true);
				mainpane.setCloseContextMenu(true);

				viewport.add(mainpane,
						new BorderLayoutData(LayoutRegion.CENTER));
				mainpanel = new MainPanel(mainpane);

				state = new Status();
				state.setBox(true);
				viewport.add(state,
						new BorderLayoutData(LayoutRegion.SOUTH, 30));

				RootPanel.get().add(viewport);
				RootPanel.get("spinner").setVisible(false);
				StructureFactory.loadData();

			}// end onSucess
		};
		Services.auth.session(sessionId, asyncCallback);

	}
}

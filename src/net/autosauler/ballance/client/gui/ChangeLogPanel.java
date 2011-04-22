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

package net.autosauler.ballance.client.gui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;

/**
 * The Class ChangeLogPanel.
 * 
 * @author alexript
 */
public class ChangeLogPanel extends Composite implements IPaneWithMenu {

	/** The impl. */
	private static ChangeLogPanel impl = null;

	/** The d. */
	private static DecoratorPanel d;

	/**
	 * Gets the.
	 * 
	 * @return the license panel
	 */
	public static ChangeLogPanel get() {
		if (impl == null) {
			impl = new ChangeLogPanel();
		}
		return impl;
	}

	/**
	 * Instantiates a new change log panel.
	 */
	private ChangeLogPanel() {

		MainPanel.setCommInfo(true);
		try {
			new RequestBuilder(RequestBuilder.GET, "CHANGELOG").sendRequest("",
					new RequestCallback() {
						@Override
						public void onError(Request res, Throwable throwable) {
							MainPanel.setCommInfo(false);
							Log.error(throwable.getMessage());
						}

						@Override
						public void onResponseReceived(Request request,
								Response response) {
							MainPanel.setCommInfo(false);
							String text = response.getText();
							HTML w = new HTML("<p>" + text + "</p>");
							d.setWidget(w);

						}
					});
		} catch (RequestException e) {
			MainPanel.setCommInfo(false);
			Log.error(e.getMessage());
		}

		d = new DecoratorPanel();
		d.setWidth("100%");

		initWidget(d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public MenuBar getPaneMenu() {

		return null;
	}

}

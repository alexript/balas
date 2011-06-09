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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;

/**
 * The Class TopPanel.
 * 
 * @author alexript
 */
public class TopPanel extends Composite {

	/** The panel. */
	private final HorizontalPanel panel;

	/** The title. */
	private final InlineHTML title;

	/**
	 * Instantiates a new top panel.
	 */
	public TopPanel() {
		panel = new HorizontalPanel();
		panel.setSpacing(3);
		panel.setWidth("100%");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		title = new InlineHTML(
				"<div id=\"topline_title\">Balance :: AutoSauler.net</div>");
		panel.add(title);
		panel.setCellHorizontalAlignment(title,
				HasHorizontalAlignment.ALIGN_LEFT);

		// add today currency values

		panel.add(new CurrencyTopPanel());

		// add language flags
		InlineHTML locales = new InlineHTML(
				"&nbsp;&nbsp;<a href=\"index.html?locale=ru\"><img src=\"flags/ru.gif\" style=\"border-style:none;\"/></a>&nbsp;<a href=\"index.html?locale=en\"><img src=\"flags/gb.gif\" style=\"border-style:none;\"/></a>&nbsp;");
		locales.setWidth("40px");
		panel.add(locales);
		panel.setCellWidth(locales, "50px");

		initWidget(panel);
		this.setStyleName("topPanel");

	}

	/**
	 * Start animation.
	 */
	public void startAnimation() {
		startTitleAnimation();
	}

	/**
	 * Start title animation.
	 * 
	 * @param element
	 *            the element
	 */
	private native void startTitleAnimation() /*-{
		var foo = this;
		$wnd
				.setTimeout(
						function() {
							new $wnd.Effect.Opacity('topline_title', {
								from : 1.0,
								to : 0.3,
								duration : 0.5,
								queue : 'end'
							});
							new $wnd.Effect.Opacity('topline_title', {
								from : 0.3,
								to : 1.0,
								duration : 0.5,
								queue : 'end'
							});

							foo.@net.autosauler.ballance.client.gui.TopPanel::startAnimation()();
						}, 5000);
	}-*/;
}

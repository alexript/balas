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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class AlertDialog.
 */
public class AlertDialog extends DialogBox {

	/** The l. */
	private DialogMessages l;

	/** The images. */
	private MenuImages images;

	/**
	 * Instantiates a new alert dialog.
	 * 
	 * @param message
	 *            the message
	 */
	public AlertDialog(String message) {
		initDialog(message, null);
	}

	/**
	 * Instantiates a new alert dialog.
	 * 
	 * @param message
	 *            the message
	 * @param additionalinfo
	 *            the additionalinfo
	 */
	public AlertDialog(String message, String additionalinfo) {
		initDialog(message, additionalinfo);
	}

	/**
	 * Inits the dialog.
	 * 
	 * @param message
	 *            the message
	 * @param additionalinfo
	 *            the additionalinfo
	 */
	private void initDialog(String message, String additionalinfo) {
		l = GWT.create(DialogMessages.class);
		images = (MenuImages) GWT.create(MenuImages.class);
		setText(l.msgAlertTitle());
		setAnimationEnabled(true);
		setGlassEnabled(true);

		Button ok = new Button(l.btnOk());

		ok.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AlertDialog.this.hide();
			}

		});

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("300px");

		HorizontalPanel qpanel = new HorizontalPanel();
		qpanel.setSpacing(15);
		qpanel.add(new Image(images.icoExclamation()));
		qpanel.add(new Label(message));
		vpanel.add(qpanel);

		if ((additionalinfo != null) && !additionalinfo.isEmpty()) {
			DisclosurePanel advancedDisclosure = new DisclosurePanel(
					l.msgAdditionalInfo());
			advancedDisclosure.setAnimationEnabled(true);
			TextArea textArea = new TextArea();
			textArea.setVisibleLines(10);
			textArea.setText(additionalinfo);
			textArea.setReadOnly(true);
			textArea.setStyleName("AdditionalInfoText");
			advancedDisclosure.setContent(textArea);
			advancedDisclosure.setWidth("100%");
			advancedDisclosure.addStyleName("LeftPadding6");
			vpanel.add(advancedDisclosure);

		}

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setWidth("100%");
		buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		buttons.add(ok);
		buttons.setSpacing(5);

		vpanel.add(buttons);

		setWidget(vpanel);

		ok.setFocus(true);

		setPopupPosition((Window.getClientWidth() / 2 - 150), 200);
	}
}

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

import net.autosauler.ballance.client.gui.images.Images;
import net.autosauler.ballance.client.gui.messages.M;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class AlertDialog.
 */
public class AlertDialog extends Dialog {

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
	 * Instantiates a new alert dialog.
	 * 
	 * @param caught
	 *            the caught
	 */
	public AlertDialog(Throwable caught) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : caught.getStackTrace()) {
			sb.append(element.toString());
			sb.append("\n");
		}
		initDialog(caught.getMessage(), sb.toString());
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

		setHeading(M.dialog.msgAlertTitle());
		setAnimCollapse(true);
		setAutoHeight(true);
		setAutoWidth(true);
		setBlinkModal(true);
		setClosable(false);
		setDraggable(true);
		setModal(true);
		setShadow(true);
		setButtons(Dialog.OK);
		setHideOnButtonClick(true);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setWidth("300px");

		HorizontalPanel qpanel = new HorizontalPanel();
		qpanel.setSpacing(15);
		qpanel.add(new Image(Images.menu.icoExclamation()));
		qpanel.add(new Label(message));
		vpanel.add(qpanel);

		if ((additionalinfo != null) && !additionalinfo.isEmpty()) {
			DisclosurePanel advancedDisclosure = new DisclosurePanel(
					M.dialog.msgAdditionalInfo());
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

		this.add(vpanel);

	}
}

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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class QuestionDialog.
 */
public class QuestionDialog extends Dialog {

	/** The question dialog receiver. */
	private final IDialogYesReceiver yesreceiver;

	/** The mytag. */
	private final String mytag;

	/** The mytag2. */
	private final Object mytag2;

	/**
	 * Instantiates a new question dialog.
	 * 
	 * @param question
	 *            the question
	 * @param receiver
	 *            the receiver
	 * @param tag
	 *            the tag
	 */
	public QuestionDialog(String question, IDialogYesReceiver receiver,
			String tag) {
		this(question, receiver, tag, null);
	}

	/**
	 * Instantiates a new question dialog.
	 * 
	 * @param question
	 *            the question
	 * @param receiver
	 *            the receiver
	 * @param tag
	 *            the tag
	 * @param tag2
	 *            the tag2
	 */
	public QuestionDialog(String question, IDialogYesReceiver receiver,
			String tag, Object tag2) {
		yesreceiver = receiver;
		mytag = tag;
		mytag2 = tag2;

		setHeading(M.dialog.msgTitle());
		setAnimCollapse(true);
		setAutoHeight(true);
		setAutoWidth(true);
		setBlinkModal(true);
		setClosable(false);
		setDraggable(true);
		setModal(true);
		setShadow(true);

		Button yes = new Button(M.dialog.btnYes());

		yes.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				yesreceiver.onDialogYesButtonClick(mytag, mytag2);
				QuestionDialog.this.hide();

			}

		});

		Button no = new Button(M.dialog.btnNo());
		no.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				QuestionDialog.this.hide();

			}

		});

		VerticalPanel vpanel = new VerticalPanel();

		HorizontalPanel qpanel = new HorizontalPanel();
		qpanel.setSpacing(15);
		qpanel.add(new Image(Images.menu.icoQuestion()));
		qpanel.add(new Label(question));
		vpanel.add(qpanel);

		this.add(vpanel);
		getButtonBar().removeAll();
		addButton(yes);
		addButton(no);

		no.focus();

	}
}

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

import net.autosauler.ballance.client.Ballance_autosauler_net;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class QuestionDialog.
 */
public class QuestionDialog extends DialogBox {

	/** The question dialog receiver. */
	private final IDialogYesReceiver yesreceiver;

	/** The l. */
	private final DialogMessages l;

	/** The mytag. */
	private final String mytag;

	/** The mytag2. */
	private final Object mytag2;

	/** The images. */
	private final MenuImages images;

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
		l = GWT.create(DialogMessages.class);
		images = (MenuImages) GWT.create(MenuImages.class);

		setText(l.msgTitle());
		setAnimationEnabled(true);
		setGlassEnabled(true);

		Button yes = new Button(l.btnYes());

		yes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				yesreceiver.onDialogYesButtonClick(mytag, mytag2);
				QuestionDialog.this.hide();
			}

		});

		Button no = new Button(l.btnNo());
		no.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				QuestionDialog.this.hide();

			}

		});

		VerticalPanel vpanel = new VerticalPanel();

		HorizontalPanel qpanel = new HorizontalPanel();
		qpanel.setSpacing(15);
		qpanel.add(new Image(images.icoQuestion()));
		qpanel.add(new Label(question));
		vpanel.add(qpanel);

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.setWidth("100%");
		buttons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel bcontainer = new HorizontalPanel();
		bcontainer.add(yes);
		bcontainer.add(no);
		bcontainer.setSpacing(5);
		buttons.add(bcontainer);

		vpanel.add(buttons);

		setWidget(vpanel);

		no.setFocus(true);

		setPopupPosition(
				(Ballance_autosauler_net.mainpanel.getOffsetWidth() / 2 - 150),
				200);
	}
}

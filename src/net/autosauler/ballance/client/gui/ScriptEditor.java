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

import net.autosauler.ballance.client.Services;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ScriptEditor.
 * 
 * @author alexript
 */
public class ScriptEditor extends DialogBox {

	/** The receiver. */
	private final IReloadMsgReceiver receiver;

	/** The scriptname. */
	private final String scriptname;

	/** The text. */
	private String text;

	/** The editor. */
	private ScriptArea editor;

	/**
	 * Instantiates a new script editor.
	 * 
	 * @param scriptname
	 *            the scriptname
	 * @param reloadreceiver
	 *            the reloadreceiver
	 */
	public ScriptEditor(final String scriptname,
			IReloadMsgReceiver reloadreceiver) {

		receiver = reloadreceiver;
		this.scriptname = scriptname;

		initGui();
		MainPanel.setCommInfo(true);
		Services.scripts.getScript(scriptname, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught).show();

			}

			@Override
			public void onSuccess(String result) {
				MainPanel.setCommInfo(false);
				if (result != null) {
					text = result;
					editor.setText(text);
					ScriptEditor.this.show();
				} else {
					new AlertDialog("Can't load script " + scriptname).show();
				}

			}
		});
	};

	/**
	 * Inits the gui.
	 */
	private void initGui() {
		setText("Script: " + scriptname);
		setAnimationEnabled(true);
		setGlassEnabled(true);

		editor = new ScriptArea("javascript");
		editor.setWidth("500px");
		editor.setHeight("400px");

		Button btnSave = new Button("Save");

		btnSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainPanel.setCommInfo(true);
				text = editor.getText();
				Services.scripts.saveScript(scriptname, text,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								MainPanel.setCommInfo(false);
								new AlertDialog(caught).show();

							}

							@Override
							public void onSuccess(Boolean result) {
								MainPanel.setCommInfo(false);
								if (result) {
									ScriptEditor.this.hide();
									if (receiver != null) {
										receiver.reloadList();
									}
								} else {
									new AlertDialog("Can't save script "
											+ scriptname).show();
								}

							}
						});
			}

		});

		Button btnCancel = new Button("Cancel");

		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ScriptEditor.this.hide();
				if (receiver != null) {
					receiver.reloadList();
				}
			}

		});

		HorizontalPanel bcontainer = new HorizontalPanel();
		bcontainer.add(btnSave);
		bcontainer.add(btnCancel);
		bcontainer.setSpacing(5);

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.add(editor);
		vpanel.add(bcontainer);
		vpanel.setWidth("500px");
		vpanel.setHeight("450px");

		setWidget(vpanel);

		setPopupPosition(50, 50);
		setWidth("660px");

	}
}

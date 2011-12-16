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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class ScriptEditor.
 * 
 * @author alexript
 */
public class ScriptEditor extends Window {

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
		super();
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
		setHeading("Script: " + scriptname);
		setAnimCollapse(true);
		setModal(true);
		setBlinkModal(true);
		setLayout(new FitLayout());
		setSize(658, 369);
		setResizable(false);

		setClosable(false);

		editor = new ScriptArea("lua");

		Button btnSave = new Button("Save");

		btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
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

		btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				ScriptEditor.this.hide();
				if (receiver != null) {
					receiver.reloadList();
				}
			}

		});

		add(editor);
		addButton(btnSave);
		addButton(btnCancel);
		show();
	}
}

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.autosauler.ballance.client.Ballance_autosauler_net;
import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.messages.M;
import net.autosauler.ballance.shared.ReportFormField;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ReportPanel.
 * 
 * @author alexript
 */
public class ReportPanel extends Composite implements IPaneWithMenu,
		IReloadMsgReceiver {

	/** The scriptname. */
	private final String scriptname;

	/** The form. */
	private final VerticalPanel form;

	/** The report. */
	private final Panel report;

	/** The myfields. */
	private List<ReportFormField> myfields;

	/** The fields. */
	private final HashMap<String, HeaderField> fields;

	/** The btn exec. */
	private final Button btnExec;

	/**
	 * Instantiates a new report panel.
	 * 
	 * @param scriptname
	 *            the scriptname
	 */
	public ReportPanel(String scriptname) {
		this.scriptname = scriptname;
		form = new VerticalPanel();
		fields = new HashMap<String, HeaderField>();
		VerticalPanel panel = new VerticalPanel();
		panel.add(form);
		btnExec = new Button(M.tools.btnExecute());
		btnExec.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				execReport();

			}
		});
		btnExec.setEnabled(false);
		panel.add(btnExec);
		panel.setCellHeight(btnExec, "30px");

		report = new DecoratorPanel();
		report.setWidth("500px");
		report.setHeight("400px");
		report.setVisible(false);
		panel.add(report);

		initWidget(panel);

		reloadList();
	}

	/**
	 * Exec report.
	 */
	private void execReport() {

		HashMap<String, String> params = new HashMap<String, String>();
		Iterator<ReportFormField> i = myfields.iterator();
		while (i.hasNext()) {
			ReportFormField field = i.next();
			params.put(field.getName(), fields.get(field.getName())
					.getValueAsString());
		}
		report.clear();
		MainPanel.setCommInfo(true);
		Services.reports.generateReport(scriptname, params,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(String result) {
						MainPanel.setCommInfo(false);
						HTML w = new HTML("<p>" + result + "</p>");
						report.add(w);
						report.setVisible(true);
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IPaneWithMenu#getPaneMenu()
	 */
	@Override
	public MenuBar getPaneMenu() {
		MenuBar menu = new MenuBar();
		menu.addItem(M.catalog.menuReload(), new Command() { // reload users
					// list
					@Override
					public void execute() {
						reloadList();
					}
				});
		if (Ballance_autosauler_net.sessionId.getUserrole().isAdmin()) {

			menu.addItem(M.catalog.menuScript(), new Command() {
				@Override
				public void execute() {
					new ScriptEditor("report." + scriptname, ReportPanel.this);
				}
			});
		}
		return menu;
	}

	/**
	 * Reload form.
	 */
	private void reloadForm() {
		btnExec.setEnabled(false);
		report.setVisible(false);
		MainPanel.setCommInfo(true);
		Services.reports.getFields(scriptname,
				new AsyncCallback<List<ReportFormField>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);

						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(List<ReportFormField> result) {

						MainPanel.setCommInfo(false);
						myfields = result;
						form.clear();
						fields.clear();
						Iterator<ReportFormField> i = result.iterator();
						while (i.hasNext()) {
							ReportFormField field = i.next();
							Object helper = null;
							if (field.getType() == DataTypes.DT_CATALOGRECORD) {
								helper = new CatalogPanel(field.getName(), null);
							}
							HeaderField hf = DataTypeFactory.addField(
									field.getDescr(), field.getName(),
									field.getType(), field.getDefval(), helper);
							fields.put(field.getName(), hf);
							form.add(hf);
						}

						btnExec.setEnabled(true);

					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.gui.IReloadMsgReceiver#reloadList()
	 */
	@Override
	public void reloadList() {
		reloadForm();

	}
}

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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class ReportPanel.
 * 
 * @author alexript
 */
public class ReportPanel extends ContentPanel implements IPaneWithMenu,
		IReloadMsgReceiver {

	/** The scriptname. */
	private final String scriptname;

	/** The form. */
	private final FormPanel form;

	/** The report. */
	private final HtmlContainer report;

	/** The myfields. */
	private List<ReportFormField> myfields;

	/** The fields. */
	private final HashMap<String, HeaderField> fields;

	/** The btn exec. */
	private final Button btnExec;
	private final FormData formData = new FormData("98%");

	/**
	 * Instantiates a new report panel.
	 * 
	 * @param scriptname
	 *            the scriptname
	 */
	public ReportPanel(String scriptname) {
		super(new FillLayout());
		setScrollMode(Scroll.AUTO);
		setHeaderVisible(false);
		this.scriptname = scriptname;
		fields = new HashMap<String, HeaderField>();

		form = new FormPanel();
		form.setLabelAlign(LabelAlign.RIGHT);
		form.setLabelWidth(150);
		form.setHeaderVisible(false);

		btnExec = new Button(M.tools.btnExecute());
		btnExec.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				execReport();

			}
		});
		btnExec.setEnabled(false);
		form.addButton(btnExec);
		add(form);

		report = new HtmlContainer();
		report.setWidth("500px");
		report.setHeight("400px");
		report.setVisible(false);

		add(report);

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
		report.setHtml("");
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

						report.setHtml("<p>" + result + "</p>");
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
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu();
		menu.add(new MenuItem(M.catalog.menuReload(),
				new SelectionListener<MenuEvent>() { // reload
					// users
					// list
					@Override
					public void componentSelected(MenuEvent ce) {
						reloadList();
					}
				}));
		if (Ballance_autosauler_net.sessionId.getUserrole().isAdmin()) {

			menu.add(new MenuItem(M.catalog.menuScript(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							new ScriptEditor(scriptname, ReportPanel.this);
						}
					}));
		}
		menubar.add(new MenuBarItem(M.menu.menubarReport(), menu));
		return menubar;
	}

	/**
	 * Reload form.
	 */
	private void reloadForm() {
		// TODO: get fields from loaded description
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
						form.removeAll();
						fields.clear();
						Iterator<ReportFormField> i = result.iterator();
						while (i.hasNext()) {
							ReportFormField field = i.next();
							Object helper = null;
							if (field.getType() == DataTypes.DT_CATALOGRECORD) {
								helper = new CatalogPanel("catalog."
										+ field.getName());
							} else if (field.getType() == DataTypes.DT_DOCUMENTRECORD) {
								helper = new DocumentPanel("document."
										+ field.getName());
							}

							HeaderField hf = DataTypeFactory.addField(
									field.getDescr(), field.getName(),
									field.getType(), field.getDefval(), helper);
							fields.put(field.getName(), hf);
							form.add(hf.getField(), formData);
						}
						form.recalculate();
						form.layout(true);
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

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
import java.util.Set;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.images.Images;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

/**
 * The Class CatalogSelector.
 * 
 * @author alexript
 */
public class CatalogSelector extends Composite {

	/** The catalogname. */
	private final String catalogname;

	/** The box. */
	private final ListBox box;

	/**
	 * Instantiates a new catalog selector.
	 * 
	 * @param catname
	 *            the catname
	 * @param number
	 *            the number
	 */
	public CatalogSelector(String catname, Long number) {
		catalogname = catname;
		box = new ListBox();

		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(3);
		panel.add(box);

		Image reload = new Image(Images.menu.reload());

		reload.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				Long oldvalue = getValue();
				reloadBox(oldvalue);

			}
		});

		panel.add(reload);

		// TODO: add button for create new record

		initWidget(panel);
		reloadBox(number);
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Long getValue() {
		int index = box.getSelectedIndex();
		String val = box.getValue(index);
		Long number = Long.parseLong(val);
		return number;
	}

	/**
	 * Reload box.
	 * 
	 * @param selectednumber
	 *            the selectednumber
	 */
	private void reloadBox(final Long selectednumber) {
		box.clear();

		box.addItem("<not selected>", "0");

		MainPanel.setCommInfo(true);
		Services.catalogs.getRecordsForSelection(catalogname,
				new AsyncCallback<HashMap<String, Long>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(HashMap<String, Long> result) {

						if (result != null) {
							Set<String> set = result.keySet();
							Iterator<String> i = set.iterator();
							while (i.hasNext()) {
								String name = i.next();
								Long number = result.get(name);
								box.addItem(name, number.toString());

							}
						}
						select(selectednumber);
						MainPanel.setCommInfo(false);
					}
				});

	}

	/**
	 * Reset.
	 */
	public void reset() {
		box.setSelectedIndex(0);
	}

	/**
	 * Select.
	 * 
	 * @param number
	 *            the number
	 */
	public void select(Long number) {
		int total = box.getItemCount();
		for (int i = 0; i < total; i++) {
			String val = box.getValue(i);
			Long l = Long.parseLong(val);
			if (l.equals(number)) {
				box.setSelectedIndex(i);
				return;
			}
		}
		box.setSelectedIndex(0);
	}

}

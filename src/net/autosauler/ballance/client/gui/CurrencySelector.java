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

import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.client.CurrencyService;
import net.autosauler.ballance.client.CurrencyServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

/**
 * The Class CurrencySelector.
 * 
 * @author alexript
 */
public class CurrencySelector extends Composite {

	/** The service. */
	private static CurrencyServiceAsync service = GWT
			.create(CurrencyService.class);

	/** The box. */
	private final ListBox box;

	/** The images. */
	private static MenuImages images;

	static {
		images = GWT.create(MenuImages.class);

	}

	/**
	 * Instantiates a new currency selector.
	 * 
	 * @param currentcurrency
	 *            the currentcurrency
	 */
	public CurrencySelector(String currentcurrency) {
		box = new ListBox();

		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(3);
		panel.add(box);

		Image reload = new Image(images.reload());

		reload.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String oldvalue = getValue();
				reloadBox(oldvalue);

			}
		});

		panel.add(reload);

		initWidget(panel);
		reloadBox(currentcurrency);
	}

	/**
	 * Gets the list box.
	 * 
	 * @return the list box
	 */
	public ListBox getListBox() {
		return box;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		int index = box.getSelectedIndex();
		String val = box.getItemText(index);
		return val;
	}

	/**
	 * Reload box.
	 * 
	 * @param selectedcurrency
	 *            the selectedcurrency
	 */
	private void reloadBox(final String selectedcurrency) {
		box.clear();

		MainPanel.setCommInfo(true);
		service.getUsedCurrencyes(new AsyncCallback<Set<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				MainPanel.setCommInfo(false);
				new AlertDialog(caught).show();
			}

			@Override
			public void onSuccess(Set<String> result) {
				if (result != null) {

					Iterator<String> i = result.iterator();
					while (i.hasNext()) {
						String name = i.next();

						box.addItem(name);

					}
				}
				select(selectedcurrency);
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
	 * @param currency
	 *            the currency
	 */
	public void select(String currency) {
		int total = box.getItemCount();
		for (int i = 0; i < total; i++) {
			String val = box.getItemText(i);

			if (val.equals(currency)) {
				box.setSelectedIndex(i);
				return;
			}
		}
		box.setSelectedIndex(0);
	}
}

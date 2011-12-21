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

import net.autosauler.ballance.client.model.CatalogModel;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * The Class CatalogSelector.
 * 
 * @author alexript
 */
public class CatalogSelector extends ComboBox<CatalogModel> {

	/** The catalogname. */
	private final String catalogname;

	/**
	 * Instantiates a new catalog selector.
	 * 
	 * @param catname
	 *            the catname
	 * @param number
	 *            the number
	 */
	public CatalogSelector(String catname, Long number) {
		super();
		catalogname = catname;
		ListStore<CatalogModel> records = new ListStore<CatalogModel>();
		CatalogModel.load(records, catalogname);

		setEmptyText("Select a record...");
		setDisplayField("fullname");
		setValueField("number");
		// setWidth(150);
		setStore(records);
		setTypeAhead(true);
		setTriggerAction(TriggerAction.ALL);

		// Image reload = new Image(Images.menu.reload());
		//
		// reload.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// Long oldvalue = getValue();
		// reloadBox(oldvalue);
		//
		// }
		// });

		// TODO: add button for create new record

		select(number);
	}

	/**
	 * Adds the change handler.
	 * 
	 * @param handler
	 *            the handler
	 */
	public void addChangeHandler(Listener<FieldEvent> handler) {
		addListener(Events.Change, handler);

	}

	public Long getLongValue() {
		return (Long) super.getValue().get("number");
	}

	/**
	 * Reset.
	 */
	@Override
	public void reset() {
		super.reset();
		this.select(0);
	}

	/**
	 * Select.
	 * 
	 * @param number
	 *            the number
	 */
	public void select(Long number) {

		int total = getStore().getCount();
		for (int i = 0; i < total; i++) {

			CatalogModel val = getStore().getAt(i);

			if ((val != null) && (val.get("number") != null)
					&& val.get("number").equals(number)) {

				setValue(val);
				return;
			}
		}
		select(0);

	}

	public void setValue(Long number) {
		select(number);
	}

}

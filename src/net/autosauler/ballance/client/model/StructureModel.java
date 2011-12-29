package net.autosauler.ballance.client.model;

import java.util.HashMap;
import java.util.Set;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;
import net.autosauler.ballance.shared.Description;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class StructureModel extends BaseModelData {

	private static final long serialVersionUID = -2526053708403697876L;

	public static void load(final ListStore<StructureModel> store) {

		MainPanel.setCommInfo(true);
		// Log.error(catalogname);
		Services.structure
				.getAll(new AsyncCallback<HashMap<String, Description>>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.google.gwt.user.client.rpc.AsyncCallback#onFailure
					 * (java.lang .Throwable)
					 */
					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();

					}

					@Override
					public void onSuccess(HashMap<String, Description> result) {
						MainPanel.setCommInfo(false);
						Set<String> names = result.keySet();
						for (String name : names) {
							store.add(new StructureModel(name));
						}

					}
				});

	}

	public StructureModel(String structname) {
		set("name", structname);
	}

}

package net.autosauler.ballance.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.client.Services;
import net.autosauler.ballance.client.gui.AlertDialog;
import net.autosauler.ballance.client.gui.MainPanel;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CatalogModel extends BaseModelData {

	private static final long serialVersionUID = -8157421165515276943L;

	public static void load(final ListStore<CatalogModel> store,
			final String catalogname) {
		final List<CatalogModel> records = new ArrayList<CatalogModel>();
		MainPanel.setCommInfo(true);
		// Log.error(catalogname);
		Services.catalogs.getAllRecords(catalogname,
				new AsyncCallback<Set<Long>>() {

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
					public void onSuccess(Set<Long> result) {

						MainPanel.setCommInfo(false);
						Iterator<Long> i = result.iterator();
						while (i.hasNext()) {
							Long number = i.next();
							records.add(new CatalogModel(number, catalogname,
									store));

						}
						store.add(records);

					}
				});

	}

	public CatalogModel(Long number, String catalogname,
			final ListStore<CatalogModel> store) {
		MainPanel.setCommInfo(true);
		Services.catalogs.getRecord(catalogname, number,
				new AsyncCallback<HashMap<String, Object>>() {

					@Override
					public void onFailure(Throwable caught) {
						MainPanel.setCommInfo(false);
						new AlertDialog(caught).show();
					}

					@Override
					public void onSuccess(final HashMap<String, Object> result) {
						MainPanel.setCommInfo(false);

						if (result == null) {
							new AlertDialog("Server exchange error").show();
						} else {
							Set<String> keys = result.keySet();
							for (String key : keys) {
								set(key, result.get(key));
							}
							store.update(CatalogModel.this);
						}
					}
				});
	}

}

package net.autosauler.ballance.client.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DocumentTableModel extends BaseModelData {

	private static final long serialVersionUID = -6617882539369203722L;

	public DocumentTableModel(HashMap<String, Object> document,
			HashMap<String, Integer> datatypes) {
		Set<String> keys = document.keySet();
		for (String key : keys) {
			if (datatypes.containsKey(key)) {
				int type = datatypes.get(key);

				set(key, DataTypes.fromMapping(type, document.get(key)));
			} else {
				set(key, document.get(key));
			}
		}
	}

	public DocumentTableModel(Long newnumber,
			HashMap<String, Object> defaultvalues,
			HashMap<String, Integer> datatypes) {
		HashMap<String, Object> row = new HashMap<String, Object>();
		row.put("number", newnumber);
		Set<String> names = defaultvalues.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			row.put(name,
					DataTypes.toMapping(datatypes.get(name),
							defaultvalues.get(name)));
		}
	}

}

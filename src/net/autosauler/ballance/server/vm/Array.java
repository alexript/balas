package net.autosauler.ballance.server.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class Array {

	private final List<Hashtable<String, Object>> myset;

	public Array() {
		myset = new ArrayList<Hashtable<String, Object>>();

	}

	public Array(Set<HashMap<String, Object>> set) {
		myset = new ArrayList<Hashtable<String, Object>>();

		for (HashMap<String, Object> record : set) {
			Hashtable<String, Object> cajurecord = new Hashtable<String, Object>(
					record);

			myset.add(cajurecord);
		}

	}

	public Hashtable<String, Object> get(int i) {
		return myset.get(i);
	}

	public int size() {
		return myset.size();
	}
}

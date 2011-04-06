/**
 * 
 */
package net.autosauler.ballance.server.model;

import sixx.Sixx;

import com.allen_sauer.gwt.log.client.Log;

/**
 * @author alexript
 * 
 */
public class Scripts {
	private final static String TABLENAME = "scripts";
	private final String domain;
	private final String name;
	private String text;
	private static Sixx vm = null;

	public Scripts(String domain, String name) {
		this.name = name;
		this.domain = domain;
		loadText();
		if (vm == null) {
			try {
				vm = new Sixx();
			} catch (Exception e) {
				Log.error(e.getMessage());
				vm = null;
			}
		}

		if (vm != null) {
			try {
				vm.eval(text, vm.r6rs);
			} catch (Exception e) {
				Log.error(e.getMessage());
			}
		}
	}

	public Object eval(String str) {
		Object obj = null;
		try {
			obj = vm.eval(str, vm.r6rs);
		} catch (Exception e) {
			Log.error(e.getMessage());
			obj = null;
		}
		return obj;
	}

	public String getText() {
		return text;
	}

	private void loadText() {
		String txt = ""; // TODO: load from database
		if ((txt == null) || txt.isEmpty()) {
			if (!domain.equals("127.0.0.1")) {
				Scripts s = new Scripts("127.0.0.1", name);
				txt = s.getText();
			}
		}
		setText(txt);
	}

	public void setText(String txt) {
		text = txt;
		// TODO: save to database
	}
}

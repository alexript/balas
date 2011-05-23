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

package net.autosauler.ballance.server.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.server.util.Base64;
import net.autosauler.ballance.server.vm.VM;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * The Class Scripts.
 * 
 * @author alexript
 */
public class Scripts {

	/** The Constant TABLENAME. */
	private final static String TABLENAME = "scripts";

	/**
	 * Dump.
	 * 
	 * @param domain
	 *            the domain
	 * @return the string
	 */
	public static String dump(String domain) {
		StringBuilder sb = new StringBuilder();
		sb.append("<scripts>\n");
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			BasicDBObject q = new BasicDBObject();

			q.put("domain", domain);

			DBCursor cur = coll.find(q);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();

				String txt;
				try {
					txt = Base64.encodeObject((String) myDoc.get("text"));
					sb.append("<script name=\"");
					sb.append((String) myDoc.get("name"));
					sb.append("\">");
					sb.append(txt);
					sb.append("</script>\n");
				} catch (IOException e) {
					Log.error(e.getMessage());
				}

			}
			Database.release();
		}
		sb.append("</scripts>\n");
		return sb.toString();
	}

	/** The domain. */
	private final String domain;

	/** The name. */
	private String name;

	/** The text. */
	private String text;

	/** The vm. */
	private static final HashMap<String, VM> vms = new HashMap<String, VM>();

	/** The caller. */
	private final IScriptableObject caller;

	/**
	 * Instantiates a new scripts.
	 * 
	 * @param obj
	 *            the obj
	 * @param domain
	 *            the domain
	 * @param name
	 *            the name
	 */
	public Scripts(IScriptableObject obj, String domain, String name) {
		this.name = name;
		this.domain = domain;
		caller = obj;
		initVM();
		initStruct();
		loadText();

		if (vms.containsKey(domain)) {
			if (text != null) {
				try {

					vms.get(domain).eval(text);
				} catch (Exception e) {
					Log.error(e.getMessage());
				}

			} else {
				Log.error("There is no text for script " + name);
			}

		}
	}

	/**
	 * Instantiates a new scripts. WARNING: only for database restore case
	 * 
	 * @param domain
	 *            the domain
	 */
	public Scripts(String domain) {
		name = "";
		this.domain = domain;
		caller = null;
		initStruct();
	}

	/**
	 * Instantiates a new scripts.
	 * 
	 * @param domain
	 *            the domain
	 * @param name
	 *            the name
	 */
	public Scripts(String domain, String name) {
		this.name = name;
		this.domain = domain;
		caller = null;
		initVM();
		initStruct();
		loadText();

		if (vms.containsKey(domain)) {
			VM vm = vms.get(domain);
			if (vm != null) {

				if (text != null) {
					try {
						vms.get(domain).eval(text);
					} catch (Exception e) {
						Log.error(e.getMessage());
					}

				} else {
					Log.error("There is no text for script " + name);
				}
			}

		}
	}

	/**
	 * Eval.
	 * 
	 * @param cmd
	 *            the cmd
	 * @return the object
	 */
	public Object eval(String cmd) {

		VM vm = vms.get(domain);
		if (vm != null) {
			return vm.eval(cmd);
		}
		return null;
	}

	/**
	 * Eval.
	 * 
	 * @param evalstring
	 *            the evalstring
	 * @param params
	 *            the params
	 * @param types
	 *            the types
	 * @return the hash map
	 */
	public HashMap<String, String> eval(String evalstring,
			HashMap<String, String> params, HashMap<String, Integer> types) {

		Hashtable<String, Object> input = new Hashtable<String, Object>();
		Set<String> names = params.keySet();
		Iterator<String> i = names.iterator();
		while (i.hasNext()) {
			String name = i.next();
			if (types.containsKey(name)) {
				input.put(name,
						DataTypes.fromString(types.get(name), params.get(name)));
			}
		}
		HashMap<String, String> result = new HashMap<String, String>();

		VM vm = vms.get(domain);
		Object eval = null;
		if (vm != null) {

			try {
				eval = vm.eval(evalstring); // TODO: add input hashtable

			} catch (Exception e) {
				Log.error(e.getMessage());
				eval = null;
			}
		}
		// ---------------------
		if (eval != null) {
			if (Hashtable.class.isInstance(eval)) {
				@SuppressWarnings("unchecked")
				Hashtable<String, Object> output = (Hashtable<String, Object>) eval;

				if ((output != null) && !output.isEmpty()) {
					Set<String> cnames = output.keySet();
					Iterator<String> j = cnames.iterator();
					while (j.hasNext()) {
						String name = new String(j.next());
						if (types.containsKey(name)) {
							result.put(
									name,
									DataTypes.toString(types.get(name),
											output.get(name)));
						}
					}
				} else {
					Log.error("Empty eval's result");
				}
			} else {
				Log.error("Eval result is not HashMap: "
						+ eval.getClass().getCanonicalName());
			}
		}

		return result;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Inits the struct.
	 */
	private void initStruct() {
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			List<DBObject> indexes = coll.getIndexInfo();
			if (indexes.size() < 1) {
				BasicDBObject i = new BasicDBObject();

				i.put("domain", 1);
				i.put("name", 1);
				coll.createIndex(i);

			}
			Database.release();
		}
	}

	/**
	 * Inits the vm.
	 */
	private void initVM() {
		if (!vms.containsKey(domain)) {
			vms.put(domain, new VM());

			Scripts global = new Scripts(domain, "global");
			global.nop();
		}

	}

	/**
	 * Load text.
	 */
	private void loadText() {
		String txt = "";

		DBObject doc = null;
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			BasicDBObject query = new BasicDBObject();
			query.put("domain", domain);
			query.put("name", name);

			doc = coll.findOne(query);
			Database.release();
			if (doc != null) {
				txt = (String) doc.get("text");
			}
		}

		if ((txt == null) || txt.isEmpty()) {
			if (!domain.equals("127.0.0.1")) {
				Scripts s = new Scripts(caller, "127.0.0.1", name);
				txt = s.getText();
			}

		}
		if ((txt == null) || txt.isEmpty()) {
			if (domain.equals("127.0.0.1") && name.equals("global")) {
				txt = "$java.lang\nLog.error('Global script evaluated');\n";
			}
			if (caller != null) {
				txt = caller.generateDefaultScript();

			}
			setText(txt, true);
			return;
		}
		setText(txt, false);
	}

	/**
	 * Nop. (no operations)
	 */
	private void nop() {
		return;
	}

	/**
	 * Restore.
	 * 
	 * @param vals
	 *            the vals
	 */
	public void restore(Element vals) {
		NodeList nodes = vals.getElementsByTagName("script");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element val = (Element) nodes.item(i);
			String name = val.getAttribute("name");
			String b64 = val.getTextContent();
			try {
				String text = new String(Base64.decode(b64), "UTF-8");
				this.name = name;
				setText(text, true);
			} catch (IOException e) {
				Log.error(e.getMessage());
			}

		}

	}

	/**
	 * Sets the text.
	 * 
	 * @param txt
	 *            the new text
	 */
	public void setText(String txt) {
		setText(txt, true);
	}

	/**
	 * Sets the text.
	 * 
	 * @param txt
	 *            the txt
	 * @param andstore
	 *            the andstore
	 */
	public void setText(String txt, boolean andstore) {
		text = txt;
		if (andstore) {
			DBObject doc = null;
			DB db = Database.get(domain);
			if (db != null) {
				Database.retain();
				DBCollection coll = db.getCollection(TABLENAME);
				BasicDBObject query = new BasicDBObject();
				query.put("domain", domain);
				query.put("name", name);

				doc = coll.findOne(query);

				if (doc != null) {
					doc.put("text", text);
					coll.save(doc);
				} else {
					doc = new BasicDBObject();
					doc.put("domain", domain);
					doc.put("name", name);
					doc.put("text", text);
					coll.insert(doc);
				}

				Database.release();
				if (vms != null) {
					vms.remove(domain);
				}
			}
		}
	}
}

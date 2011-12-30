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

import javax.script.ScriptException;

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
					txt = Base64.encodeBytes(((String) myDoc.get("text"))
							.getBytes("UTF-8"));
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
	private VM vm = null;

	/** The caller. */
	private final IScriptableObject caller;

	private final String username;

	public Scripts(IScriptableObject obj, String domain, String username,
			String name) {
		this(obj, domain, username, name, null);
	}

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
	public Scripts(IScriptableObject obj, String domain, String username,
			String name, VM vm2) {
		this.name = name;
		this.domain = domain;
		this.username = username;
		caller = obj;
		initVM(vm2);

		initStruct();

		loadText();

		if (vm != null) {
			if (text != null) {
				try {
					vm.eval(text);
				} catch (Exception e) {
					Log.error(e.getMessage());
				}

			} else {
				Log.error("There is no text for script " + name);
			}
		} else {
			Log.error("My vm is null!!!!");
		}

	}

	/**
	 * Instantiates a new scripts. WARNING: only for database restore case
	 * 
	 * @param domain
	 *            the domain
	 */
	public Scripts(String domain, String username) {
		name = "";
		this.domain = domain;
		this.username = username;
		caller = null;
		initStruct();
	}

	public Scripts(String domain, String username, String name) {
		this(domain, username, name, null);
	}

	/**
	 * Instantiates a new scripts.
	 * 
	 * @param domain
	 *            the domain
	 * @param name
	 *            the name
	 */
	public Scripts(String domain, String username, String name, VM vm2) {
		this.name = name;
		this.domain = domain;
		this.username = username;
		caller = null;
		initVM(vm2);
		initStruct();
		loadText();

		if (vm != null) {

			if (text != null) {
				try {

					vm.eval(text);
				} catch (Exception e) {
					Log.error(e.getMessage());
				}

			} else {
				Log.error("There is no text for script " + name);
			}
		}

	}

	/**
	 * Call.
	 * 
	 * @param funcname
	 *            the funcname
	 * @param args
	 *            the args
	 * @return the object
	 * @throws ScriptException
	 *             the script exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	public Object call(String funcname, final Object... args)
			throws ScriptException, NoSuchMethodException {
		Object result = null;

		if (vm != null) {
			result = vm.call(funcname, args);
		}
		return result;
	}

	/**
	 * Eval.
	 * 
	 * @param cmd
	 *            the cmd
	 * @return the object
	 */
	public Object eval(String cmd) {

		if (vm != null) {
			try {
				return vm.eval(cmd);
			} catch (ScriptException e) {
				Log.error(e.getMessage());
			}
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
	 * @throws ScriptException
	 *             the script exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	public HashMap<String, String> eval(String evalstring,
			HashMap<String, String> params, HashMap<String, Integer> types)
			throws ScriptException, NoSuchMethodException {

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

		Object eval = null;
		if (vm != null) {

			eval = vm.call(evalstring, input);

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

	public HashMap<String, String> eval(String evalstring, String changedfield,
			HashMap<String, String> params, HashMap<String, Integer> types)
			throws ScriptException, NoSuchMethodException {
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

		Object eval = null;
		if (vm != null) {

			eval = vm.call(evalstring, changedfield, input);

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
	 * @param evalstring
	 * @param tablename
	 * @param changedfield
	 * @param params
	 * @param types
	 * @return
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 */
	public HashMap<String, String> eval(String evalstring, String tablename,
			String changedfield, HashMap<String, String> params,
			HashMap<String, Integer> types) throws ScriptException,
			NoSuchMethodException {
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

		Object eval = null;
		if (vm != null) {

			eval = vm.call(evalstring, tablename, changedfield, input);

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
	private void initVM(VM parentvm) {
		Log.trace("init vm");
		if (parentvm == null) {
			vm = new VM(domain, username);
		} else {
			vm = parentvm;
		}
		if (!name.equals("global")) {
			Scripts global = new Scripts(domain, username, "global", vm);
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
				Scripts s = new Scripts(caller, "127.0.0.1", username, name, vm);
				txt = s.getText();
			}

		}

		if ((txt == null) || txt.isEmpty()) {
			if (domain.equals("127.0.0.1") && name.equals("global")) {
				txt = "import java.lang\nLog.error('Global script evaluated')\n";
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

				initVM(null);

			}
		}
	}
}

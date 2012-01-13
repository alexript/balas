package net.autosauler.ballance.server.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.server.util.Base64;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Helps {
	/** The Constant TABLENAME. */
	private final static String TABLENAME = "structhelps";

	/**
	 * Dump.
	 * 
	 * @param domain
	 *            the domain
	 * @return the string
	 */
	public static String dump(String domain) {
		StringBuilder sb = new StringBuilder();
		sb.append("<helps>\n");
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
					sb.append("<help name=\"");
					sb.append((String) myDoc.get("name"));
					sb.append("\" ");
					sb.append("locale=\"");
					sb.append((String) myDoc.get("loc"));
					sb.append("\">");
					sb.append(txt);
					sb.append("</help>\n");
				} catch (IOException e) {
					Log.error(e.getMessage());
				}

			}
			Database.release();
		}
		sb.append("</helps>\n");
		return sb.toString();
	}

	public static List<String> getNames(String domain) {
		List<String> names = new ArrayList<String>();
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			BasicDBObject q = new BasicDBObject();

			q.put("domain", domain);

			DBCursor cur = coll.find(q);
			while (cur.hasNext()) {
				DBObject myDoc = cur.next();

				String name = (String) myDoc.get("name");
				if (!names.contains(name)) {
					names.add(name);
				}
			}
			Database.release();
		}
		return names;
	}

	public static void updateHelps(String domain, String name,
			HashMap<String, String> texts) {

		DBObject doc = null;
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			Set<String> locales = texts.keySet();
			for (String locale : locales) {
				String text = texts.get(locale);

				BasicDBObject query = new BasicDBObject();
				query.put("domain", domain);
				query.put("name", name);
				query.put("loc", locale);

				doc = coll.findOne(query);

				if (doc != null) {
					doc.put("text", text);
					coll.save(doc);
				} else {
					doc = new BasicDBObject();
					doc.put("domain", domain);
					doc.put("name", name);
					doc.put("loc", locale);
					doc.put("text", text);
					coll.insert(doc);
				}

			}
			Database.release();

		}

	}

	/** The domain. */
	private final String domain;

	private final String locale;

	public Helps(String domain, String locale) {

		this.domain = domain;
		this.locale = locale;

		initStruct();
	}

	private String generateTemplate(String name) {
		StringBuilder sb = new StringBuilder();

		sb.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
				.append('\n');
		sb.append("<html>").append('\n');
		sb.append("<head>").append('\n');
		sb.append(
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append('\n');
		sb.append("<title>").append(name).append("</title>").append('\n');
		sb.append("</head>").append('\n');
		sb.append("<body>").append('\n');
		sb.append("Unaviable for ").append(name).append(" now").append('\n');
		sb.append("</body>").append('\n');
		sb.append("</html>");

		String result = sb.toString().trim();
		if (!name.isEmpty()) {
			save(name, result);
		}
		return result;
	}

	public String get(String name) {
		String txt = "";

		DBObject doc = null;
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			BasicDBObject query = new BasicDBObject();
			query.put("domain", domain);
			query.put("name", name);
			query.put("loc", locale);

			doc = coll.findOne(query);
			Database.release();
			if (doc != null) {
				txt = (String) doc.get("text");
			}
		}

		if (txt.trim().isEmpty()) {
			txt = generateTemplate(name);
		}

		return txt;
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
				coll.createIndex(i);
				i.put("loc", 1);
				coll.createIndex(i);
				i.put("name", 1);
				coll.createIndex(i);

			}
			Database.release();
		}
	}

	/**
	 * Restore.
	 * 
	 * @param vals
	 *            the vals
	 */
	public void restore(Element vals) {
		NodeList nodes = vals.getElementsByTagName("help");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element val = (Element) nodes.item(i);
			String name = val.getAttribute("name");
			String loc = val.getAttribute("locale");
			String b64 = val.getTextContent();
			try {
				String text = new String(Base64.decode(b64), "UTF-8");
				save(loc, name, text);
			} catch (IOException e) {
				Log.error(e.getMessage());
			}

		}

	}

	public void save(String name, String txt) {
		save(locale, name, txt);
	}

	/**
	 * Sets the text.
	 * 
	 * @param txt
	 *            the txt
	 * @param andstore
	 *            the andstore
	 */
	public void save(String locale, String name, String txt) {

		DBObject doc = null;
		DB db = Database.get(domain);
		if (db != null) {
			Database.retain();
			DBCollection coll = db.getCollection(TABLENAME);
			BasicDBObject query = new BasicDBObject();
			query.put("domain", domain);
			query.put("name", name);
			query.put("loc", locale);

			doc = coll.findOne(query);

			if (doc != null) {
				doc.put("text", txt);
				coll.save(doc);
			} else {
				doc = new BasicDBObject();
				doc.put("domain", domain);
				doc.put("name", name);
				doc.put("loc", locale);
				doc.put("text", txt);
				coll.insert(doc);
			}

			Database.release();

		}

	}
}

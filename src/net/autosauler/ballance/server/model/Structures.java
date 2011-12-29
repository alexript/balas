package net.autosauler.ballance.server.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.server.util.Base64;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.Name;
import net.autosauler.ballance.shared.Table;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Structures {
	/** The Constant TABLENAME. */
	private final static String TABLENAME = "structures";

	/**
	 * Dump.
	 * 
	 * @param domain
	 *            the domain
	 * @return the string
	 */
	public static String dump(String domain) {
		StringBuilder sb = new StringBuilder();
		sb.append("<structures>\n");
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
					sb.append("<struct name=\"");
					sb.append((String) myDoc.get("name"));
					sb.append("\">");
					sb.append(txt);
					sb.append("</struct>\n");
				} catch (IOException e) {
					Log.error(e.getMessage());
				}

			}
			Database.release();
		}
		sb.append("</structures>\n");
		return sb.toString();
	}

	/**
	 * Gets the access.
	 * 
	 * @param access
	 *            the access
	 * @return the access
	 */
	private static UserRole getAccess(String access) {
		UserRole role = new UserRole();

		String[] names = access.split("\\|");
		for (int i = 0; i < names.length; i++) {
			String rolename = names[i].trim().toUpperCase();
			if (rolename.equals("GUEST")) {
				role.setGuest();
			} else if (rolename.equals("ADMIN")) {
				role.setAdmin();
			} else if (rolename.equals("MANAGER")) {
				role.setManager();
			} else if (rolename.equals("DOCUMENTS")) {
				role.setDocuments();
			} else if (rolename.equals("FINANCES")) {
				role.setFinances();
			}
		}

		return role;
	}

	public static HashMap<String, Description> getAll(String domain) {
		HashMap<String, Description> map = new HashMap<String, Description>();

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
				String txt = (String) myDoc.get("text");
				Description d = txtToDescription(txt);
				if (d != null) {
					map.put(name, d);
				}

			}
			Database.release();
		}
		return map;
	}

	private static Description txtToDescription(String text) {
		Description d = new Description();
		try {
			byte[] bytes = text.getBytes("UTF-8");
			InputStream is = new ByteArrayInputStream(bytes);

			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(is);

			NodeList rootnodes = doc.getElementsByTagName("struct");
			for (int i = 0; i < rootnodes.getLength(); i++) {

				Element rootelement = (Element) rootnodes.item(i);

				// Get access levels
				String access = rootelement.getAttribute("access");
				d.setRole(getAccess(access).getRole());

				// read names
				NodeList fieldsets = rootelement.getElementsByTagName("names");
				Name name = new Name();
				Element fieldset1 = (Element) fieldsets.item(0);
				NodeList locales1 = fieldset1.getChildNodes();
				for (int l = 0; l < locales1.getLength(); l++) {
					Node loc = locales1.item(l);
					if (loc.getNodeType() == Node.ELEMENT_NODE) {
						name.setName(((Element) loc).getNodeName(),
								((Element) loc).getTextContent());
					}
				}
				d.setName(name);

				// Read fieldset
				fieldsets = rootelement.getElementsByTagName("fields");
				for (int j = 0; j < fieldsets.getLength(); j++) {
					Element fieldset = (Element) fieldsets.item(j);
					// read fields from set
					NodeList fields = fieldset.getElementsByTagName("field");
					for (int k = 0; k < fields.getLength(); k++) {
						Element field = (Element) fields.item(k);

						String type = field.getAttribute("type");
						String fieldname = field.getAttribute("name");

						Field f = new Field(DataTypes.fromString(type));
						f.setVisible(true);
						f.setInlist(false);
						f.setFieldname(fieldname); // field is visible by
													// default

						// Read field values
						NodeList values = field.getChildNodes();
						for (int c = 0; c < values.getLength(); c++) {
							Node v = values.item(c);
							if (v.getNodeType() == Node.ELEMENT_NODE) {
								Element val = (Element) v;
								if (val.getNodeName().equals("default")) {
									f.setDefvalAsString(val.getTextContent());
								} else if (val.getNodeName().equals("helper")) {
									f.setHelpertype(val.getAttribute("type"));
									f.setHelper(val.getTextContent());
								} else if (val.getNodeName().equals("width")) {
									f.setColumnwidth(Integer.parseInt(val
											.getTextContent()));
								} else if (val.getNodeName().equals("visible")) {
									f.setVisible(Boolean.parseBoolean(val
											.getTextContent()));
								} else if (val.getNodeName().equals("inlist")) {
									f.setInlist(Boolean.parseBoolean(val
											.getTextContent()));
								} else if (val.getNodeName().equals("names")) {
									NodeList locales = val.getChildNodes();
									for (int l = 0; l < locales.getLength(); l++) {
										Node loc = locales.item(l);
										if (loc.getNodeType() == Node.ELEMENT_NODE) {
											f.setName(((Element) loc)
													.getNodeName(),
													((Element) loc)
															.getTextContent());
										}
									}
								}
							}
						}

						d.add(f);

					}
				}

				// read tables
				fieldsets = rootelement.getElementsByTagName("tables");
				for (int j = 0; j < fieldsets.getLength(); j++) {
					Element fieldset = (Element) fieldsets.item(j);
					// read fields from set
					NodeList tables = fieldset.getElementsByTagName("table");
					for (int k = 0; k < tables.getLength(); k++) {
						Element table = (Element) tables.item(k);

						String tablename = table.getAttribute("name");

						Table t = new Table();
						t.setName(tablename);

						// Read table values
						NodeList values = table.getChildNodes();
						for (int c = 0; c < values.getLength(); c++) {
							Node v = values.item(c);
							if (v.getNodeType() == Node.ELEMENT_NODE) {
								Element val = (Element) v;
								if (val.getNodeName().equals("names")) {
									NodeList locales = val.getChildNodes();
									for (int l = 0; l < locales.getLength(); l++) {
										Node loc = locales.item(l);
										if (loc.getNodeType() == Node.ELEMENT_NODE) {
											t.setName(((Element) loc)
													.getNodeName(),
													((Element) loc)
															.getTextContent());
										}
									}
								}
							}
						}

						d.addTable(t);

					}
				}

			}

		} catch (ParserConfigurationException e) {
			Log.error(e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.error(e.getMessage());
			return null;
		} catch (IOException e) {
			Log.error(e.getMessage());
			return null;
		}

		return d;
	}

	/** The domain. */
	private final String domain;

	public Structures(String domain) {

		this.domain = domain;

		initStruct();
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

			doc = coll.findOne(query);
			Database.release();
			if (doc != null) {
				txt = (String) doc.get("text");
			}
		}

		return txt;
	}

	public Description getDescription(String name) {
		return txtToDescription(get(name));
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
		NodeList nodes = vals.getElementsByTagName("struct");
		for (int i = 0; i < nodes.getLength(); i++) {
			Element val = (Element) nodes.item(i);
			String name = val.getAttribute("name");
			String b64 = val.getTextContent();
			try {
				String text = new String(Base64.decode(b64), "UTF-8");
				save(name, text);
			} catch (IOException e) {
				Log.error(e.getMessage());
			}

		}

	}

	/**
	 * Sets the text.
	 * 
	 * @param txt
	 *            the txt
	 * @param andstore
	 *            the andstore
	 */
	public void save(String name, String txt) {

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
				doc.put("text", txt);
				coll.save(doc);
			} else {
				doc = new BasicDBObject();
				doc.put("domain", domain);
				doc.put("name", name);
				doc.put("text", txt);
				coll.insert(doc);
			}

			Database.release();

		}

	}
}

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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.autosauler.ballance.server.mongodb.Database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.allen_sauer.gwt.log.client.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * The Class Currency.
 */
public class Currency {

	/** The Constant CURRENCYTABLE. */
	final private static String CURRENCYTABLE = "currency";

	/**
	 * Creates the default records.
	 * 
	 * @param db
	 *            the db
	 */
	public static void createDefaultRecords(DB db) {

		if (db != null) {
			DBCollection coll = db.getCollection(CURRENCYTABLE);
			if (coll.getCount() < 1) {
				SimpleDateFormat formatter;
				formatter = new SimpleDateFormat("dd/MM/yyyy");
				String day = formatter.format(new Date());
				receiveCBR(day, db);

				BasicDBObject i = new BasicDBObject();
				i.put("date", 1);
				i.put("mnemo", 1);

				coll.createIndex(i);

				i = new BasicDBObject();
				i.put("timestamp", 1);
				i.put("mnemo", 1);

				coll.createIndex(i);

			}
		}
	}

	/**
	 * Find.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param day
	 *            the day
	 * @return the dB object
	 */
	private static DBObject find(String mnemo, String day) {
		DBObject myDoc = null;
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection(CURRENCYTABLE);
			BasicDBObject query = new BasicDBObject();
			query.put("date", day);
			query.put("mnemo", mnemo);
			myDoc = coll.findOne(query);
		}
		return myDoc;
	}

	/**
	 * Find preivous.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param day
	 *            the day
	 * @return the dB object
	 */
	private static DBObject findPreivous(String mnemo, Date day) {
		Long toplimit = day.getTime();

		DBObject myDoc = null;
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection(CURRENCYTABLE);
			BasicDBObject query = new BasicDBObject();
			query.put("timestamp", new BasicDBObject("$lt", toplimit));
			query.put("mnemo", mnemo);

			myDoc = coll.findOne(query, new BasicDBObject("$orderby",
					"timestamp"));
		}
		return myDoc;
	}

	/**
	 * Gets the currency value for today.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @return the double
	 */
	public static Double get(String mnemo) {

		return get(mnemo, new Date());
	}

	/**
	 * Gets the currency value for date.
	 * 
	 * @param mnemo
	 *            the mnemo
	 * @param date
	 *            the date
	 * @return the double
	 */
	public static Double get(String mnemo, Date date) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		String day = formatter.format(date);

		Double val = new Double(1.0);
		if (mnemo.equals("RUR")) {
			// TODO: configurable base currency
			return val;
		}

		DBObject myDoc = find(mnemo, day);

		if (myDoc != null) { // if value is exists in database
			val = (Double) myDoc.get("val");
		} else {
			receiveCBR(day, null);
			myDoc = find(mnemo, day);
			if (myDoc != null) { // if value is downloaded
				val = (Double) myDoc.get("val");
			} else {
				myDoc = findPreivous(mnemo, date);
				if (myDoc != null) { // if old value is exists
					val = (Double) myDoc.get("val");
				}
			}
		}
		return val;
	}

	/**
	 * Gets the node value.
	 * 
	 * @param element
	 *            the element
	 * @param nodename
	 *            the nodename
	 * @return the node value
	 */
	private static String getNodeValue(Element element, String nodename) {
		NodeList fstNmElmntLst = element.getElementsByTagName(nodename);
		Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		NodeList fstNm = fstNmElmnt.getChildNodes();
		String val = (fstNm.item(0)).getNodeValue();
		// log.trace(nodename + ": " + val);
		return val;

	}

	/**
	 * Receive currency from cbr.
	 * 
	 * @param day
	 *            the day
	 */
	private static void receiveCBR(String day, DB database) {

		String requesturi = "http://www.cbr.ru/scripts/XML_daily.asp?date_req="
				+ day;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			Log.error(ex.getMessage());
		} finally {
			try {
				doc = db.parse(requesturi);
			} catch (SAXException ex) {
				Log.error(ex.getMessage());
			} catch (IOException ex) {
				Log.error(ex.getMessage());
			} finally {
				try {
					doc.getDocumentElement().normalize();
					Log.trace("Root element "
							+ doc.getDocumentElement().getNodeName());
					NodeList nodeLst = doc.getElementsByTagName("Valute");
					Log.trace("Information of all valutes");
					if (database == null) {
						database = Database.get();
					}
					if (database != null) {
						DBCollection coll = database
								.getCollection(CURRENCYTABLE);
						Date now = new Date();
						Long timestamp = now.getTime();
						for (int s = 0; s < nodeLst.getLength(); s++) {

							Node fstNode = nodeLst.item(s);

							if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

								Element fstElmnt = (Element) fstNode;

								// String id = fstElmnt.getAttribute("ID");
								// String numcode = getNodeValue(fstElmnt,
								// "NumCode");
								String mnemo = getNodeValue(fstElmnt,
										"CharCode");
								Integer nominal = Integer
										.parseInt(getNodeValue(fstElmnt,
												"Nominal"));
								// String name = getNodeValue(fstElmnt, "Name");
								Double value = Double.parseDouble(getNodeValue(
										fstElmnt, "Value").replace(',', '.'));

								BasicDBObject dbdoc = new BasicDBObject();

								dbdoc.put("date", day);
								dbdoc.put("mnemo", mnemo);
								dbdoc.put("timestamp", timestamp);
								dbdoc.put("val", new Double(value / nominal));
								coll.insert(dbdoc);

							}
						}
					}

				}

				catch (Exception ex) {
					Log.error(ex.getMessage());
				}
			}
		}

	}
}

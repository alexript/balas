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

package net.autosauler.ballance.server.struct;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Field;
import net.autosauler.ballance.shared.UserRole;
import net.autosauler.ballance.shared.datatypes.DataTypes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.allen_sauer.gwt.log.client.Log;

/**
 * A factory for creating Structure objects.
 * 
 * @author alexript
 */
public class StructureFactory {
	// TODO: and for documents!

	/**
	 * Load description.
	 * 
	 * @param fullname
	 *            the fullname
	 * @return the description
	 */
	public static Description loadDescription(String fullname) {
		Description d = null;

		StructureFactory sf = new StructureFactory();

		try {
			d = sf.buildFromStream(fullname);
		} catch (ParserConfigurationException e) {
			Log.error(e.getMessage());
		} catch (SAXException e) {
			Log.error(e.getMessage());
		} catch (IOException e) {
			Log.error(e.getMessage());
		}

		return d;
	}

	/**
	 * Builds the from stream.
	 * 
	 * @param fullname
	 *            the fullname
	 * @return the description
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Description buildFromStream(String fullname)
			throws ParserConfigurationException, SAXException, IOException {
		Description d = null;
		InputStream is = openStream(fullname);
		if (is != null) {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(is);

			NodeList rootnodes = doc.getElementsByTagName("struct");
			for (int i = 0; i < rootnodes.getLength(); i++) {

				d = new Description();

				Element rootelement = (Element) rootnodes.item(i);

				// Get access levels
				String access = rootelement.getAttribute("access");
				d.setRole(getAccess(access).getRole());

				// Read fieldset
				NodeList fieldsets = rootelement.getElementsByTagName("fields");
				for (int j = 0; j < fieldsets.getLength(); j++) {
					Element fieldset = (Element) fieldsets.item(j);
					// read fields from set
					NodeList fields = fieldset.getElementsByTagName("field");
					for (int k = 0; k < fields.getLength(); k++) {
						Element field = (Element) fields.item(k);

						String type = field.getAttribute("type");
						String fieldname = field.getAttribute("name");

						Field f = new Field(DataTypes.fromString(type));
						f.setFieldname(fieldname);

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

			}
		} else {
			Log.error("can't open input stream for " + fullname);
		}
		return d;
	}

	/**
	 * Gets the access.
	 * 
	 * @param access
	 *            the access
	 * @return the access
	 */
	private UserRole getAccess(String access) {
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

	/**
	 * Open stream.
	 * 
	 * @param filename
	 *            the filename
	 * @return the input stream
	 * @throws UnsupportedEncodingException
	 */
	private InputStream openStream(String filename) {
		String name = "net/autosauler/ballance/server/struct/" + filename
				+ ".xml";

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream istream = classLoader.getResourceAsStream(name);
		return istream;
	}
}

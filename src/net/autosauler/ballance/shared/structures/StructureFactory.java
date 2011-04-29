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

package net.autosauler.ballance.shared.structures;

import net.autosauler.ballance.shared.datatypes.DataTypes;

/**
 * A factory for creating Structure objects.
 * 
 * @author alexript
 */
public class StructureFactory {
	// TODO: place data into xml files
	// TODO: and for documents!

	/**
	 * Builds the catalog.
	 * 
	 * @param catalogname
	 *            the catalogname
	 * @return the description
	 */
	public static Description buildCatalog(String catalogname) {
		Description d = new Description();
		Field f = null;
		if (catalogname.equals("paymethod")) {

		} else if (catalogname.equals("tarifs")) {
			f = new Field(DataTypes.DT_SCRIPT);
			f.setFieldname("scrpt");
			f.setDefval("");
			f.setHelper("");
			f.setName("en", "Script");
			d.add(f);
		} else if (catalogname.equals("partners")) {
			f = new Field(DataTypes.DT_STRING);
			f.setFieldname("email");
			f.setDefval("");
			f.setHelper("");
			f.setName("en", "email");
			d.add(f);

			f = new Field(DataTypes.DT_CATALOGRECORD);
			f.setFieldname("paymethod");
			f.setDefval(new Long(0L));
			f.setHelper("paymethod");
			f.setName("en", "Pay method");
			d.add(f);

			f = new Field(DataTypes.DT_CURRENCY);
			f.setFieldname("currency");
			f.setDefval("RUR");
			f.setHelper("");
			f.setName("en", "Currency");
			d.add(f);

			f = new Field(DataTypes.DT_CATALOGRECORD);
			f.setFieldname("tarif");
			f.setDefval(new Long(0L));
			f.setHelper("tarifs");
			f.setName("en", "tarif");
			d.add(f);

		}
		return d;
	}
}

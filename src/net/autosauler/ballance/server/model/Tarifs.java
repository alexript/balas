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

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.mongodb.BasicDBObject;

/**
 * @author alexript
 * 
 */
public class Tarifs extends AbstractCatalog {
	// TODO: must be periodic

	private static final String catname = "tarifs";
	private static final String fieldname_script = "scrpt";

	public Tarifs(String domain, Long number) {
		super(catname, domain, number);
	}

	public Tarifs(String domain, String username) {
		super(catname, domain, username);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addFindLastNumberParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addFindLastNumberParams(BasicDBObject w) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractStructuredData#
	 * addGetRecordParams(com.mongodb.BasicDBObject)
	 */
	@Override
	protected void addGetRecordParams(BasicDBObject query) {
		return;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.server.model.AbstractCatalog#initStructure()
	 */
	@Override
	protected void initStructure() {
		struct.add(fieldname_script, DataTypes.DT_SCRIPT, ""); // TODO: default
																// script

	}

}

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

/**
 * The Class GoodsTablePart.
 * 
 * @author alexript
 */
public class GoodsTablePart extends AbstractDocumentTablePart {

	/** The Constant fieldname_partner. */
	private static final String fieldname_partner = "partner";

	/** The Constant fieldname_weight. */
	private static final String fieldname_weight = "weight";

	/** The Constant fieldname_summ. */
	private static final String fieldname_summ = "summ";

	/** The Constant fieldname_currency. */
	private static final String fieldname_currency = "currency";

	/** The Constant fieldname_boxesnum. */
	private static final String fieldname_boxesnum = "boxesnum";

	/**
	 * Instantiates a new goods table part.
	 * 
	 * @param domain
	 *            the domain
	 */
	public GoodsTablePart(String domain) {
		super("goods", domain);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractDocumentTablePart#initStructure
	 * ()
	 */
	@Override
	protected void initStructure() {
		struct.add(fieldname_partner, DataTypes.DT_CATALOGRECORD, new Long(0L));
		struct.add(fieldname_weight, DataTypes.DT_DOUBLE, new Double(0.0D));
		struct.add(fieldname_summ, DataTypes.DT_MONEY, new Double(0.0D));
		struct.add(fieldname_currency, DataTypes.DT_CURRENCY, "RUR");
		struct.add(fieldname_boxesnum, DataTypes.DT_INT, new Integer(0));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.server.model.AbstractStructuredData#onGetRecord
	 * (java.lang.Long)
	 */
	@Override
	protected void onGetRecord(Long number) {
		return;

	}

}

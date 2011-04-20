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
package net.autosauler.ballance.client.gui;

import java.util.Date;

import net.autosauler.ballance.shared.datatypes.DataTypes;

import com.google.gwt.core.client.GWT;

/**
 * @author alexript
 * 
 */
public class AdditionalPaysTablePart extends DocumentTablePart {

	private static final IncomingGoodsMessages l = GWT
			.create(IncomingGoodsMessages.class);

	/**
	 * @param title
	 */
	public AdditionalPaysTablePart(String title) {
		super(title);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.gui.DocumentTablePart#initTableColumns()
	 */
	@Override
	protected void initTableColumns() {

		addColumn(l.colPartner(), "partner", DataTypes.DT_CATALOGRECORD, 100,
				true, new Long(0L), new PartnersPanel());
		addColumn(l.colPaydate(), "paydate", DataTypes.DT_DATE, 100, true,
				new Date(), null);
		addColumn(l.colPrice(), "summ", DataTypes.DT_MONEY, 50, true,
				new Double(0.0D), null);
		addColumn(l.colCurrency(), "currency", DataTypes.DT_CURRENCY, 50, true,
				"RUR", null);
		addColumn(l.colComments(), "descr", DataTypes.DT_STRING, 250, true, "",
				null);
	}

}

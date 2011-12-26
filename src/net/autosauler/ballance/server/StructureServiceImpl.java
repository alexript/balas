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

package net.autosauler.ballance.server;

import java.util.HashMap;

import net.autosauler.ballance.client.StructureService;
import net.autosauler.ballance.server.struct.StructureFactory;
import net.autosauler.ballance.shared.Description;
import net.autosauler.ballance.shared.Dummy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class StructureServiceImpl.
 * 
 * @author alexript
 */
public class StructureServiceImpl extends RemoteServiceServlet implements
		StructureService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8046719189550180499L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.StructureService#getAll()
	 */
	@Override
	public HashMap<String, Description> getAll() {
		HashMap<String, Description> map = StructureFactory.getDescriptions();

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.StructureService#getDummy()
	 */
	@Override
	public Dummy getDummy() {

		return new Dummy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.StructureService#getStructureDescription
	 * (java.lang.String)
	 */
	@Override
	public Description getStructureDescription(String name) {

		return StructureFactory.loadDescription(name);
	}

}

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

import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.StructureService;
import net.autosauler.ballance.server.model.Helps;
import net.autosauler.ballance.server.model.Structures;
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

	@Override
	public String get(String name) {
		Structures s = new Structures(getDomain());
		return s.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.StructureService#getAll()
	 */
	@Override
	public HashMap<String, Description> getAll() {
		HashMap<String, Description> map = Structures.getAll(getDomain());

		return map;
	}

	/**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
	private String getDomain() {
		HttpSession httpSession = getSession();
		String domain = HttpUtilities.getUserDomain(httpSession);
		return domain;
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

	@Override
	public String getHelp(String locale, String name) {
		Helps h = new Helps(getDomain(), locale);
		return h.get(name);
	}

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	private HttpSession getSession() {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		return httpSession;
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
		Structures s = new Structures(getDomain());
		return s.getDescription(name);
	}

	@Override
	public void save(String name, String text) {
		Structures s = new Structures(getDomain());
		s.save(name, text);

	}

}

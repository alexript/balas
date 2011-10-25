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
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.CatalogService;
import net.autosauler.ballance.server.model.AbstractCatalog;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class CatalogServiceImpl.
 * 
 * @author alexript
 */
public class CatalogServiceImpl extends RemoteServiceServlet implements
		CatalogService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5604719619146321115L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#addPartner(java.util.HashMap
	 * )
	 */
	@Override
	public boolean addRecord(String catalogname, HashMap<String, Object> map) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		AbstractCatalog c = null;
		String domain = HttpUtilities.getUserDomain(httpSession);
		String login = HttpUtilities.getUserLogo(httpSession);
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager() || role.isDocuments()) {
				c = new AbstractCatalog("partners", domain, login);
			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin() || role.isManager()) {
				c = new AbstractCatalog("paymethod", domain, login);
			}
		} else if (catalogname.equals("tarifs")) {
			if (role.isAdmin() || role.isFinances() || role.isManager()) {
				c = new AbstractCatalog("tarifs", domain, login);
			}
		}
		if (c != null) {
			c.fromMap(map);
			result = c.save();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.CatalogService#getAllPartners()
	 */
	@Override
	public Set<Long> getAllRecords(String catalogname) {
		Set<Long> set = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		AbstractCatalog c = null;
		String domain = HttpUtilities.getUserDomain(httpSession);
		String login = HttpUtilities.getUserLogo(httpSession);

		if (catalogname.equals("partners")) {
			c = new AbstractCatalog("partners", domain, login);

		} else if (catalogname.equals("paymethod")) {
			c = new AbstractCatalog("paymethod", domain, login);

		} else if (catalogname.equals("tarifs")) {
			c = new AbstractCatalog("tarifs", domain, login);
		}
		if (c != null) {
			set = c.findAll();
		}
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#getPartner(java.lang.Long)
	 */
	@Override
	public HashMap<String, Object> getRecord(String catalogname, Long number) {
		HashMap<String, Object> map = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		String domain = HttpUtilities.getUserDomain(httpSession);

		AbstractCatalog c = null;
		if (catalogname.equals("partners")) {
			c = new AbstractCatalog("partners", domain, number);

		} else if (catalogname.equals("paymethod")) {
			c = new AbstractCatalog("paymethod", domain, number);

		} else if (catalogname.equals("tarifs")) {
			c = new AbstractCatalog("tarifs", domain, number);

		}
		if (c != null) {
			map = c.toMap();
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#getRecordsForSelection(
	 * java.lang.String)
	 */
	@Override
	public HashMap<String, Long> getRecordsForSelection(String catalogname) {
		HashMap<String, Long> set = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		String domain = HttpUtilities.getUserDomain(httpSession);
		String login = HttpUtilities.getUserLogo(httpSession);
		AbstractCatalog c = null;
		if (catalogname.equals("partners")) {
			c = new AbstractCatalog("partners", domain, login);

		} else if (catalogname.equals("paymethod")) {
			c = new AbstractCatalog("paymethod", domain, login);

		} else if (catalogname.equals("tarifs")) {
			c = new AbstractCatalog("tarifs", domain, login);

		}
		if (c != null) {
			set = c.getSelectData();
		}
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#getRecordsForView(java.
	 * lang.String)
	 */
	@Override
	public HashMap<Long, String> getRecordsForView(String catalogname) {
		HashMap<Long, String> set = null;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		String domain = HttpUtilities.getUserDomain(httpSession);
		String login = HttpUtilities.getUserLogo(httpSession);
		AbstractCatalog c = null;
		if (catalogname.equals("partners")) {
			c = new AbstractCatalog("partners", domain, login);

		} else if (catalogname.equals("paymethod")) {
			c = new AbstractCatalog("paymethod", domain, login);

		} else if (catalogname.equals("tarifs")) {
			c = new AbstractCatalog("tarifs", domain, login);

		}
		if (c != null) {
			set = c.getViewData();
		}
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#restorePartner(java.lang
	 * .Long)
	 */
	@Override
	public boolean restoreRecord(String catalogname, Long number) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		String domain = HttpUtilities.getUserDomain(httpSession);
		AbstractCatalog c = null;
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager() || role.isDocuments()) {
				c = new AbstractCatalog("partners", domain, number);
			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin()) {
				c = new AbstractCatalog("paymethod", domain, number);
			}
		} else if (catalogname.equals("tarifs")) {
			if (role.isAdmin()) {
				c = new AbstractCatalog("tarifs", domain, number);

			}
		}
		if (c != null) {
			c.restore();
			result = c.save();

		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#trashPartner(java.lang.
	 * Long)
	 */
	@Override
	public boolean trashRecord(String catalogname, Long number) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		String domain = HttpUtilities.getUserDomain(httpSession);

		AbstractCatalog c = null;
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager() || role.isDocuments()) {
				c = new AbstractCatalog("partners", domain, number);

			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin()) {
				c = new AbstractCatalog("paymethod", domain, number);

			}
		} else if (catalogname.equals("tarifs")) {
			if (role.isAdmin()) {
				c = new AbstractCatalog("tarifs", domain, number);

			}
		}
		if (c != null) {
			c.trash();
			result = c.save();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.CatalogService#updatePartner(java.lang
	 * .Long, java.util.HashMap)
	 */
	@Override
	public boolean updateRecord(String catalogname, Long number,
			HashMap<String, Object> map) {
		boolean result = false;
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		String domain = HttpUtilities.getUserDomain(httpSession);
		UserRole role = HttpUtilities.getUserRole(httpSession);
		AbstractCatalog c = null;
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager() || role.isDocuments()) {
				c = new AbstractCatalog("partners", domain, number);

			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin() || role.isManager()) {
				c = new AbstractCatalog("paymethod", domain, number);

			}
		} else if (catalogname.equals("tarifs")) {
			if (role.isAdmin() || role.isFinances() || role.isManager()) {
				c = new AbstractCatalog("tarifs", domain, number);

			}
		}
		if (c != null) {
			c.fromMap(map);
			result = c.save();
		}
		return result;
	}

}

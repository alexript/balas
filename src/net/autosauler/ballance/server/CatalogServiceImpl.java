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
import net.autosauler.ballance.server.model.Partner;
import net.autosauler.ballance.server.model.PayMethod;
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
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager()) {
				Partner p = new Partner(
						HttpUtilities.getUserDomain(httpSession),
						HttpUtilities.getUserLogo(httpSession));
				p.fromMap(map);
				result = p.save();
			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin() || role.isManager()) {
				PayMethod p = new PayMethod(
						HttpUtilities.getUserDomain(httpSession),
						HttpUtilities.getUserLogo(httpSession));
				p.fromMap(map);
				result = p.save();
			}
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
		if (catalogname.equals("partners")) {
			Partner p = new Partner(HttpUtilities.getUserDomain(httpSession),
					HttpUtilities.getUserLogo(httpSession));
			set = p.findAll();
		} else if (catalogname.equals("paymethod")) {
			PayMethod p = new PayMethod(
					HttpUtilities.getUserDomain(httpSession),
					HttpUtilities.getUserLogo(httpSession));
			set = p.findAll();
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
		if (catalogname.equals("partners")) {
			Partner p = new Partner(HttpUtilities.getUserDomain(httpSession),
					number);

			map = p.toMap();
		} else if (catalogname.equals("paymethod")) {
			PayMethod p = new PayMethod(
					HttpUtilities.getUserDomain(httpSession), number);

			map = p.toMap();
		}
		return map;
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
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager()) {
				Partner p = new Partner(
						HttpUtilities.getUserDomain(httpSession), number);
				p.restore();
				result = p.save();
			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin()) {
				PayMethod p = new PayMethod(
						HttpUtilities.getUserDomain(httpSession), number);
				p.restore();
				result = p.save();
			}
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
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager()) {
				Partner p = new Partner(
						HttpUtilities.getUserDomain(httpSession), number);
				p.trash();
				result = p.save();
			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin()) {
				PayMethod p = new PayMethod(
						HttpUtilities.getUserDomain(httpSession), number);
				p.trash();
				result = p.save();
			}
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
		UserRole role = HttpUtilities.getUserRole(httpSession);
		if (catalogname.equals("partners")) {
			if (role.isAdmin() || role.isManager()) {
				Partner p = new Partner(
						HttpUtilities.getUserDomain(httpSession), number);
				p.fromMap(map);
				result = p.save();
			}
		} else if (catalogname.equals("paymethod")) {
			if (role.isAdmin() || role.isManager()) {
				PayMethod p = new PayMethod(
						HttpUtilities.getUserDomain(httpSession), number);
				p.fromMap(map);
				result = p.save();
			}
		}
		return result;
	}

}

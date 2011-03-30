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

import net.autosauler.ballance.client.DocumentService;
import net.autosauler.ballance.server.model.IncomingPayment;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class DocumentServiceImpl.
 * 
 * @author alexript
 */
public class DocumentServiceImpl extends RemoteServiceServlet implements
		DocumentService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1695208451355559991L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DocumentService#activate(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public void activate(String docname, Long number) {
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()), number);
				doc.activation();
				doc.save();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DocumentService#create(java.lang.String,
	 * java.util.HashMap)
	 */
	@Override
	public boolean create(String docname, HashMap<String, Object> map) {
		boolean result = false;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()),
						HttpUtilities.getUserLogo(getSession()));
				doc.fromMap(map);
				result = doc.save();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DocumentService#createAndActivate(java
	 * .lang.String, java.util.HashMap)
	 */
	@Override
	public boolean createAndActivate(String docname, HashMap<String, Object> map) {
		boolean result = false;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()),
						HttpUtilities.getUserLogo(getSession()));
				doc.fromMap(map);
				doc.activation();
				result = doc.save();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.DocumentService#get(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public HashMap<String, Object> get(String docname, Long number) {
		HashMap<String, Object> map = null;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()), number);

				map = doc.toMap();
			}
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.DocumentService#get(java.lang.String,
	 * java.util.Set)
	 */
	@Override
	public Set<HashMap<String, Object>> get(String docname, Set<Long> numbers) {
		Set<HashMap<String, Object>> set = null;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()));
				set = doc.get(numbers);
			}
		}
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DocumentService#getAll(java.lang.String)
	 */
	@Override
	public Set<Long> getAll(String docname) {
		Set<Long> set = null;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()),
						HttpUtilities.getUserLogo(getSession()));
				set = doc.findAll();
			}
		}
		return set;
	}

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	private UserRole getRole() {
		HttpSession httpSession = getSession();
		UserRole role = HttpUtilities.getUserRole(httpSession);
		return role;
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
	 * net.autosauler.ballance.client.DocumentService#unactivate(java.lang.String
	 * , java.lang.Long)
	 */
	@Override
	public void unactivate(String docname, Long number) {
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()), number);
				doc.unactivation();

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DocumentService#update(java.lang.String,
	 * java.lang.Long, java.util.HashMap)
	 */
	@Override
	public boolean update(String docname, Long number,
			HashMap<String, Object> map) {
		boolean result = false;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()), number);

				if (doc.isActive()) {
					doc.unactivation();
				}
				doc.fromMap(map);
				result = doc.save();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.DocumentService#updateAndActivate(java
	 * .lang.String, java.lang.Long, java.util.HashMap)
	 */
	@Override
	public boolean updateAndActivate(String docname, Long number,
			HashMap<String, Object> map) {
		boolean result = false;
		UserRole role = getRole();
		if (docname.equals("inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				IncomingPayment doc = new IncomingPayment(
						HttpUtilities.getUserDomain(getSession()), number);
				if (doc.isActive()) {
					doc.unactivation();
				}
				doc.fromMap(map);
				doc.activation();
				result = doc.save();
			}
		}
		return result;
	}

}

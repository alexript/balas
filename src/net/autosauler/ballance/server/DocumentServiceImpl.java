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
import net.autosauler.ballance.server.model.AbstractDocument;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class DocumentServiceImpl.
 * 
 * @author alexript
 */
public class DocumentServiceImpl extends RemoteServiceServlet implements
		DocumentService {

	// TODO: trash, restore

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
		// TODO: return result
		UserRole role = getRole();
		AbstractDocument d = null;
		String domain = getDomain();
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, number);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, number);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, number);

			}
		}

		if (d != null) {
			d.activation();
			d.save();
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
	public boolean create(String docname, HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts) {

		boolean result = false;
		UserRole role = getRole();
		String domain = getDomain();
		String login = getLogin();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, login);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, login);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, login);

			}
		}
		if (d != null) {
			d.fromMap(map);
			result = d.save();
			result = result && d.saveTableRecords(login, tableparts);
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
	public boolean createAndActivate(String docname,
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts) {
		boolean result = false;
		UserRole role = getRole();
		String domain = getDomain();
		String login = getLogin();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, login);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, login);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, login);

			}
		}
		if (d != null) {
			d.fromMap(map);
			result = d.save();
			result = result && d.saveTableRecords(login, tableparts);
			d.activation();
			result = result && d.save();
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
		String domain = getDomain();
		AbstractDocument d = null;

		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, number);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, number);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, number);

			}
		}
		if (d != null) {
			map = d.toMap();
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
		String domain = getDomain();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain);

			}
		}
		if (d != null) {
			set = d.get(numbers);
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
		String domain = getDomain();
		String login = getLogin();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, login);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, login);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, login);

			}
		}
		if (d != null) {
			set = d.findAll();
		}
		return set;
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

	/**
	 * Gets the login.
	 * 
	 * @return the login
	 */
	private String getLogin() {
		HttpSession httpSession = getSession();
		String login = HttpUtilities.getUserLogo(httpSession);
		return login;
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
	 * net.autosauler.ballance.client.DocumentService#getTable(java.lang.String,
	 * java.lang.Long, java.lang.String)
	 */
	@Override
	public Set<HashMap<String, Object>> getTable(String docname, Long number,
			String tablename) {
		Set<HashMap<String, Object>> set = null;
		UserRole role = getRole();
		String domain = getDomain();
		AbstractDocument d = null;
		if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, number);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, number);

			}
		}
		if (d != null) {
			set = d.getTableRecords(tablename);
		}
		return set;
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
		// TODO: return result
		String domain = getDomain();
		UserRole role = getRole();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, number);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, number);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, number);

			}
		}
		if (d != null) {
			d.unactivation();
			d.save();

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
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts) {
		boolean result = false;
		UserRole role = getRole();
		String domain = getDomain();
		String login = getLogin();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, number);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, number);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, number);

			}
		}
		if (d != null) {
			if (d.isActive()) {
				d.unactivation();
			}
			d.fromMap(map);
			result = d.saveTableRecords(login, tableparts);
			result = result & d.save();
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
			HashMap<String, Object> map,
			HashMap<String, Set<HashMap<String, Object>>> tableparts) {
		boolean result = false;
		UserRole role = getRole();
		String domain = getDomain();
		String login = getLogin();
		AbstractDocument d = null;
		if (docname.equals("document.inpay")) {
			if (role.isAdmin() || role.isFinances()) {
				d = new AbstractDocument("inpay", domain, number);

			}
		} else if (docname.equals("document.ingoods")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("ingoods", domain, number);

			}
		} else if (docname.equals("document.cargo")) {
			if (role.isAdmin() || role.isDocuments() || role.isManager()) {
				d = new AbstractDocument("cargo", domain, number);

			}
		}
		if (d != null) {
			if (d.isActive()) {
				d.unactivation();
			}

			d.fromMap(map);
			result = d.saveTableRecords(login, tableparts);
			d.activation();
			result = result && d.save();
		}
		return result;
	}

}

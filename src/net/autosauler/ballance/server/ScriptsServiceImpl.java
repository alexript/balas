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

import javax.script.ScriptException;
import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.ScriptsService;
import net.autosauler.ballance.server.model.Scripts;
import net.autosauler.ballance.shared.UserRole;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class ScriptsServiceImpl.
 * 
 * @author alexript
 */
public class ScriptsServiceImpl extends RemoteServiceServlet implements
		ScriptsService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1421414947034157378L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.ScriptsService#eval(java.lang.String,
	 * java.lang.String, java.util.HashMap)
	 */
	@Override
	public HashMap<String, String> eval(String scriptname, String evalstring,
			HashMap<String, String> params, HashMap<String, Integer> types) {

		Scripts script = new Scripts(getDomain(), getLogin(), scriptname);

		HashMap<String, String> map = null;
		try {
			map = script.eval(evalstring, params, types);
		} catch (ScriptException e) {
			Log.error(e.getMessage());
			map = null;
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
			map = null;
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.ScriptsService#evalOnChange(java.lang.
	 * String, java.lang.String, java.lang.String, java.util.HashMap,
	 * java.util.HashMap)
	 */
	@Override
	public HashMap<String, String> evalOnChange(String scriptname,
			String evalstring, String changedfield,
			HashMap<String, String> params, HashMap<String, Integer> types) {
		Scripts script = new Scripts(getDomain(), getLogin(), scriptname);

		HashMap<String, String> map = null;
		try {
			map = script.eval(evalstring, changedfield, params, types);
		} catch (ScriptException e) {
			Log.error(e.getMessage());
			map = null;
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
			map = null;
		}

		return map;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.ScriptsService#evalOnChangeTable(java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.util.HashMap, java.util.HashMap)
	 */
	@Override
	public HashMap<String, String> evalOnChangeTable(String scriptname,
			String evalstring, String tablename, String changedfield,
			HashMap<String, String> params, HashMap<String, Integer> types) {
		Scripts script = new Scripts(getDomain(), getLogin(), scriptname);

		HashMap<String, String> map = null;
		try {
			map = script.eval(evalstring, tablename, changedfield, params,
					types);
		} catch (ScriptException e) {
			Log.error(e.getMessage());
			map = null;
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
			map = null;
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.ScriptsService#getScript(java.lang.String)
	 */
	@Override
	public String getScript(String scriptname) {
		UserRole role = getRole();
		if (role.isAdmin()) {
			Scripts scr = new Scripts(getDomain(), getLogin(), scriptname);
			String text = scr.getText();
			return text;
		}
		return null;
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
	 * net.autosauler.ballance.client.ScriptsService#saveScript(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public Boolean saveScript(String scriptname, String script) {
		UserRole role = getRole();
		if (role.isAdmin()) {
			Scripts scr = new Scripts(getDomain(), getLogin(), scriptname);
			scr.setText(script, true);
			return true;
		}
		return false;
	}

}

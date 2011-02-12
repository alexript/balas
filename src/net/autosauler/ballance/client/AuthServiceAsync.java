/*
   Copyright 2011 Alex 'Ript' Malyshev <alexript@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.autosauler.ballance.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface AuthServiceAsync.
 * 
 * @author alexript
 */
public interface AuthServiceAsync {

	/**
	 * Chk auth.
	 * 
	 * @param login
	 *            the login
	 * @param password
	 *            the password
	 * @param callback
	 *            the callback
	 */
	void chkAuth(String login, String password,
			AsyncCallback<SessionId> callback);

	/**
	 * Logoff.
	 * 
	 * @param callback
	 *            the callback
	 */
	void logoff(AsyncCallback<Void> callback);

	/**
	 * Session.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param callback
	 *            the callback
	 */
	void session(SessionId sessionId, AsyncCallback<SessionId> callback);

}

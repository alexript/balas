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

package net.autosauler.ballance.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface AuthService.
 */
@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {

	/**
	 * Chk auth for login-password pair
	 * 
	 * @param login
	 *            the login
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	public SessionId chkAuth(String login, String password);

	/**
	 * Logoff user.
	 */
	public void logoff();

	/**
	 * Check session for session :)
	 * 
	 * @param sessionId
	 *            the session id
	 * @return the session id
	 */
	SessionId session(SessionId sessionId);
}

/**
 * 
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
	 * Checks if is session auth.
	 *
	 * @param callback the callback
	 */
	void isSessionAuth(AsyncCallback<Boolean> callback);

	/**
	 * Chk auth.
	 *
	 * @param login the login
	 * @param password the password
	 * @param callback the callback
	 */
	void chkAuth(String login, String password, AsyncCallback<Boolean> callback);

	/**
	 * Logoff.
	 *
	 * @param callback the callback
	 */
	void logoff(AsyncCallback<Void> callback);

}

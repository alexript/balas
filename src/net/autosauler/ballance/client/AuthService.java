package net.autosauler.ballance.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface AuthService.
 */
@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {
	
	/**
	 * Checks if is session auth.
	 *
	 * @return true, if is session auth
	 */
	public boolean isSessionAuth();
	
	/**
	 * Chk auth.
	 *
	 * @param login the login
	 * @param password the password
	 * @return true, if successful
	 */
	public boolean chkAuth(String login, String password);
	
	/**
	 * Logoff.
	 */
	public void logoff();
}

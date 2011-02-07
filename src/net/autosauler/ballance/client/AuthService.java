package net.autosauler.ballance.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface AuthService.
 */
@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {

	
	/**
	 * Chk auth.
	 *
	 * @param login the login
	 * @param password the password
	 * @return true, if successful
	 */
	public SessionId chkAuth(String login, String password);
	
	/**
	 * Logoff.
	 */
	public void logoff();
	
	SessionId session(SessionId sessionId);
}

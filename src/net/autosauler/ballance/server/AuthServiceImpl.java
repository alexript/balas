package net.autosauler.ballance.server;

import net.autosauler.ballance.client.AuthService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class AuthServiceImpl.
 */
public class AuthServiceImpl extends RemoteServiceServlet implements
		AuthService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5272457632267707581L;

	/* (non-Javadoc)
	 * @see net.autosauler.ballance.client.AuthService#isSessionAuth()
	 */
	@Override
	public boolean isSessionAuth() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see net.autosauler.ballance.client.AuthService#chkAuth(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean chkAuth(String login, String password) {
		if(login.equals("admin") && password.equals("admin")) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.autosauler.ballance.client.AuthService#logoff()
	 */
	@Override
	public void logoff() {
		// TODO Auto-generated method stub
		
	}

}

package net.autosauler.ballance.server;

import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.AuthService;
import net.autosauler.ballance.client.SessionId;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class AuthServiceImpl.
 */
public class AuthServiceImpl extends RemoteServiceServlet implements
		AuthService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5272457632267707581L;



	/* (non-Javadoc)
	 * @see net.autosauler.ballance.client.AuthService#chkAuth(java.lang.String, java.lang.String)
	 */
	@Override
	public String chkAuth(String login, String password) {
		if(login.equals("admin") && password.equals("admin")) {
			HttpSession httpSession = getThreadLocalRequest().getSession();
			httpSession.setMaxInactiveInterval(1000 * 60 *60);
			return httpSession.getId();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.autosauler.ballance.client.AuthService#logoff()
	 */
	@Override
	public void logoff() {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		httpSession.removeAttribute("ttt");
	}

	public SessionId session(SessionId sessionId) {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		if(httpSession != null){
			try {
				sessionId.setSessionId(httpSession.getId());
			} catch (IllegalStateException e) {
				sessionId.setSessionId("");
			}
			return sessionId;
		}//end if(result == null)
		return null;
	}

}

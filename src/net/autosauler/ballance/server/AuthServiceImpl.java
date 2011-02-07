package net.autosauler.ballance.server;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.AuthService;
import net.autosauler.ballance.client.SessionId;
import net.autosauler.ballance.server.crypt.BCrypt;

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
		HttpServletRequest request = getThreadLocalRequest();
		String urlAddress = request.getRequestURL().toString();
		try {
			URL url = new URL(urlAddress);
			String hostname = url.getHost();
			login = login + "@" + hostname;
		} catch (MalformedURLException e) {
			
		}
		
		boolean valid = false;
		
		String hashFromDB = findHashForUser(login);
		if(hashFromDB!=null && !hashFromDB.isEmpty()) {
			valid = BCrypt.checkpw(password, hashFromDB);
		} else {
			valid = login.equals("admin@127.0.0.1") && password.equals("admin");
		}
		
		//String hash = BCrypt.hashpw(password, BCrypt.gensalt());
		
		if(valid) {
			HttpSession httpSession = getThreadLocalRequest().getSession();
			httpSession.setMaxInactiveInterval(1000 * 60 *60);
			return httpSession.getId();
		}
		return null;
	}

	private String findHashForUser(String login) {
		// TODO Auto-generated method stub
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

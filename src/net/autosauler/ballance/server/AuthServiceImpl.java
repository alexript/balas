package net.autosauler.ballance.server;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.autosauler.ballance.client.AuthService;
import net.autosauler.ballance.client.SessionId;
import net.autosauler.ballance.server.crypt.BCrypt;
import net.autosauler.ballance.server.mongodb.Database;
import net.autosauler.ballance.shared.UserRole;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * The Class AuthServiceImpl.
 */
public class AuthServiceImpl extends RemoteServiceServlet implements
		AuthService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5272457632267707581L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.AuthService#chkAuth(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public SessionId chkAuth(String login, String password) {
		login = login.trim();
		password = password.trim();
		HttpServletRequest request = getThreadLocalRequest();
		String urlAddress = request.getRequestURL().toString();
		try {
			URL url = new URL(urlAddress);
			String hostname = url.getHost().trim();
			login = login + "@" + hostname;
		} catch (MalformedURLException e) {
			login = login + "@127.0.0.1";
		}

		boolean valid = false;

		String username = "Anonymous";
		UserRole userrole = new UserRole();
		Long uid = -1L;
		String hashFromDB = null;

		DBObject myDoc = findHashForUser(login);

		if (myDoc != null) {
			hashFromDB = (String) myDoc.get("hash");
			username = (String) myDoc.get("fullname");
			userrole.setRole((Integer) myDoc.get("roles"));
			uid = 0L;
		}

		if (hashFromDB != null && !hashFromDB.isEmpty()) {
			valid = BCrypt.checkpw(password, hashFromDB);
			// } else {
			// valid = login.equals("admin@127.0.0.1") &&
			// password.equals("admin");
			// username = "Admin The Great";
			// userrole.setAdmin();
			// uid = 0L;
		}

		if (valid) {
			HttpSession httpSession = getThreadLocalRequest().getSession();
			httpSession.setMaxInactiveInterval(1000 * 60 * 60);
			SessionId sessionid = new SessionId();
			sessionid.setSessionId(httpSession.getId());
			sessionid.setUsername(username);
			httpSession.setAttribute("username", username);
			sessionid.setUserrole(userrole);
			httpSession.setAttribute("userrole", userrole.getRole());
			sessionid.setUid(uid);
			httpSession.setAttribute("uid", uid);
			httpSession.setAttribute("login", login);
			return sessionid;
		}
		return null;
	}

	/**
	 * Find hash for user.
	 * 
	 * @param login
	 *            the login
	 * @return the string
	 */
	private DBObject findHashForUser(String login) {
		DBObject myDoc = null;
		DB db = Database.get();
		if (db != null) {
			DBCollection coll = db.getCollection("registredusers");
			BasicDBObject query = new BasicDBObject();
			query.put("login", login);
			myDoc = coll.findOne(query);

		}
		return myDoc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.autosauler.ballance.client.AuthService#logoff()
	 */
	@Override
	public void logoff() {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		httpSession.removeAttribute("login");
		httpSession.removeAttribute("uid");
		httpSession.removeAttribute("username");
		httpSession.removeAttribute("userrole");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.autosauler.ballance.client.AuthService#session(net.autosauler.ballance
	 * .client.SessionId)
	 */
	public SessionId session(SessionId sessionId) {
		HttpSession httpSession = getThreadLocalRequest().getSession(false);
		if (httpSession != null) {
			try {
				sessionId.setSessionId(httpSession.getId());
				String username = (String) httpSession.getAttribute("username");
				// System.err.println("stored username: " + username);
				if (username == null) {
					username = "Anonymous";
				}
				sessionId.setUsername(username);
				UserRole userrole = new UserRole(
						(Integer) httpSession.getAttribute("userrole"));
				sessionId.setUserrole(userrole);
				Long uid = (Long) httpSession.getAttribute("uid");
				if (uid == null) {
					uid = -1L;
				}
				sessionId.setUid(uid);
			} catch (IllegalStateException e) {
				System.err.println(e.getMessage());
				// sessionId.setUsername(e.getMessage());
				sessionId.setSessionId("");
			}
			return sessionId;
		}// end if(result == null)
		return null;
	}

}

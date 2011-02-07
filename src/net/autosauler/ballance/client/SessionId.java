package net.autosauler.ballance.client;

import java.io.Serializable;

public class SessionId implements Serializable {

	private static final long serialVersionUID = -2546318251991425119L;
	private String sessionId = "";

	public void setSessionId(String sessionId) {
		if (sessionId == null) {
			this.sessionId = "";
		} else {
			this.sessionId = sessionId;
		}
	}// end setSessionId

	public String getSessionId() {
		return sessionId;
	}// end getSessionId
}

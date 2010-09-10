package com.asolutions.scmsshd.authenticators;

import java.util.ArrayList;

import org.apache.sshd.server.session.ServerSession;

public class PassIfAnyInCollectionPassAuthenticator implements
		IPasswordAuthenticator {

	private ArrayList<IPasswordAuthenticator> authList = new ArrayList<IPasswordAuthenticator>();

	public Object authenticate(String username, String password, ServerSession session) {
		for (IPasswordAuthenticator auth : authList) {
			final Object authResult = auth.authenticate(username, password, session);
			if (authResult != null){
				return authResult;
			}
		}
		return null;
	}

	public void setAuthenticators(ArrayList<IPasswordAuthenticator> authList) {
		this.authList = authList;
	}

}

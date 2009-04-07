package com.asolutions.scmsshd.authenticators;

import java.util.ArrayList;

public class PassIfAnyInCollectionPassAuthenticator implements
		IPasswordAuthenticator {

	private ArrayList<IPasswordAuthenticator> authList = new ArrayList<IPasswordAuthenticator>();

	public Object authenticate(String username, String password) {
		for (IPasswordAuthenticator auth : authList) {
			final Object authResult = auth.authenticate(username, password);
			if (authResult != null){
				return authResult;
			}
		}
		return null;
	}

	public void setAuthenticators(ArrayList authList) {
		this.authList = authList;
	}

}

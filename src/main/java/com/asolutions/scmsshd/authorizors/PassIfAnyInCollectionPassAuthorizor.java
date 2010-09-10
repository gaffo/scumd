package com.asolutions.scmsshd.authorizors;

import java.util.ArrayList;

import com.asolutions.scmsshd.sshd.IProjectAuthorizer;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class PassIfAnyInCollectionPassAuthorizor implements IProjectAuthorizer {

	private ArrayList<IProjectAuthorizer> authList = new ArrayList<IProjectAuthorizer>();

	public AuthorizationLevel userIsAuthorizedForProject(String username, String project)
			throws UnparsableProjectException {
		for (IProjectAuthorizer auth : authList) {
			AuthorizationLevel result = auth.userIsAuthorizedForProject(username, project);
			if (result != null){
				return result;
			}
		}
		return null;
	}

	public void setProjectAuthorizers(ArrayList<IProjectAuthorizer> authList) {
		this.authList = authList;
	}

}

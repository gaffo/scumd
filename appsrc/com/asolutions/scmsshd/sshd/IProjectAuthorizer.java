package com.asolutions.scmsshd.sshd;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;

public interface IProjectAuthorizer {

	AuthorizationLevel userIsAuthorizedForProject(String username, String project) throws UnparsableProjectException;

}

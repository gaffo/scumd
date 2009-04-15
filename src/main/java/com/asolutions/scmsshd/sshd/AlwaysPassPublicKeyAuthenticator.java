package com.asolutions.scmsshd.sshd;

import java.security.PublicKey;

import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

public class AlwaysPassPublicKeyAuthenticator implements PublickeyAuthenticator {

	public boolean hasKey(String username, PublicKey key, ServerSession session) {
		return true;
	}

}

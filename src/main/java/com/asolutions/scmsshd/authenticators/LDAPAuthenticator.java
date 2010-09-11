package com.asolutions.scmsshd.authenticators;

import javax.naming.NamingException;

import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.ldap.LDAPBinding;

public class LDAPAuthenticator implements IPasswordAuthenticator {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private LDAPBinding provider;
	private LDAPUsernameResolver resolver;


	public LDAPAuthenticator(LDAPUsernameResolver resolver,
			LDAPBinding ldapBinding) {
				this.resolver = resolver;
				provider = ldapBinding;
	}

	public Object authenticate(String username, String password, ServerSession session) {
		try {
			String nameInNamespace = resolver.resolverUserName(username);
			return this.provider.getBinding(nameInNamespace, password);
		} catch (NamingException e) {
			log.error("Error Authenticating", e);
			return null;
		}
	}

}

package com.asolutions.scmsshd.authenticators;

import javax.naming.NamingException;

import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.ldap.ILDAPAuthLookupProvider;

public class LDAPAuthenticator implements IPasswordAuthenticator {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private String url;
	private String userBase;
	private boolean promiscuous;
	private ILDAPAuthLookupProvider provider;
	private final String matchingElement;

	public LDAPAuthenticator(String url, String userBase, boolean promiscuous) {
		this(url, userBase,"cn=", promiscuous, new JavaxNamingLDAPAuthLookupProvider());
	}

	public LDAPAuthenticator(String url, String userBase, boolean promiscuous,
			                 ILDAPAuthLookupProvider provider) {
		this(url,userBase, "cn=", promiscuous, provider);
	}

	public LDAPAuthenticator(String url, String userbase,
			String matchingElement, boolean promiscuous,
			ILDAPAuthLookupProvider mockAuthLookupProvider) {
				this.url = url;
				this.userBase = userbase;
				this.matchingElement = matchingElement;
				this.promiscuous = promiscuous;
				provider = mockAuthLookupProvider;
		
	}

	public Object authenticate(String username, String password, ServerSession session) {
		username = matchingElement + username + "," + userBase;
		try {
			return provider.provide(url, username, password, promiscuous);
		} catch (NamingException e) {
			log.error("Error Authenticating", e);
			return null;
		}
	}

}

package com.asolutions.scmsshd.authenticators;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.ldap.ILDAPAuthLookupProvider;

public class LDAPAuthenticator implements IPasswordAuthenticator {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private String url;
	private String userBase;
	private boolean promiscuous;
	private ILDAPAuthLookupProvider provider;

	public LDAPAuthenticator(String url, String userBase, boolean promiscuous) {
		this(url, userBase, promiscuous, new JavaxNamingLDAPAuthLookupProvider());
	}

	public LDAPAuthenticator(String url, String userBase, boolean promiscuous,
			                 ILDAPAuthLookupProvider provider) {
		this.url = url;
		this.userBase = userBase;
		this.promiscuous = promiscuous;
		this.provider = provider;
	}

	public Object authenticate(String username, String password) {
		username = "cn=" + username + "," + userBase;
		try {
			return provider.provide(url, username, password, promiscuous);
		} catch (NamingException e) {
			log.error("Error Authenticating", e);
			return null;
		}
	}

}

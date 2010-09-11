/**
 * 
 */
package com.asolutions.scmsshd.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import com.asolutions.scmsshd.ssl.PromiscuousSSLSocketFactory;

public class LDAPBindingProvider {
	private String lookupUserDN;
	private String lookupUserPassword;
	private String url;
	private boolean promiscuous;
	
	public LDAPBindingProvider(String lookupUserDN, String lookupUserPassword,
			String url, boolean promiscuous) {
		this.lookupUserDN = lookupUserDN;
		this.lookupUserPassword = lookupUserPassword;
		this.url = url;
		this.promiscuous = promiscuous;
	}


	public InitialDirContext getBinding() throws NamingException {
		return new InitialDirContext(getProperties(url, lookupUserDN, lookupUserPassword, promiscuous));
	}
	
	public InitialDirContext getBinding(String lookupUserDN, String lookupUserPassword) throws NamingException {
		return new InitialDirContext(getProperties(url, lookupUserDN, lookupUserPassword, promiscuous));
	}

	
	public Properties getProperties(String url, String username, String password, boolean promiscuous)
	{
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		properties.setProperty(Context.PROVIDER_URL, url);
		properties.setProperty(Context.SECURITY_PRINCIPAL, username);
		properties.setProperty(Context.SECURITY_CREDENTIALS, password);
		if (promiscuous){
			properties.setProperty("java.naming.ldap.factory.socket", PromiscuousSSLSocketFactory.class.getName());
		}
		return properties;
	}
}
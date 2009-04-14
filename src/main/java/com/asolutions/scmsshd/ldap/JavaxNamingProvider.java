package com.asolutions.scmsshd.ldap;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import com.asolutions.scmsshd.ssl.PromiscuousSSLSocketFactory;

public class JavaxNamingProvider implements IJavaxNamingProvider {

	private String url;
	private boolean promiscuous;


	public JavaxNamingProvider(String url, boolean promiscuous) {
		this.url = url;
		this.promiscuous = promiscuous;
	}


	public InitialDirContext getBinding(String userDN, String lookupUserPassword) throws NamingException {
		return new InitialDirContext(getProperties(url, userDN, lookupUserPassword, promiscuous));
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

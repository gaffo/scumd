package com.asolutions.scmsshd.authenticators;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.ldap.ILDAPAuthLookupProvider;
import com.asolutions.scmsshd.ssl.PromiscuousSSLSocketFactory;

public class JavaxNamingLDAPAuthLookupProvider implements
		ILDAPAuthLookupProvider {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public Object provide(String url, String username, String password,
			boolean promiscuous) throws NamingException {
		InitialDirContext context = new InitialDirContext(getProperties(url, username, password, promiscuous));
		
		SearchControls searchCtls = new SearchControls();
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		//specify the LDAP search filter
		String searchFilter = "(objectClass=user)";

		//initialize counter to total the results

		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = context.search(username, searchFilter, searchCtls);
		return (answer.next());
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

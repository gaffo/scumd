/**
 * 
 */
package com.asolutions.scmsshd.authenticators;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.asolutions.scmsshd.ldap.LDAPBinding;

public class LDAPUsernameResolver {
	LDAPBinding provider;
	private String userBase;
	private String matchingElement;
	public LDAPUsernameResolver(LDAPBinding provider, String userBase) {
		this(provider, userBase, "cn=");
	}
	public LDAPUsernameResolver(LDAPBinding provider, String userBase, String matchingElement) {
		this.provider = provider;
		this.userBase = userBase;
		this.matchingElement = matchingElement;
	}
	
	public String resolverUserName(String username) throws NamingException{
		InitialDirContext InitialDirectoryContext = provider.getBinding();
		SearchControls searchCtls = new SearchControls();
		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		//specify the LDAP search filter
		String searchFilter = "("+matchingElement+username+")";

		//initialize counter to total the results

		// Search for objects using the filter
		NamingEnumeration<SearchResult> answer = InitialDirectoryContext.search(userBase, searchFilter, searchCtls);
		SearchResult next = answer.next();
		return next.getNameInNamespace();
	}
}
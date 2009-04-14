package com.asolutions.scmsshd.ldap;

import javax.naming.NamingException;

public interface ILDAPAuthLookupProvider {

	Object provide(String url, String username, String password, boolean promiscuous) throws NamingException;

}

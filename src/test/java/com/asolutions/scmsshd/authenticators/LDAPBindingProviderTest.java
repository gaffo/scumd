package com.asolutions.scmsshd.authenticators;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.naming.Context;

import org.junit.Test;

import com.asolutions.scmsshd.ldap.LDAPBindingProvider;
import com.asolutions.scmsshd.ssl.PromiscuousSSLSocketFactory;

public class LDAPBindingProviderTest {

	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String URL = "ldaps://server.lan";
	
	@Test
	public void testGettingPropertiesNonPromiscuous() throws Exception {
		LDAPBindingProvider provider = new LDAPBindingProvider(USERNAME, PASSWORD, URL, false);
		Properties props = provider.getProperties(URL, USERNAME, PASSWORD, false);
		assertEquals(4, props.keySet().size());
		assertEquals(URL, props.get(Context.PROVIDER_URL));
		assertEquals(USERNAME, props.get(Context.SECURITY_PRINCIPAL));
		assertEquals(PASSWORD, props.get(Context.SECURITY_CREDENTIALS));
		assertEquals("com.sun.jndi.ldap.LdapCtxFactory", props.get(Context.INITIAL_CONTEXT_FACTORY));
	}
	
	@Test
	public void testGettingPropertiesPromiscuous() throws Exception {
		LDAPBindingProvider provider = new LDAPBindingProvider(USERNAME, PASSWORD, URL, false);
		Properties props = provider.getProperties(URL, USERNAME, PASSWORD, true);
		assertEquals(5, props.keySet().size());
		assertEquals(URL, props.get(Context.PROVIDER_URL));
		assertEquals(USERNAME, props.get(Context.SECURITY_PRINCIPAL));
		assertEquals(PASSWORD, props.get(Context.SECURITY_CREDENTIALS));
		assertEquals("com.sun.jndi.ldap.LdapCtxFactory", props.get(Context.INITIAL_CONTEXT_FACTORY));
		assertEquals(PromiscuousSSLSocketFactory.class.getName(), props.get("java.naming.ldap.factory.socket"));
	}
	
//	@Test
//	public void testBasicAuth() throws Exception {
//		JavaxNamingLDAPAuthLookupProvider provider = new JavaxNamingLDAPAuthLookupProvider();
//		Object p = provider.provide(URL, USERNAME, PASSWORD, false);
//		assertNotNull(p);
//	}
	
}
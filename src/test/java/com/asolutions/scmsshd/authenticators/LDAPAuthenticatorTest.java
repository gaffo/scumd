package com.asolutions.scmsshd.authenticators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.sshd.server.session.ServerSession;
import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.ldap.ILDAPAuthLookupProvider;


public class LDAPAuthenticatorTest extends MockTestCase {
	
	private static final String USERBASE = "cn=Users,dn=server,dn=lan";
	private static final String URL = "ldaps://server.lan";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static String USERDN = "cn=" + USERNAME + "," + USERBASE;
	private static String MATCHING_ELEMENT = "sAMAccountName=";
	
	@Test
	public void testAuthenticate_UsesMatchingElementFromCtor_PassesWithNoException() throws Exception {
		final ILDAPAuthLookupProvider mockAuthLookupProvider = context.mock(ILDAPAuthLookupProvider.class);
		final ServerSession mockServerSession = context.mock(ServerSession.class);
		checking(new Expectations(){{
			oneOf(mockAuthLookupProvider).provide(URL, MATCHING_ELEMENT + USERNAME + "," + USERBASE, PASSWORD, true);
			will(returnValue("hi"));
		}});
		
		LDAPAuthenticator auth = new LDAPAuthenticator(URL, USERBASE,MATCHING_ELEMENT, true, mockAuthLookupProvider);
		
		assertNotNull(auth.authenticate(USERNAME, PASSWORD, mockServerSession));
	}
	
	@Test
	public void testAuthenticateFailsNull() throws Exception {
		final ILDAPAuthLookupProvider mockAuthLookupProvider = context.mock(ILDAPAuthLookupProvider.class);
		final ServerSession mockServerSession = context.mock(ServerSession.class);
		
		checking(new Expectations(){{
			oneOf(mockAuthLookupProvider).provide(URL, MATCHING_ELEMENT + USERNAME + "," + USERBASE, PASSWORD, true);
			will(returnValue(null));
		}});
		
		LDAPAuthenticator auth = new LDAPAuthenticator(URL, USERBASE,MATCHING_ELEMENT, true, mockAuthLookupProvider);
		
		assertNull(auth.authenticate(USERNAME, PASSWORD, mockServerSession));
	}

}

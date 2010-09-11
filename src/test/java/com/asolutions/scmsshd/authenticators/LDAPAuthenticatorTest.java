package com.asolutions.scmsshd.authenticators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.apache.sshd.server.session.ServerSession;
import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.ldap.LDAPBinding;


public class LDAPAuthenticatorTest extends MockTestCase {
	
	private static final String RESOLVED_USER_NAME = "resolved user name";
	private static final String PASSWORD = "attribute";
	private static final String USERNAME = "username";
	
	@Test
	public void testAuthenticate_UsesMatchingElementFromCtor_PassesWithNoException() throws Exception {
		final ServerSession mockServerSession = context.mock(ServerSession.class);
		final LDAPBinding ldapBinding = context.mock(LDAPBinding.class);
		final LDAPUsernameResolver ldapUsernameResolver = context.mock(LDAPUsernameResolver.class);
		final InitialDirContext binding = context.mock(InitialDirContext.class);
		checking(new Expectations(){{
			one(ldapUsernameResolver).resolverUserName(USERNAME);
			will(returnValue(RESOLVED_USER_NAME));
			oneOf(ldapBinding).getBinding(RESOLVED_USER_NAME, PASSWORD);
			will(returnValue(binding));
		}});
		
		LDAPAuthenticator auth = new LDAPAuthenticator(ldapUsernameResolver,ldapBinding);
		
		assertNotNull(auth.authenticate(USERNAME, PASSWORD, mockServerSession));
	}
	
	@Test
	public void testAuthenticateFailsNull() throws Exception {
		final ServerSession mockServerSession = context.mock(ServerSession.class);
		final LDAPBinding ldapBinding = context.mock(LDAPBinding.class);
		final LDAPUsernameResolver ldapUsernameResolver = context.mock(LDAPUsernameResolver.class);
		checking(new Expectations(){{
			one(ldapUsernameResolver).resolverUserName(USERNAME);
			will(returnValue(RESOLVED_USER_NAME));
			oneOf(ldapBinding).getBinding(RESOLVED_USER_NAME, PASSWORD);
			will(throwException(new NamingException()));
		}});

		LDAPAuthenticator auth = new LDAPAuthenticator(ldapUsernameResolver,ldapBinding);
		assertNull(auth.authenticate(USERNAME, PASSWORD, mockServerSession));
	}

}

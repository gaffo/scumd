package com.asolutions.scmsshd.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.authenticators.LDAPUsernameResolver;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;


public class LDAPProjectAuthorizerTest extends MockTestCase {
	final private String groupBaseDN = "cn=Groups,DC=ldapserver,DC=lan";
	final private String userBaseDN = "cn=User,DC=ldapserver,DC=lan";
	private LDAPBindingProvider ldapBinding;
	private String usernameToCheck = "mike.gaffney";
	private String userToCheckDN = "cn=" + usernameToCheck + "," + userBaseDN;
	private LDAPUsernameResolver ldapUsernameResolver;
	@Before
	public void setupMocks() {
		ldapBinding = context.mock(LDAPBindingProvider.class);
		ldapUsernameResolver = context.mock(LDAPUsernameResolver.class);
	}
	
	@Test
	public void testLookupForProjectSuccess() throws Exception {
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);
		
		final Attributes mockAttrs = context.mock(Attributes.class);
		final Attribute mockAttribute = context.mock(Attribute.class);
		final NamingEnumeration<?> mockEnum = context.mock(NamingEnumeration.class);

		checking(new Expectations(){{
			one(ldapUsernameResolver).resolverUserName(usernameToCheck);
			will(returnValue(userToCheckDN));
			
			one(mockBinding).getAttributes("cn=proj-2-git,cn=Groups,DC=ldapserver,DC=lan");
			will(returnValue(mockAttrs));
			
			one(mockAttrs).get("member");
			will(returnValue(mockAttribute));
			
			one(mockAttribute).getAll();
			will(returnValue(mockEnum));
			
			one(mockEnum).hasMoreElements();
			will(returnValue(true));
			
			one(mockEnum).nextElement();
			will(returnValue(userToCheckDN));
			
			one(ldapBinding).getBinding();
			will(returnValue(mockBinding));
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(groupBaseDN,
															   "git",
															   AuthorizationLevel.AUTH_LEVEL_READ_ONLY,
															   ldapBinding,
															   ldapUsernameResolver);
		assertEquals(AuthorizationLevel.AUTH_LEVEL_READ_ONLY, auth.userIsAuthorizedForProject(usernameToCheck, "proj-2"));
	}
	
	@Test
	public void testLookupForProjectSuccessNoSuffix() throws Exception {
		
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);
		
		final Attributes mockAttrs = context.mock(Attributes.class);
		final Attribute mockAttribute = context.mock(Attribute.class);
		final NamingEnumeration<?> mockEnum = context.mock(NamingEnumeration.class);


		checking(new Expectations(){{
			one(ldapUsernameResolver).resolverUserName(usernameToCheck);
			will(returnValue(userToCheckDN));
			
			one(mockBinding).getAttributes("cn=proj-2,cn=Groups,DC=ldapserver,DC=lan");
			will(returnValue(mockAttrs));
			
			one(mockAttrs).get("member");
			will(returnValue(mockAttribute));
			
			one(mockAttribute).getAll();
			will(returnValue(mockEnum));
			
			one(mockEnum).hasMoreElements();
			will(returnValue(true));
			
			one(mockEnum).nextElement();
			will(returnValue(userToCheckDN));
			
			one(ldapBinding).getBinding();
			will(returnValue(mockBinding));
			
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(groupBaseDN,
															   null,
															   AuthorizationLevel.AUTH_LEVEL_READ_ONLY,
															   ldapBinding,
															   ldapUsernameResolver);
		assertEquals(AuthorizationLevel.AUTH_LEVEL_READ_ONLY, auth.userIsAuthorizedForProject(usernameToCheck, "proj-2"));
	}
	
	@Test
	public void testLookupForProjectNotAMember() throws Exception {
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);
		
		final Attributes mockAttrs = context.mock(Attributes.class);
		final Attribute mockAttribute = context.mock(Attribute.class);
		final NamingEnumeration<?> mockEnum = context.mock(NamingEnumeration.class);

		checking(new Expectations(){{
			
			one(ldapUsernameResolver).resolverUserName(usernameToCheck);
			will(returnValue(userToCheckDN));

			one(mockBinding).getAttributes("cn=proj-2-git,cn=Groups,DC=ldapserver,DC=lan");
			will(returnValue(mockAttrs));
			
			one(mockAttrs).get("member");
			will(returnValue(mockAttribute));
			
			one(mockAttribute).getAll();
			will(returnValue(mockEnum));
			
			one(mockEnum).hasMoreElements();
			will(returnValue(false));

			one(ldapBinding).getBinding();
			will(returnValue(mockBinding));
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(groupBaseDN,
				   "git",
				   AuthorizationLevel.AUTH_LEVEL_READ_ONLY,
				   ldapBinding,
				   ldapUsernameResolver);
		assertNull(auth.userIsAuthorizedForProject(usernameToCheck, "proj-2"));
	}
	

	
}


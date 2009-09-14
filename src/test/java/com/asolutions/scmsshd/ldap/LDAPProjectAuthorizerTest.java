package com.asolutions.scmsshd.ldap;

import static org.junit.Assert.*;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;


public class LDAPProjectAuthorizerTest extends MockTestCase {
	final private String userDN = "cn=Administrator,cn=Users,DC=ldapserver,DC=lan";
	final private String groupBaseDN = "cn=Groups,DC=ldapserver,DC=lan";
	final private String userBaseDN = "cn=User,DC=ldapserver,DC=lan";
	final private String lookupUserPassword = "password";
	final private String groupName = "proj-2-git";
	private String usernameToCheck = "mike.gaffney";
	private String userToCheckDN = "cn=" + usernameToCheck + "," + userBaseDN;
	@Test
	public void testLookupForProjectSuccess() throws Exception {
		final IJavaxNamingProvider namingProvider = context.mock(IJavaxNamingProvider.class);
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);
		final String groupDN = groupName + "," + groupBaseDN;
		
		final Attributes mockAttrs = context.mock(Attributes.class);
		final Attribute mockAttribute = context.mock(Attribute.class);
		final NamingEnumeration<?> mockEnum = context.mock(NamingEnumeration.class);

		checking(new Expectations(){{
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));

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
			
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(userDN, 
															   lookupUserPassword, 
															   groupBaseDN,
															   userBaseDN,
															   "git",
															   namingProvider,
															   AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
		assertEquals(AuthorizationLevel.AUTH_LEVEL_READ_ONLY, auth.userIsAuthorizedForProject(usernameToCheck, "proj-2"));
	}
	
	@Test
	public void testLookupForProjectSuccessNoSuffix() throws Exception {
		
		final IJavaxNamingProvider namingProvider = context.mock(IJavaxNamingProvider.class);
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);
		final String groupDN = groupName + "," + groupBaseDN;
		
		final Attributes mockAttrs = context.mock(Attributes.class);
		final Attribute mockAttribute = context.mock(Attribute.class);
		final NamingEnumeration<?> mockEnum = context.mock(NamingEnumeration.class);

		checking(new Expectations(){{
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));
			
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
			
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(userDN, 
															   lookupUserPassword, 
															   groupBaseDN,
															   userBaseDN,
															   null,
															   namingProvider,
															   AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
		assertEquals(AuthorizationLevel.AUTH_LEVEL_READ_ONLY, auth.userIsAuthorizedForProject(usernameToCheck, "proj-2"));
	}
	
	@Test
	public void testLookupForProjectNotAMember() throws Exception {
		final IJavaxNamingProvider namingProvider = context.mock(IJavaxNamingProvider.class);
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);
		
		final Attributes mockAttrs = context.mock(Attributes.class);
		final Attribute mockAttribute = context.mock(Attribute.class);
		final NamingEnumeration<?> mockEnum = context.mock(NamingEnumeration.class);

		checking(new Expectations(){{
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));

			one(mockBinding).getAttributes("cn=proj-2-git,cn=Groups,DC=ldapserver,DC=lan");
			will(returnValue(mockAttrs));
			
			one(mockAttrs).get("member");
			will(returnValue(mockAttribute));
			
			one(mockAttribute).getAll();
			will(returnValue(mockEnum));
			
			one(mockEnum).hasMoreElements();
			will(returnValue(false));
			
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(userDN, 
															   lookupUserPassword, 
															   groupBaseDN,
															   userBaseDN,
															   "git",
															   namingProvider,
															   AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
		assertNull(auth.userIsAuthorizedForProject(usernameToCheck, "proj-2"));
	}
	
    @Test
	public void testValidWithValidInfo() throws Exception{
		
		final IJavaxNamingProvider namingProvider = context.mock(IJavaxNamingProvider.class);
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);

		checking(new Expectations(){{
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(returnValue(mockBinding));
		}});
		
		LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(userDN, 
				   lookupUserPassword, 
				   groupBaseDN,
				   userBaseDN,
				   "git",
				   namingProvider,
				   AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
	}

	@Test
	public void testInvalidWithInvalidInfo() throws Exception{
		
		Class<IJavaxNamingProvider> typeToMock = IJavaxNamingProvider.class;
		final IJavaxNamingProvider namingProvider = context.mock(typeToMock);
		final InitialDirContext mockBinding = context.mock(InitialDirContext.class);

		checking(new Expectations(){{
			one(namingProvider).getBinding(userDN, lookupUserPassword);
			will(throwException(new NamingException()));
		}});
		
		try{
			LDAPProjectAuthorizer auth = new LDAPProjectAuthorizer(userDN, 
					   lookupUserPassword, 
					   groupBaseDN,
					   userBaseDN,
					   "git",
					   namingProvider,
					   AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
			fail("didn't throw");
		}
		catch (NamingException e){
		}
	}
	
}


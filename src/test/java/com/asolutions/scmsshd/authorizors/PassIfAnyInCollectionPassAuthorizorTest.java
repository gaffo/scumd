package com.asolutions.scmsshd.authorizors;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class PassIfAnyInCollectionPassAuthorizorTest extends MockTestCase {
	
	private static final String PROJECT = "project";
	private static final String USERNAME = "username";

	@Test
	public void testAuthingWithEmptyChainFails() throws Exception {
		assertNull(new PassIfAnyInCollectionPassAuthorizor().userIsAuthorizedForProject(USERNAME, PROJECT));
	}
	
	@Test
	public void testIfAnyPassItPasses() throws Exception {
		final IProjectAuthorizer failsAuth = context.mock(IProjectAuthorizer.class, "failsAuth");
		final IProjectAuthorizer passesAuth = context.mock(IProjectAuthorizer.class, "passesAuth");
		
		checking(new Expectations(){{
			allowing(failsAuth).userIsAuthorizedForProject(USERNAME, PROJECT);
			will(returnValue(null));
			allowing(passesAuth).userIsAuthorizedForProject(USERNAME, PROJECT);
			will(returnValue(AuthorizationLevel.AUTH_LEVEL_READ_ONLY));
		}});
		
		PassIfAnyInCollectionPassAuthorizor auth = new PassIfAnyInCollectionPassAuthorizor();
		ArrayList<IProjectAuthorizer> authList = new ArrayList<IProjectAuthorizer>();
		authList.add(failsAuth);
		authList.add(passesAuth);
		authList.add(failsAuth);
		auth.setProjectAuthorizers(authList);
		assertEquals(AuthorizationLevel.AUTH_LEVEL_READ_ONLY, auth.userIsAuthorizedForProject(USERNAME, PROJECT));
	}
	
	@Test
	public void testIfNonePassItFails() throws Exception {
		final IProjectAuthorizer failsAuth = context.mock(IProjectAuthorizer.class, "failsAuth");
		
		checking(new Expectations(){{
			allowing(failsAuth).userIsAuthorizedForProject(USERNAME, PROJECT);
			will(returnValue(null));
		}});
		
		PassIfAnyInCollectionPassAuthorizor auth = new PassIfAnyInCollectionPassAuthorizor();
		ArrayList<IProjectAuthorizer> authList = new ArrayList<IProjectAuthorizer>();
		authList.add(failsAuth);
		authList.add(failsAuth);
		auth.setProjectAuthorizers(authList);
		assertNull(auth.userIsAuthorizedForProject(USERNAME, PROJECT));
	}

}

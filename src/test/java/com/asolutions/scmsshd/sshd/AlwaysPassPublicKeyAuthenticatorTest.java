package com.asolutions.scmsshd.sshd;

import java.security.PublicKey;

import org.apache.sshd.server.session.ServerSession;
import org.jmock.Expectations;
import org.junit.Test;

import static org.junit.Assert.*;

import com.asolutions.MockTestCase;

public class AlwaysPassPublicKeyAuthenticatorTest extends MockTestCase{

	@Test
	public void testFailingAuth() throws Exception {
		PublicKey mockPublicKey = context.mock(PublicKey.class);
		ServerSession mockSession = context.mock(ServerSession.class);
		
		checking(new Expectations(){{
		}});
		
		AlwaysPassPublicKeyAuthenticator auth = new AlwaysPassPublicKeyAuthenticator();
		assertTrue(auth.hasKey("username", mockPublicKey, mockSession));
		assertTrue(auth.hasKey("username2", mockPublicKey, mockSession));
	}
	
}

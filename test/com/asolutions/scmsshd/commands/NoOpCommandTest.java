package com.asolutions.scmsshd.commands;

import static org.junit.Assert.*;

import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;

public class NoOpCommandTest extends MockTestCase{

	@Test
	public void testStartJustCallsExitWith1OnCallback() throws Exception {
		final ExitCallback mockExitCallback = context.mock(ExitCallback.class);
		
		checking(new Expectations(){{
			one(mockExitCallback).onExit(-1);
		}});
		
		NoOpCommand cmd = new NoOpCommand();
		cmd.setExitCallback(mockExitCallback);
		cmd.start();
	}
	
}

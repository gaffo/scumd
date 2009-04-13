package com.asolutions.scmsshd.commands;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.asolutions.scmsshd.commands.filters.BadCommandException;


public class AllowedCommandCheckerTest {

	@Test
	public void testParsesGitCommands() throws Exception {
		new AllowedCommandChecker("git-upload-pack");
		new AllowedCommandChecker("git upload-pack");
		new AllowedCommandChecker("git-receive-pack");
		new AllowedCommandChecker("git receive-pack");
	}
	
	@Test
	public void testFailsOnUnknownCommand() throws Exception {
		try{
			new AllowedCommandChecker("git receive-packs");
			fail("Didn't Throw");
		}
		catch (BadCommandException e){
		}
	}
	
}

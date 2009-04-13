package com.asolutions.scmsshd.commands.filters.git;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GitCommandSeamerTest {
	@Test
	public void testSeamsGitCommands() throws Exception {
		assertEquals("git-upload-pack bob", new GitCommandSeamer("git upload-pack bob").toString());
	}
	
	@Test
	public void testDoesntResemUnessecarily() throws Exception {
		assertEquals("git-upload-pack bob", new GitCommandSeamer("git-upload-pack bob").toString());
	}
}

package com.asolutions.scmsshd.commands.factories;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.asolutions.scmsshd.commands.filters.git.GitBadCommandFilter;

public class GitCommandFactoryTest {

	@Test
	public void testUsesCorrectFilterAndFactory() throws Exception {
		CommandFactoryBase factory = new GitCommandFactory();
		assertEquals(GitBadCommandFilter.class, factory.getBadCommandFilter().getClass());
		assertEquals(GitSCMCommandFactory.class, factory.getScmCommandFactory().getClass());
	}

}

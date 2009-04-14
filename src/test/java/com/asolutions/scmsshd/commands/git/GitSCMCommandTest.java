package com.asolutions.scmsshd.commands.git;

import java.util.Properties;

import org.junit.Test;
import org.spearce.jgit.util.QuotedString.GitPathStyle;

import static org.junit.Assert.*;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class GitSCMCommandTest extends MockTestCase{
	
	@Test
	public void testGitCTORCreatesObjectCorrectly() throws Exception {
		FilteredCommand command = new FilteredCommand("git-upload-pack", "/proj-2/git.git");
		IProjectAuthorizer mockProjectAuthorizer = context.mock(IProjectAuthorizer.class);
		IPathToProjectNameConverter mockProjectNameConverter = context.mock(IPathToProjectNameConverter.class);
		final Properties mockConfig = context.mock(Properties.class);
		
		GitSCMCommand cmd = new GitSCMCommand(command, mockProjectAuthorizer, mockProjectNameConverter, mockConfig);
		assertEquals(GitSCMCommandHandler.class, cmd.getSCMCommandHandler().getClass());
		assertEquals(command, cmd.getFilteredCommand());
		assertEquals(mockProjectAuthorizer, cmd.getProjectAuthorizer());
		assertEquals(mockProjectNameConverter, cmd.getPathToProjectNameConverter());
	}
	
}

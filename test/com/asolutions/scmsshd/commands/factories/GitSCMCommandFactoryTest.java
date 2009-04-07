package com.asolutions.scmsshd.commands.factories;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.git.SCMCommand;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class GitSCMCommandFactoryTest extends MockTestCase{
	
	@Test
	public void testCreatesAGitCommand() throws Exception {
		FilteredCommand filteredCommand = new FilteredCommand();
		IProjectAuthorizer mockProjectAuthorizer = context.mock(IProjectAuthorizer.class);
		final IPathToProjectNameConverter mockPathConverter = context.mock(IPathToProjectNameConverter.class);
		
		final Properties mockConfig = context.mock(Properties.class);
		
		GitSCMCommandFactory factory = new GitSCMCommandFactory();
		SCMCommand command = (SCMCommand) factory.create(filteredCommand, mockProjectAuthorizer, mockPathConverter, mockConfig);
		assertEquals(filteredCommand, command.getFilteredCommand());
		assertEquals(mockProjectAuthorizer, command.getProjectAuthorizer());
	}

}

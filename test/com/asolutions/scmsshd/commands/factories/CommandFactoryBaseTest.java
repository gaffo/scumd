package com.asolutions.scmsshd.commands.factories;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.NoOpCommand;
import com.asolutions.scmsshd.commands.filters.BadCommandException;
import com.asolutions.scmsshd.commands.filters.IBadCommandFilter;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class CommandFactoryBaseTest  extends MockTestCase {
	
	private static final String ARGUMENT = "argument";
	private static final String COMMAND = "command";
	
	@Test
	public void testBadCommandReturnsNoOp() throws Exception {
		final IBadCommandFilter mockBadCommandFilter = context.mock(IBadCommandFilter.class);
		checking(new Expectations(){{
			one(mockBadCommandFilter).filterOrThrow(COMMAND);
			will(throwException(new BadCommandException()));
		}});
		
		CommandFactoryBase factory = new CommandFactoryBase();
		factory.setBadCommandFilter(mockBadCommandFilter);
		assertEquals(NoOpCommand.class, factory.createCommand(COMMAND).getClass());
	}
	
	@Test
	public void testChecksBadCommandFirst() throws Exception {
		final IBadCommandFilter mockBadCommandFilter = context.mock(IBadCommandFilter.class);
		final FilteredCommand filteredCommand = new FilteredCommand(COMMAND, ARGUMENT);
		final ISCMCommandFactory mockScmCommandFactory = context.mock(ISCMCommandFactory.class);
		final IProjectAuthorizer mockProjAuth = context.mock(IProjectAuthorizer.class);
		final IPathToProjectNameConverter mockPathConverter = context.mock(IPathToProjectNameConverter.class);
		final Properties mockConfig = context.mock(Properties.class);
		
		checking(new Expectations(){{
			one(mockBadCommandFilter).filterOrThrow(COMMAND);
			will(returnValue(filteredCommand));
			one(mockScmCommandFactory).create(filteredCommand, mockProjAuth, mockPathConverter, mockConfig);
		}});
		
		CommandFactoryBase factory = new CommandFactoryBase();
		factory.setBadCommandFilter(mockBadCommandFilter);
		factory.setScmCommandFactory(mockScmCommandFactory);
		factory.setProjectAuthorizor(mockProjAuth);
		factory.setPathToProjectNameConverter(mockPathConverter);
		factory.setConfiguration(mockConfig);
		factory.createCommand(COMMAND);
	}

}

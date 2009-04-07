package com.asolutions.scmsshd.commands.git;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.jmock.Expectations;
import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.handlers.ISCMCommandHandler;
import com.asolutions.scmsshd.exceptions.Failure;
import com.asolutions.scmsshd.exceptions.MustHaveWritePrivilagesToPushFailure;

import static org.junit.Assert.*;

public class GitSCMCommandHandlerTest extends MockTestCase {
	
	@Test
	public void testExecuteWithUploadPack() throws Exception {
		final FilteredCommand filteredCommand = new FilteredCommand("git-upload-pack", "proj-2/git.git");
		final InputStream mockInputStream = context.mock(InputStream.class);
		final OutputStream mockOutputStream = context.mock(OutputStream.class, "mockOutputStream");
		final OutputStream mockErrorStream = context.mock(OutputStream.class, "mockErrorStream");
		final ExitCallback mockExitCallback = context.mock(ExitCallback.class);
		final ISCMCommandHandler mockUploadPackHandler = context.mock(ISCMCommandHandler.class, "mockUploadPackHandler");
		final ISCMCommandHandler mockFetchPackHandler = context.mock(ISCMCommandHandler.class, "mockFetchPackHandler");
		
		final Properties mockProperties = context.mock(Properties.class);
		
		checking(new Expectations(){{
			one(mockUploadPackHandler).execute(filteredCommand, mockInputStream, mockOutputStream, mockErrorStream, mockExitCallback, mockProperties, AuthorizationLevel.AUTH_LEVEL_READ_WRITE);
		}});
		
		GitSCMCommandHandler handler = new GitSCMCommandHandler(mockUploadPackHandler, mockFetchPackHandler);
		handler.execute(filteredCommand, mockInputStream, mockOutputStream, mockErrorStream, mockExitCallback, mockProperties, AuthorizationLevel.AUTH_LEVEL_READ_WRITE);
	}
	
	@Test
	public void testWithUploadPackReadOnlyAccessLevelWorks() throws Exception {
		final FilteredCommand filteredCommand = new FilteredCommand("git-upload-pack", "proj-2/git.git");
		final InputStream mockInputStream = context.mock(InputStream.class);
		final OutputStream mockOutputStream = context.mock(OutputStream.class, "mockOutputStream");
		final OutputStream mockErrorStream = context.mock(OutputStream.class, "mockErrorStream");
		final ExitCallback mockExitCallback = context.mock(ExitCallback.class);
		final ISCMCommandHandler mockUploadPackHandler = context.mock(ISCMCommandHandler.class, "mockUploadPackHandler");
		final ISCMCommandHandler mockFetchPackHandler = context.mock(ISCMCommandHandler.class, "mockFetchPackHandler");
		
		final Properties mockProperties = context.mock(Properties.class);

		checking(new Expectations(){{
			one(mockUploadPackHandler).execute(filteredCommand, mockInputStream, mockOutputStream, mockErrorStream, mockExitCallback, mockProperties, AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
		}});
		
		GitSCMCommandHandler handler = new GitSCMCommandHandler(mockUploadPackHandler, mockFetchPackHandler);
		handler.execute(filteredCommand, mockInputStream, mockOutputStream, mockErrorStream, mockExitCallback, mockProperties, AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
	}
	
	@Test
	public void testExecuteWithReceivePack() throws Exception {
		final FilteredCommand filteredCommand = new FilteredCommand("git-receive-pack", "proj-2/git.git");
		final InputStream mockInputStream = context.mock(InputStream.class);
		final OutputStream mockOutputStream = context.mock(OutputStream.class, "mockOutputStream");
		final OutputStream mockErrorStream = context.mock(OutputStream.class, "mockErrorStream");
		final ExitCallback mockExitCallback = context.mock(ExitCallback.class);
		final ISCMCommandHandler mockUploadPackHandler = context.mock(ISCMCommandHandler.class, "mockUploadPackHandler");
		final ISCMCommandHandler mockReceivePackHandler = context.mock(ISCMCommandHandler.class, "mockReceivePackHandler");
		final Properties mockProperties = context.mock(Properties.class);
		
		checking(new Expectations(){{
			one(mockReceivePackHandler).execute(filteredCommand, mockInputStream, mockOutputStream, mockErrorStream, mockExitCallback, mockProperties, AuthorizationLevel.AUTH_LEVEL_READ_WRITE);
		}});
		
		GitSCMCommandHandler handler = new GitSCMCommandHandler(mockUploadPackHandler, mockReceivePackHandler);
		handler.execute(filteredCommand, mockInputStream, mockOutputStream, mockErrorStream, mockExitCallback, mockProperties, AuthorizationLevel.AUTH_LEVEL_READ_WRITE);
	}
	
}

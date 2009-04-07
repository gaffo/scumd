package com.asolutions.scmsshd.commands.git;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.jmock.Expectations;
import org.junit.Test;
import static org.junit.Assert.*;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.ReceivePack;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.factories.GitSCMCommandFactory;
import com.asolutions.scmsshd.exceptions.Failure;
import com.asolutions.scmsshd.exceptions.MustHaveWritePrivilagesToPushFailure;

public class GitReceivePackSCMCommandHandlerTest extends MockTestCase {

	@Test
	public void testReceivePackPassesCorrectStuffToJGIT() throws Exception {
		final String pathtobasedir = "pathtobasedir";
		final FilteredCommand filteredCommand = new FilteredCommand(
				"git-receive-pack", "proj-2/git.git");
		final InputStream mockInputStream = context.mock(InputStream.class);
		final OutputStream mockOutputStream = context.mock(OutputStream.class,
				"mockOutputStream");
		final OutputStream mockErrorStream = context.mock(OutputStream.class,
				"mockErrorStream");
		final ExitCallback mockExitCallback = context.mock(ExitCallback.class);

		final GitSCMRepositoryProvider mockRepoProvider = context
				.mock(GitSCMRepositoryProvider.class);
		final Repository mockRepoistory = context.mock(Repository.class);
		final File base = new File(pathtobasedir);
		final GitReceivePackProvider mockReceivePackProvider = context
				.mock(GitReceivePackProvider.class);
		final ReceivePack mockUploadPack = context.mock(ReceivePack.class);

		final Properties mockConfig = context.mock(Properties.class);

		checking(new Expectations() {
			{
				one(mockRepoProvider).provide(base,
						filteredCommand.getArgument());
				will(returnValue(mockRepoistory));
				one(mockReceivePackProvider).provide(mockRepoistory);
				will(returnValue(mockUploadPack));
				one(mockUploadPack).receive(mockInputStream, mockOutputStream,
						mockErrorStream);
//				one(mockExitCallback).onExit(0);
//				one(mockOutputStream).flush();
//				one(mockErrorStream).flush();

				one(mockConfig).getProperty(
						GitSCMCommandFactory.REPOSITORY_BASE);
				will(returnValue(pathtobasedir));
			}
		});

		GitSCMCommandImpl handler = new GitReceivePackSCMCommandHandler(
				mockRepoProvider, mockReceivePackProvider);
		handler.runCommand(filteredCommand, mockInputStream, mockOutputStream,
				mockErrorStream, mockExitCallback, mockConfig,
				AuthorizationLevel.AUTH_LEVEL_READ_WRITE);
	}

	@Test
	public void testThrowsExceptionIfWrongAccessLevel() throws Exception {

		final String pathtobasedir = "pathtobasedir";
		final FilteredCommand filteredCommand = new FilteredCommand(
				"git-receive-pack", "proj-2/git.git");
		final InputStream mockInputStream = context.mock(InputStream.class);
		final OutputStream mockOutputStream = context.mock(OutputStream.class,
				"mockOutputStream");
		final OutputStream mockErrorStream = context.mock(OutputStream.class,
				"mockErrorStream");
		final ExitCallback mockExitCallback = context.mock(ExitCallback.class);

		final GitSCMRepositoryProvider mockRepoProvider = context
				.mock(GitSCMRepositoryProvider.class);
		final Repository mockRepoistory = context.mock(Repository.class);
		final File base = new File(pathtobasedir);
		final GitReceivePackProvider mockReceivePackProvider = context
				.mock(GitReceivePackProvider.class);
		final ReceivePack mockUploadPack = context.mock(ReceivePack.class);

		final Properties mockConfig = context.mock(Properties.class);

		checking(new Expectations() {
			{
				never(mockRepoProvider).provide(base,
						filteredCommand.getArgument());
				never(mockConfig).getProperty(
						GitSCMCommandFactory.REPOSITORY_BASE);
			}
		});

		GitSCMCommandImpl handler = new GitReceivePackSCMCommandHandler(
				mockRepoProvider, mockReceivePackProvider);
		try {
			handler.runCommand(filteredCommand, mockInputStream,
					mockOutputStream, mockErrorStream, mockExitCallback,
					mockConfig, AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
			fail("didn't throw");
		} catch (Failure e) {
			assertEquals(MustHaveWritePrivilagesToPushFailure.class, e
					.getClass());
		}
	}

}

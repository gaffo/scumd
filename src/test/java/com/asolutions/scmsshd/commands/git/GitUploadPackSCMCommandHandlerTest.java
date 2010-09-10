package com.asolutions.scmsshd.commands.git;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.jmock.Expectations;
import org.junit.Test;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.UploadPack;

import com.asolutions.MockTestCase;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.factories.GitSCMCommandFactory;

public class GitUploadPackSCMCommandHandlerTest extends MockTestCase {

	@Test
	public void testUploadPackPassesCorrectStuffToJGIT() throws Exception {
		final String pathtobasedir = "pathtobasedir";

		final FilteredCommand filteredCommand = new FilteredCommand(
				"git-upload-pack", "proj-2/git.git");
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
		final GitUploadPackProvider mockUploadPackProvider = context
				.mock(GitUploadPackProvider.class);
		final UploadPack mockUploadPack = context.mock(UploadPack.class);
		final Properties mockConfig = context.mock(Properties.class);

		checking(new Expectations() {
			{
				one(mockRepoProvider).provide(base,
						filteredCommand.getArgument());
				will(returnValue(mockRepoistory));
				one(mockUploadPackProvider).provide(mockRepoistory);
				will(returnValue(mockUploadPack));
				one(mockUploadPack).upload(mockInputStream, mockOutputStream,
						mockErrorStream);
//				one(mockExitCallback).onExit(0);
//				one(mockOutputStream).flush();
//				one(mockErrorStream).flush();

				one(mockConfig).getProperty(
						GitSCMCommandFactory.REPOSITORY_BASE);
				will(returnValue(pathtobasedir));
			}
		});

		GitSCMCommandImpl handler = new GitUploadPackSCMCommandHandler(
				mockRepoProvider, mockUploadPackProvider);
		handler.runCommand(filteredCommand, mockInputStream, mockOutputStream,
				mockErrorStream, mockExitCallback, mockConfig,
				AuthorizationLevel.AUTH_LEVEL_READ_ONLY);
	}

}

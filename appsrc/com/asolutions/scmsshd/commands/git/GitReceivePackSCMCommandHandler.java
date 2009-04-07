package com.asolutions.scmsshd.commands.git;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.ReceivePack;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.factories.GitSCMCommandFactory;
import com.asolutions.scmsshd.exceptions.MustHaveWritePrivilagesToPushFailure;

public class GitReceivePackSCMCommandHandler extends GitSCMCommandImpl {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private GitSCMRepositoryProvider repositoryProvider;
	private GitReceivePackProvider receivePackProvider;

	public GitReceivePackSCMCommandHandler() {
		this(new GitSCMRepositoryProvider(), new GitReceivePackProvider());
	}

	public GitReceivePackSCMCommandHandler(
			GitSCMRepositoryProvider repoProvider,
			GitReceivePackProvider uploadPackProvider) {
		this.repositoryProvider = repoProvider;
		this.receivePackProvider = uploadPackProvider;
	}

	protected void runCommand(FilteredCommand filteredCommand,
			InputStream inputStream, OutputStream outputStream,
			OutputStream errorStream, ExitCallback exitCallback,
			Properties config, AuthorizationLevel authorizationLevel) throws IOException {
		if (authorizationLevel == AuthorizationLevel.AUTH_LEVEL_READ_ONLY){
			throw new MustHaveWritePrivilagesToPushFailure("Tried to push to " + filteredCommand.getArgument());
		}
		try{
			
			String strRepoBase = config
					.getProperty(GitSCMCommandFactory.REPOSITORY_BASE);
			File repoBase = new File(strRepoBase);
	
			Repository repo = repositoryProvider.provide(repoBase, filteredCommand
					.getArgument());
	
			ReceivePack rp = receivePackProvider.provide(repo);
	
			rp.receive(inputStream, outputStream, errorStream);
			int i = 0;
		}
		catch (IOException e){
			log.error("rp caught ioe: ", e);
			throw e;
		}
	}

}

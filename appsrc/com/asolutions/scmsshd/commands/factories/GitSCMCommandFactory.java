package com.asolutions.scmsshd.commands.factories;

import java.util.Properties;

import org.apache.sshd.server.CommandFactory.Command;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.git.GitSCMCommand;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class GitSCMCommandFactory implements ISCMCommandFactory {

	public static final String REPOSITORY_BASE = "repositoryBase";

	public Command create(FilteredCommand filteredCommand,
			IProjectAuthorizer projectAuthenticator,
			IPathToProjectNameConverter pathToProjectNameConverter,
			Properties configuration) {
		return new GitSCMCommand(filteredCommand, projectAuthenticator, pathToProjectNameConverter, configuration);
	}

}

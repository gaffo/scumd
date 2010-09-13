package com.asolutions.scmsshd.commands.factories;

import java.util.Properties;

import org.apache.sshd.server.CommandFactory.Command;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.git.GitSCMCommandHandler;
import com.asolutions.scmsshd.commands.git.SCMCommand;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class GitSCMCommandFactory implements ISCMCommandFactory {

	public static final String REPOSITORY_BASE = "repositoryBase";

	public Command create(FilteredCommand filteredCommand,
			IProjectAuthorizer projectAuthorizer,
			IPathToProjectNameConverter pathToProjectNameConverter,
			Properties configuration) {
		SCMCommand retVal = new SCMCommand();
		retVal.setFilteredCommand(filteredCommand);
		retVal.setProjectAuthorizer(projectAuthorizer);
		retVal.setSCMCommandHandler(new GitSCMCommandHandler());
		retVal.setPathToProjectNameConverter(pathToProjectNameConverter);
		retVal.setConfiguration(configuration);
		return retVal;
	}

}

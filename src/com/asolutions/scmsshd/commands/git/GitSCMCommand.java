package com.asolutions.scmsshd.commands.git;

import java.util.Properties;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class GitSCMCommand extends SCMCommand {

	public GitSCMCommand(FilteredCommand filteredCommand,
						 IProjectAuthorizer projectAuthorizer, 
						 IPathToProjectNameConverter pathToProjectNameConverter, Properties configuration) {
		setFilteredCommand(filteredCommand);
		setProjectAuthorizer(projectAuthorizer);
		setSCMCommandHandler(new GitSCMCommandHandler());
		setPathToProjectNameConverter(pathToProjectNameConverter);
		setConfiguration(configuration);
	}

}

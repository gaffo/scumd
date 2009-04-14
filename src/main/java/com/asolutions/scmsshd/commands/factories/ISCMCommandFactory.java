package com.asolutions.scmsshd.commands.factories;

import java.util.Properties;

import org.apache.sshd.server.CommandFactory.Command;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public interface ISCMCommandFactory {

	Command create(FilteredCommand filteredCommand, IProjectAuthorizer mockProjAuth, IPathToProjectNameConverter pathToProjectNameConverter, Properties confi);

}

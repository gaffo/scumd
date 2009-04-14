package com.asolutions.scmsshd.commands.factories;

import java.util.Properties;

import org.apache.sshd.server.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.NoOpCommand;
import com.asolutions.scmsshd.commands.filters.BadCommandException;
import com.asolutions.scmsshd.commands.filters.IBadCommandFilter;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;

public class CommandFactoryBase implements CommandFactory {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private IProjectAuthorizer projectAuthorizer;
	private IBadCommandFilter badCommandFilter;
	private ISCMCommandFactory scmCommandFactory;
	private IPathToProjectNameConverter pathToProjectNameConverter;
	private Properties configuration;

	public CommandFactoryBase() {
	}

	public Command createCommand(String command) {
		log.info("Creating command handler for {}", command);
		try {
			FilteredCommand fc = badCommandFilter.filterOrThrow(command);
			return scmCommandFactory.create(fc, projectAuthorizer, pathToProjectNameConverter, getConfiguration());
		} catch (BadCommandException e) {
			log.error("Got Bad Command Exception For Command: [" + command
					+ "]", e);
			return new NoOpCommand();
		}
	}

	public void setProjectAuthorizor(IProjectAuthorizer projectAuthorizer) {
		this.projectAuthorizer = projectAuthorizer;
	}

	public void setBadCommandFilter(IBadCommandFilter badCommandFilter) {
		this.badCommandFilter = badCommandFilter;
	}

	public void setScmCommandFactory(ISCMCommandFactory scmCommandFactory) {
		this.scmCommandFactory = scmCommandFactory;
	}

	public IBadCommandFilter getBadCommandFilter() {
		return this.badCommandFilter;
	}

	public ISCMCommandFactory getScmCommandFactory() {
		return this.scmCommandFactory;
	}

	public void setPathToProjectNameConverter(IPathToProjectNameConverter pathToProjectNameConverter) {
		this.pathToProjectNameConverter = pathToProjectNameConverter;
	}
	
	public IPathToProjectNameConverter getPathToProjectNameConverter() {
		return pathToProjectNameConverter;
	}

	public void setConfiguration(Properties configuration) {
		this.configuration = configuration;
	}
	
	public Properties getConfiguration() {
		return configuration;
	}

}
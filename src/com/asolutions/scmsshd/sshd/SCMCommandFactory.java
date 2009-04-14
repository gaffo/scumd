package com.asolutions.scmsshd.sshd;
import org.apache.sshd.server.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.NoOpCommand;
import com.asolutions.scmsshd.commands.filters.BadCommandException;
import com.asolutions.scmsshd.commands.filters.git.GitBadCommandFilter;

public class SCMCommandFactory implements CommandFactory {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private IProjectAuthorizer projectAuthorizer;

	public SCMCommandFactory() {
	}

	public Command createCommand(String command) {
		log.info("Creating command handler for {}", command);
		try {
			GitBadCommandFilter filter = new GitBadCommandFilter();
			FilteredCommand fc = filter.filterOrThrow(command);
			return new SCMCommand(fc.getCommand(), fc.getArgument(), projectAuthorizer);
		} catch (BadCommandException e) {
			log.error("Got Bad Command Exception For Command: [" + command + "]", e);
			return new NoOpCommand();
		}
	}

    public void setProjectAuthorizor(IProjectAuthorizer projectAuthorizer) {
		this.projectAuthorizer = projectAuthorizer;
    }

}

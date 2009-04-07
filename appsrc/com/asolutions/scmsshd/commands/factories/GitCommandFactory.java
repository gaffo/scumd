package com.asolutions.scmsshd.commands.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.commands.filters.git.GitBadCommandFilter;

public class GitCommandFactory extends CommandFactoryBase {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	public GitCommandFactory() {
		setBadCommandFilter(new GitBadCommandFilter());
		setScmCommandFactory(new GitSCMCommandFactory());
	}
}

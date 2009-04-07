package com.asolutions.scmsshd.commands.filters.git;

public class GitCommandSeamer {

	private String command;

	public GitCommandSeamer(String command) {
		this.command = command;
		this.command = this.command.replaceFirst("^git (\\w)", "git-$1");
	}
	
	@Override
	public String toString() {
		return command;
	}

}

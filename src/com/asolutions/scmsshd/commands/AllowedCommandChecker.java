package com.asolutions.scmsshd.commands;

import com.asolutions.scmsshd.commands.filters.BadCommandException;
import com.asolutions.scmsshd.commands.filters.git.GitCommandSeamer;

public class AllowedCommandChecker {

	private static String commands[] = {"git-upload-pack", "git-receive-pack", };
	
	public AllowedCommandChecker(String cmd) throws BadCommandException {
		cmd = new GitCommandSeamer(cmd).toString();
		for (String toCheck: commands) {
			if (toCheck.equals(cmd)){
				return;
			}
		}
		throw new BadCommandException("Unknown Command [" + cmd + "]");
	}

}

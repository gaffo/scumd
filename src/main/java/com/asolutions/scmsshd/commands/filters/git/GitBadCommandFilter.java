package com.asolutions.scmsshd.commands.filters.git;

import java.util.regex.Pattern;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.filters.BadCommandException;
import com.asolutions.scmsshd.commands.filters.IBadCommandFilter;

public class GitBadCommandFilter implements IBadCommandFilter {
	
//	private static final Pattern commandFilter = Pattern.compile("^'/*(?P<path>[a-zA-Z0-9][a-zA-Z0-9@._-]*(/[a-zA-Z0-9][a-zA-Z0-9@._-]*)*)'$");
	private static final String commandFilter = "^'.+'$";
	private static final String[] validCommands = {"git-upload-pack", "git-receive-pack"};
	private String[] parts;

	public GitBadCommandFilter() {
	}
	
	public FilteredCommand filterOrThrow(String command) throws BadCommandException {
		command = new GitCommandSeamer(command).toString();
		checkForNewlines(command);
		parts = checkForOneArgument(command);
		throwIfContains(parts[1], "..");
		throwIfContains(parts[1], "!");
		checkAgainstPattern(parts[1]);
		checkValidCommands(parts[0]);
		parts[1] = parts[1].replaceAll("^'", "");
		parts[1] = parts[1].replaceAll("'$", "");
		return new FilteredCommand(parts[0], parts[1]);
	}

	private void checkValidCommands(String commandToCheck) throws BadCommandException {
		for (String validCommand : validCommands) {
			if (validCommand.equals(commandToCheck)){
				return;
			}
		}
		throw new BadCommandException("Unknown Command: " + commandToCheck);
	}

	private void checkAgainstPattern(String command) throws BadCommandException {
		if (!Pattern.matches(commandFilter, command))
		{
			throw new BadCommandException();
		}
	}

	private String[] checkForOneArgument(String command) throws BadCommandException {
		String[] parts = command.split(" ");
		int count = parts.length;
		if (count == 1 || count > 2)
		{
			throw new BadCommandException("Invalid Number Of Arguments (must be 1): " + count);
		}
		return parts;
	}

	private void checkForNewlines(String command) throws BadCommandException {
		throwIfContains(command, "\n");
		throwIfContains(command, "\r");
	}

	private void throwIfContains(String command, String toCheckFor) throws BadCommandException {
		if (command.contains(toCheckFor)){
			throw new BadCommandException("Cant Contain: " + toCheckFor);
		}
	}
	
	public String getCommand()
	{
		return parts[0];
	}
	
	public String getArgument()
	{
		return parts[1];
	}

}

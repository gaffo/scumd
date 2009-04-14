package com.asolutions.scmsshd.commands;

public class FilteredCommand {
	
	private String command;
	private String argument;

	public FilteredCommand() {
	}
	
	public FilteredCommand(String command, String argument) {
		this.command = command;
		this.argument = argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

	public String getArgument() {
		return this.argument;
	}
	
	@Override
	public String toString()
	{
		return "Filtered Command: " + getCommand() + ", " + getArgument();
	}

}

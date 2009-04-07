package com.asolutions.scmsshd.commands.filters;

public class BadCommandException extends Exception {

	public BadCommandException(String reason) {
		super(reason);
	}

	public BadCommandException() {
	}

}

package com.asolutions.scmsshd.commands.filters;

public class BadCommandException extends Exception {

	private static final long serialVersionUID = 4904880805323643780L;

	public BadCommandException(String reason) {
		super(reason);
	}

	public BadCommandException() {
	}

}

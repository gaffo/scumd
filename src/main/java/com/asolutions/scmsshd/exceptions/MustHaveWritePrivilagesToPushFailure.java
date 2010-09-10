package com.asolutions.scmsshd.exceptions;

public class MustHaveWritePrivilagesToPushFailure extends Failure{

	private static final long serialVersionUID = 7650486977318806435L;

	public MustHaveWritePrivilagesToPushFailure(String specifics) {
		super(0, "You must have write privilages to be able to push", specifics);
	}
	
}

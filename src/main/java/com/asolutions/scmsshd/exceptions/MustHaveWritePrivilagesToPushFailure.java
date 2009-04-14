package com.asolutions.scmsshd.exceptions;

public class MustHaveWritePrivilagesToPushFailure extends Failure{

	public MustHaveWritePrivilagesToPushFailure(String specifics) {
		super(0, "You must have write privilages to be able to push", specifics);
	}
	
}

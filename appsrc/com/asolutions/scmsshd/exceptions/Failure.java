package com.asolutions.scmsshd.exceptions;

public class Failure extends RuntimeException {

	private int resultCode;
	private final String genericError;
	private final String specifics;

	public Failure(int i, String genericError, String specifics) {
		this.resultCode = i;
		this.genericError = genericError;
		this.specifics = specifics;
	}
	
	public int getResultCode() {
		return resultCode;
	}
	
	public String toFormattedErrorMessage(){
		StringBuilder builder = new StringBuilder();
		builder.append("**********************************\n");
		builder.append("\n");
		builder.append(genericError);
		builder.append("\n");
		builder.append("\n");
		builder.append("Specifics:\n");
		builder.append("  ").append(specifics).append("\n");
		builder.append("\n");
		builder.append("**********************************\n");
		return builder.toString();
	}

}

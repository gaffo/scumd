package com.asolutions.scmsshd.converters.path;

import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public interface IPathToProjectNameConverter {

	public abstract String convert(String toParse)
			throws UnparsableProjectException;

}
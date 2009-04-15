package com.asolutions.scmsshd.test.integration.util;

import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class ConstantProjectNameConverter implements
		IPathToProjectNameConverter {

	private String pathName;

	public ConstantProjectNameConverter(String pathName) {
		this.pathName = pathName;
	}

	public String convert(String toParse) throws UnparsableProjectException {
		return pathName;
	}

}

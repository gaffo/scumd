package com.asolutions.scmsshd.test.integration.util;

import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class ConstantProjectNameConverter implements
		IPathToProjectNameConverter {

	public ConstantProjectNameConverter() {
	}

	public String convert(String toParse) throws UnparsableProjectException {
		return "constant";
	}

}

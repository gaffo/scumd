package com.asolutions.scmsshd.converters.path.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public abstract class AMatchingGroupPathToProjectNameConverter implements IPathToProjectNameConverter{

	public AMatchingGroupPathToProjectNameConverter() {
		super();
	}

	public String convert(String toParse) throws UnparsableProjectException {
		Matcher match = getPattern().matcher(toParse);
		if (match.find()){
			return match.group(1);
		}
		else{
			throw new UnparsableProjectException("Could Not Parse: [" + toParse + "] With [" + getPattern().toString() + "]");
		}
	}
	
	public abstract Pattern getPattern();

}
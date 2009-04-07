package com.asolutions.scmsshd.converters.path.regexp;

import java.util.regex.Pattern;

public class ConfigurablePathToProjectConverter extends
		AMatchingGroupPathToProjectNameConverter {
	
	private Pattern projectPattern;

	public void setProjectPattern(String pattern){
		this.projectPattern = Pattern.compile(pattern);
	}

	@Override
	public Pattern getPattern() {
		return projectPattern;
	}

}

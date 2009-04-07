package com.asolutions.asynchrony.customizations;

import java.util.regex.Pattern;

import com.asolutions.scmsshd.converters.path.regexp.AMatchingGroupPathToProjectNameConverter;


public class AsynchronyPathToProjectNameConverter extends AMatchingGroupPathToProjectNameConverter {

	private static Pattern pattern = Pattern.compile("(proj-\\d+)");
	
	public AsynchronyPathToProjectNameConverter() {
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}

}

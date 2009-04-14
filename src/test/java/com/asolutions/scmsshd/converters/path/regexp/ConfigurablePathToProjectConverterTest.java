package com.asolutions.scmsshd.converters.path.regexp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.asolutions.MockTestCase;
import com.asolutions.asynchrony.customizations.AsynchronyPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;
public class ConfigurablePathToProjectConverterTest extends MockTestCase{
	
	@Test
	public void testMatchReturnsTrue() throws Exception {
		ConfigurablePathToProjectConverter converter = new ConfigurablePathToProjectConverter();
		converter.setProjectPattern("(\\d)");
		assertEquals("2", converter.convert("'/proj-2/git.git'"));
	}
	
	@Test
	public void testNoMatchThrowsException() throws Exception {
		ConfigurablePathToProjectConverter converter = new ConfigurablePathToProjectConverter();
		converter.setProjectPattern("(\\d)");
		try{
			new AsynchronyPathToProjectNameConverter().convert("");
			fail("Did not throw");
		}
		catch (UnparsableProjectException e){
		}
	}

}

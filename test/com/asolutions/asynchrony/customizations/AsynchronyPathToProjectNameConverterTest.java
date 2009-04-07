package com.asolutions.asynchrony.customizations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class AsynchronyPathToProjectNameConverterTest {

	@Test
	public void testProjectGetter() throws Exception {
		assertEquals("proj-2", new AsynchronyPathToProjectNameConverter().convert("'/proj-2/git.git'"));
	}
	
	@Test
	public void testGetterThrowsUnparsableExceptionOnBadUrl() throws Exception {
		try{
			new AsynchronyPathToProjectNameConverter().convert("blork");
			fail("Didn't Throw");
		}
		catch (UnparsableProjectException e){
		}
	}
	
}

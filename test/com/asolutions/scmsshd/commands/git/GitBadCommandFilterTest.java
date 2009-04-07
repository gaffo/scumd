package com.asolutions.scmsshd.commands.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.filters.BadCommandException;
import com.asolutions.scmsshd.commands.filters.git.GitBadCommandFilter;


public class GitBadCommandFilterTest {
	
	@Test 
	public void testCorrect() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git-upload-pack 'bob'");
		assertEquals("git-upload-pack", fc.getCommand());
		assertEquals("bob", fc.getArgument());
	}
	
	@Test
	public void testGitUploadPack() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git-upload-pack 'bob'");
		assertEquals("git-upload-pack", fc.getCommand());
		assertEquals("bob", fc.getArgument());
	}
	
	@Test
	public void testGetUploadPackSpaced() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git upload-pack 'bob'");
		assertEquals("git-upload-pack", fc.getCommand());
		assertEquals("bob", fc.getArgument());
	}
	
	@Test
	public void testGetReceivePack() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git-receive-pack 'bob'");
		assertEquals("git-receive-pack", fc.getCommand());
		assertEquals("bob", fc.getArgument());
	}
	
	@Test
	public void testGetReceivePackSpaced() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git receive-pack 'bob'");
		assertEquals("git-receive-pack", fc.getCommand());
		assertEquals("bob", fc.getArgument());
	}
	
	@Test
	public void testUnknownCommand() throws Exception {
		assertThrows("git-unknown-pack 'bob'");
	}
	
	@Test
	public void testUploadPack() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git-upload-pack '/proj-2/deuce.git'");
		assertEquals("git-upload-pack", fc.getCommand());
		assertEquals("/proj-2/deuce.git", fc.getArgument());
	}
	
	@Test 
	public void testCorrectWithSeam() throws Exception {
		GitBadCommandFilter filter = new GitBadCommandFilter();
		FilteredCommand fc = filter.filterOrThrow("git upload-pack 'bob'");
		assertEquals("git-upload-pack", fc.getCommand());
	}
	
	@Test
	public void testBadNewline() throws Exception {
		assertThrows("ev\nil");
	}
	
	@Test
	public void testBadSlashR() throws Exception {
		assertThrows("ev\ril");
	}
	
	@Test
	public void testNoArgs() throws Exception {
		assertThrows("git-upload-pack");
	}
	
	@Test
	public void test2Args() throws Exception {
		assertThrows("git-upload-pack bob tom");
	}
	
	@Test
	public void testUnquoted() throws Exception {
		assertThrows("git-upload-pack bob");
	}
	
	@Test
	public void testWithUnsafeBang() throws Exception {
		assertThrows("git-upload-pack 'ev!l'");
	}
	
	@Test
	public void testWithUnsafeDotDot() throws Exception {
		assertThrows("git-upload-pack 'something/../evil'");
	}
	
	private void assertThrows(String toCheck) {
		try{
			GitBadCommandFilter filter = new GitBadCommandFilter();
			filter.filterOrThrow(toCheck);
			fail("didn't throw");
		}
		catch (BadCommandException e){
		}
	}

}

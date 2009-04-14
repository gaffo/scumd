package com.asolutions.scmsshd.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.spearce.jgit.lib.Repository;


public class PushTest extends IntegrationTestCase {
	
	@Test
	public void testPush() throws Exception {
		Repository repo = new Repository(makeScratchDir());
		repo.create();
	}
	
}

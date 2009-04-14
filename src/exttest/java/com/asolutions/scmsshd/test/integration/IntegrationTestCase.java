package com.asolutions.scmsshd.test.integration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.After;

public class IntegrationTestCase {

	private ArrayList<File> scratchDirs = new ArrayList<File>();

	public IntegrationTestCase() {
		super();
	}
	
	@After
	public void removeScratchDirs() throws IOException{
		for (File dir : scratchDirs) {
			FileUtils.deleteDirectory(dir);
		}
	}

	public File makeScratchDir() {
		File basedir = new File(System.getProperty("java.io.tmpdir"));
		File scratchDir = new File(basedir, "scumd-test-" + System.currentTimeMillis());
		scratchDirs.add(scratchDir);
		return scratchDir;
	}

}
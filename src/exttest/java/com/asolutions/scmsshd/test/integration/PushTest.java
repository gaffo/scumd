package com.asolutions.scmsshd.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spearce.jgit.lib.Ref;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.lib.TextProgressMonitor;

import com.asolutions.scmsshd.SCuMD;
import com.asolutions.scmsshd.authenticators.AlwaysPassPublicKeyAuthenticator;
import com.asolutions.scmsshd.authorizors.AlwaysPassProjectAuthorizer;
import com.asolutions.scmsshd.commands.factories.GitCommandFactory;
import com.asolutions.scmsshd.commands.factories.GitSCMCommandFactory;
import com.asolutions.scmsshd.test.integration.util.ConstantProjectNameConverter;


public class PushTest extends IntegrationTestCase {
	
	private static final String ORIGIN = "origin";
	private static final String REFSPEC = "master";
	private File toRepoDir;
	private File fromRepoDir;
	private Repository fromRepository;
	private Ref fromRefMaster;

	@Before
	public void createRepoClone() throws IOException{
		toRepoDir = makeScratchDir();
		createBareRepo(toRepoDir);
		fromRepoDir = makeScratchDir();
		// clone the repo to tmp so we don't break our own source
		FileUtils.copyDirectory(new File(".git"), fromRepoDir);
		fromRepository = new Repository(fromRepoDir);
		fromRefMaster = fromRepository.getRef(REFSPEC);
	}
	
	@After
	public void closeRepos()
	{
		fromRepository.close();
	}
	
	@Test
	public void testPush() throws Exception {
		addRemoteConfigForLocalGitDirectory(fromRepository, toRepoDir, ORIGIN);
		push(ORIGIN, REFSPEC, fromRepository);
		
		assertPushOfMaster(fromRefMaster);
	}
	
	@Test
	public void testPushSCuMD() throws Exception {
		final SCuMD sshd = new SCuMD();
		try{
			int serverPort = generatePort();
			
			System.out.println("Running on port: " + serverPort);
			sshd.setPort(serverPort);
			sshd.setKeyPairProvider(new FileKeyPairProvider(new String[] { "src/main/resources/ssh_host_rsa_key", 
																		   "src/main/resources/ssh_host_dsa_key" }));
			sshd.setPublickeyAuthenticator(new AlwaysPassPublicKeyAuthenticator());
			
			GitCommandFactory factory = new GitCommandFactory();
			factory.setPathToProjectNameConverter(new ConstantProjectNameConverter());
			factory.setProjectAuthorizor(new AlwaysPassProjectAuthorizer());
	
	        Properties config = new Properties();
	        config.setProperty(GitSCMCommandFactory.REPOSITORY_BASE, toRepoDir.getParent());
	        factory.setConfiguration(config);
	        sshd.setCommandFactory(factory);
	        new Thread(){
	        	@Override
	        	public void run() {
	        		try {
						sshd.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	}
	        }.start();
	        Thread.sleep(3000);
			
			addRemoteConfigForRemoteGitDirectory(fromRepository, ORIGIN, serverPort, toRepoDir.getName());
			push(ORIGIN, REFSPEC, fromRepository);
			
			assertPushOfMaster(fromRefMaster);
		}
		finally{
			sshd.stop();
		}
	}

	private void assertPushOfMaster(Ref fromRefMaster) throws IOException {
		Repository to = new Repository(toRepoDir);
		Ref toRefMaster = to.getRef(REFSPEC);
		assertEquals(fromRefMaster.getObjectId(), toRefMaster.getObjectId());
	}
	
}

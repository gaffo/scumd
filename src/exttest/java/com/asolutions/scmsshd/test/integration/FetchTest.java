package com.asolutions.scmsshd.test.integration;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.junit.Test;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.FetchResult;

import com.asolutions.scmsshd.SCuMD;
import com.asolutions.scmsshd.authenticators.AlwaysPassPublicKeyAuthenticator;
import com.asolutions.scmsshd.authorizors.AlwaysPassProjectAuthorizer;
import com.asolutions.scmsshd.commands.factories.GitCommandFactory;
import com.asolutions.scmsshd.commands.factories.GitSCMCommandFactory;
import com.asolutions.scmsshd.test.integration.util.ConstantProjectNameConverter;


public class FetchTest extends IntegrationTestCase {
	
	@Test
	public void testFetchLocal() throws Exception {
		File gitDir = new File(".git");
		String remoteName = "origin";
		
		Repository db = createCloneToRepo();
		addRemoteConfigForLocalGitDirectory(db, gitDir, remoteName);
		FetchResult r = cloneFromRemote(db, remoteName);
		assert(r.getTrackingRefUpdates().size() > 0);
	}
	
	@Test
	public void testFetchFromScumd() throws Exception {
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
	        config.setProperty(GitSCMCommandFactory.REPOSITORY_BASE, System.getProperty("user.dir"));
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
	        
			Repository db = createCloneToRepo();
			String remoteName = "origin";
			File gitDir = new File(".git");
			addRemoteConfigForRemoteGitDirectory(db, remoteName, serverPort, ".git");
			FetchResult r = cloneFromRemote(db, remoteName);
			System.out.println("results = " + r.getTrackingRefUpdates().size());
			assertTrue(r.getTrackingRefUpdates().size() > 0);
		}
		finally{
			sshd.stop();
		}
	}

	
}

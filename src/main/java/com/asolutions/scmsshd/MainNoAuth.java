package com.asolutions.scmsshd;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.Compression;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.compression.CompressionDelayedZlib;
import org.apache.sshd.common.compression.CompressionNone;
import org.apache.sshd.common.compression.CompressionZlib;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.PasswordAuthenticator;

import com.asolutions.asynchrony.customizations.AsynchronyPathToProjectNameConverter;
import com.asolutions.scmsshd.authenticators.LDAPAuthenticator;
import com.asolutions.scmsshd.authenticators.PassIfAnyInCollectionPassAuthenticator;
import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.authorizors.PassIfAnyInCollectionPassAuthorizor;
import com.asolutions.scmsshd.commands.factories.CommandFactoryBase;
import com.asolutions.scmsshd.commands.factories.GitCommandFactory;
import com.asolutions.scmsshd.commands.factories.GitSCMCommandFactory;
import com.asolutions.scmsshd.converters.path.regexp.AMatchingGroupPathToProjectNameConverter;
import com.asolutions.scmsshd.ldap.LDAPProjectAuthorizer;
import com.asolutions.scmsshd.sshd.AlwaysFailPublicKeyAuthenticator;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;


public class MainNoAuth {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Server");
		final SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(Integer.parseInt(args[0]));
		String basedir = args[1];
		
		setupAuthenticators(sshd);

        setCommandFactory(sshd, basedir);
        
		sshd.setPublickeyAuthenticator(new AlwaysFailPublicKeyAuthenticator());

		sshd.setKeyPairProvider(new FileKeyPairProvider(new String[] { "src/main/resources/ssh_host_rsa_key", "src/main/resources/ssh_host_dsa_key" }));
		
	    sshd.setCompressionFactories(Arrays.<NamedFactory<Compression>>asList(
	             new CompressionNone.Factory()));
		
	    try{
	    	sshd.start();
	    }
	    catch (Exception e){
    		System.out.println("Aborting:" + e);
	    	sshd.stop();
	    }
	    catch (Throwable e){
    		System.out.println("tAborting:" + e);
	    	sshd.stop();
	    }
	} 

	private static void setupAuthenticators(SshServer sshd) {
		sshd.setPasswordAuthenticator(new PasswordAuthenticator(){
			public Object authenticate(String username, String password) {
				return true;
			}});
	}

	private static void setCommandFactory(SshServer sshd, String basedir)
			throws NamingException {
		CommandFactoryBase commandFactory = new GitCommandFactory();
		commandFactory.setPathToProjectNameConverter(new AMatchingGroupPathToProjectNameConverter(){
			@Override
			public Pattern getPattern() {
				return Pattern.compile("(repo\\d+)");
			}});
        
        setupAuthorizers(commandFactory);
        
        Properties config = new Properties();
        config.setProperty(GitSCMCommandFactory.REPOSITORY_BASE, basedir);
        commandFactory.setConfiguration(config);
        
		sshd.setCommandFactory(commandFactory);
	}

	private static void setupAuthorizers(CommandFactoryBase commandFactory)
			throws NamingException {
		commandFactory.setProjectAuthorizor(new com.asolutions.scmsshd.authorizors.AlwaysPassProjectAuthorizer());
	}
	
}

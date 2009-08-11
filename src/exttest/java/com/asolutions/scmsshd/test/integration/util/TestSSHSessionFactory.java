package com.asolutions.scmsshd.test.integration.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.spearce.jgit.transport.OpenSshConfig;
import org.spearce.jgit.transport.SshSessionFactory;
import org.spearce.jgit.util.FS;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class TestSSHSessionFactory extends SshSessionFactory {
	
	private Set<String> loadedIdentities;

	private JSch userJSch;

	private OpenSshConfig config;

	@Override
	public synchronized Session getSession(String user, String pass,
			String host, int port) throws JSchException {
		final OpenSshConfig.Host hc = getConfig().lookup(host);
		host = hc.getHostName();
		if (port <= 0)
			port = hc.getPort();
		if (user == null)
			user = hc.getUser();

		final Session session = getUserJSch().getSession(user, host, port);
		if (hc.getIdentityFile() != null){
			addIdentity(hc.getIdentityFile());
		}
		if (pass != null){
			session.setPassword(pass);
		}
		session.setConfig("StrictHostKeyChecking", "no");
		final String pauth = hc.getPreferredAuthentications();
		if (pauth != null){
			session.setConfig("PreferredAuthentications", pauth);
		}
		return session;
	}

	private void addIdentity(final File identityFile) throws JSchException {
		final String path = identityFile.getAbsolutePath();
		if (!loadedIdentities.contains(path)) {
			userJSch.addIdentity(path);
			loadedIdentities.add(path);
		}
	}

	private void knownHosts(final JSch sch) throws JSchException {
		final File home = FS.userHome();
		if (home == null)
			return;
		final File known_hosts = new File(new File(home, ".ssh"), "known_hosts");
		try {
			final FileInputStream in = new FileInputStream(known_hosts);
			try {
				sch.setKnownHosts(in);
			} finally {
				in.close();
			}
		} catch (FileNotFoundException none) {
			// Oh well. They don't have a known hosts in home.
		} catch (IOException err) {
			// Oh well. They don't have a known hosts in home.
		}
	}

	private OpenSshConfig getConfig() {
		if (config == null)
			config = OpenSshConfig.get();
		return config;
	}
	
	private JSch getUserJSch() throws JSchException {
		if (userJSch == null) {
			loadedIdentities = new HashSet<String>();
			userJSch = new JSch();
			knownHosts(userJSch);
			identities();
		}
		return userJSch;
	}
	
	private void identities() {
		final File home = FS.userHome();
		if (home == null)
			return;
		final File sshdir = new File(home, ".ssh");
		final File[] keys = sshdir.listFiles();
		if (keys == null)
			return;
		for (int i = 0; i < keys.length; i++) {
			final File pk = keys[i];
			final String n = pk.getName();
			if (!n.endsWith(".pub"))
				continue;
			final File k = new File(sshdir, n.substring(0, n.length() - 4));
			if (!k.isFile())
				continue;

			try {
				addIdentity(k);
			} catch (JSchException e) {
				continue;
			}
		}
	}
//
//	public OutputStream getErrorStream() {
//		return new OutputStream() {
//			private StringBuilder all = new StringBuilder();
//
//			private StringBuilder sb = new StringBuilder();
//
//			public String toString() {
//				String r = all.toString();
//				while (r.endsWith("\n"))
//					r = r.substring(0, r.length() - 1);
//				return r;
//			}
//
//			@Override
//			public void write(final int b) throws IOException {
//				if (b == '\r') {
//					System.err.print('\r');
//					return;
//				}
//
//				sb.append((char) b);
//
//				if (b == '\n') {
//					final String line = sb.toString();
//					System.err.print(line);
//					all.append(line);
//					sb = new StringBuilder();
//				}
//			}
//		};
//	}

}

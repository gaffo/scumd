package com.asolutions.scmsshd.sshd;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import org.apache.sshd.server.CommandFactory.Command;
import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.apache.sshd.server.CommandFactory.SessionAware;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.UploadPack;

public class SCMCommand implements Command, SessionAware {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final String command;
	private final String project;
	private ServerSession session;
	private final IProjectAuthorizer projectAuthorizer;

	private OutputStream err;
	private ExitCallback callback;
	private InputStream in;
	private OutputStream out;

	public SCMCommand(String command, String argument,
			IProjectAuthorizer projectAuthorizer) {
		this.command = "/usr/local/bin/" + command;
		this.project = "/var/git" + argument.replaceAll("'", "");
		this.projectAuthorizer = projectAuthorizer;
	}

	public void setErrorStream(OutputStream err) {
		this.err = err;
		log.debug("setErrorStream");
	}

	public void setExitCallback(ExitCallback callback) {
		this.callback = callback;
		log.debug("setExitCallback");
	}

	public void setInputStream(InputStream in) {
		this.in = in;
		log.debug("setInputStream");
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
		log.debug("setOutputStream");
	}

	public void start() {
		new Thread("Execute:" + System.currentTimeMillis()) {
			@Override
			public void run() {
				runImp();
			}
		}.start();
	}

	private void runImp() {
		int rc = 0;
		try {
			try {
				execute();
			} catch (IOException e) {
				log.error("Error running impl" , e);
			}
		} finally {
			try {
				out.flush();
			} catch (IOException err) {
				log.error("Error running impl" , err);
			}
			try {
				err.flush();
			} catch (IOException err) {
				log.error("Error running impl" , err);
			}
			callback.onExit(-1);
		}
	}

	private void execute() throws IOException {
		String commandAry[] = { command, project };
		String string = command + " " + project;
		log.debug("Creating Process: {}", string);
		System.out.println("Creating Process: " + string);
		Repository repo = new Repository(
				new File("/shared/home/mike.gaffney/workspace/ace-deuce/.git"));
		UploadPack up = new UploadPack(repo);
		System.out.println("upload start");
		up.upload(in, out, err);
		System.out.println("upload start");
	}

	public void setSession(ServerSession session) {
		this.session = session;
	}

}

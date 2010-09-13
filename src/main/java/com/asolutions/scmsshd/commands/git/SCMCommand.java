package com.asolutions.scmsshd.commands.git;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.Command;
import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.apache.sshd.server.CommandFactory.SessionAware;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.handlers.ISCMCommandHandler;
import com.asolutions.scmsshd.converters.path.IPathToProjectNameConverter;
import com.asolutions.scmsshd.sshd.IProjectAuthorizer;
import com.asolutions.scmsshd.sshd.UnparsableProjectException;

public class SCMCommand implements Command, SessionAware{
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private InputStream inputStream;
	private OutputStream outputStream;
	private OutputStream errorStream;
	private ExitCallback exitCallback;
	private ServerSession session;
	private FilteredCommand filteredCommand;
	private IProjectAuthorizer projectAuthorizer;
	private IPathToProjectNameConverter pathToProjectNameConverter;
	private ISCMCommandHandler sCMCommandHandler;
	private Properties configuration;
	
	public SCMCommand() {
		super();
	}

	public SCMCommand(FilteredCommand filteredCommand,
			IProjectAuthorizer projectAuthorizer,
			ISCMCommandHandler anSCMCommandHandler,
			IPathToProjectNameConverter pathToProjectNameConverter,
			Properties configuration) {
				this.filteredCommand = filteredCommand;
				this.projectAuthorizer = projectAuthorizer;
				this.sCMCommandHandler = anSCMCommandHandler;
				this.pathToProjectNameConverter = pathToProjectNameConverter;
				this.configuration = configuration;
	}

	public ExitCallback getExitCallback() {
		return exitCallback;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	public OutputStream getErrorStream() {
		return errorStream;
	}
	
	public ServerSession getSession() {
		return session;
	}
	
	public void setErrorStream(OutputStream err) {
		this.errorStream = err;
	}

	public void setExitCallback(ExitCallback callback) {
		this.exitCallback = callback;
	}
	
	public void setSession(ServerSession session) {
		this.session = session;
	}

	public void setInputStream(InputStream in) {
		this.inputStream = in;
	}

	public void setOutputStream(OutputStream out) {
		this.outputStream = out;
	}

	public String getUsername() {
		if (session != null){
			return session.getUsername();
		}
		else {
			return null;
		}
	}

	public FilteredCommand getFilteredCommand() {
		return filteredCommand;
	}

	public IProjectAuthorizer getProjectAuthorizer() {
		return projectAuthorizer;
	}
	
	public IPathToProjectNameConverter getPathToProjectNameConverter() {
		return pathToProjectNameConverter;
	}
	
	public ISCMCommandHandler getSCMCommandHandler() {
		return sCMCommandHandler;
	}

	public void start() throws IOException {
		new Thread("Execute:" + System.currentTimeMillis()) {
			@Override
			public void run() {
				runImpl();
			}
		}.start();
	}
	
	protected void runImpl()
	{
		try {
			String argument = filteredCommand.getArgument();
			String project = pathToProjectNameConverter.convert(argument);
			String username = getUsername();
			AuthorizationLevel result = projectAuthorizer.userIsAuthorizedForProject(username, project);
			if (result != null)
			{
				sCMCommandHandler.execute(filteredCommand, 
										  getInputStream(), 
										  getOutputStream(), 
										  getErrorStream(), 
										  getExitCallback(),
										  getConfiguration(),
										  result);
			}
			else
			{
				getExitCallback().onExit(1);
			}
		} catch (UnparsableProjectException e) {
			log.error("Error running impl" , e);
		}
	}

	public Properties getConfiguration() {
		return configuration;
	}

}
package com.asolutions.scmsshd.commands.git;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.handlers.ISCMCommandHandler;

public class GitSCMCommandHandler implements ISCMCommandHandler {
	
	private ISCMCommandHandler uploadPackHandler;
	private ISCMCommandHandler receivePackHandler;

	public GitSCMCommandHandler() {
		this(new GitUploadPackSCMCommandHandler(), new GitReceivePackSCMCommandHandler());
	}

	public GitSCMCommandHandler(ISCMCommandHandler uploadPackHandler,
			                    ISCMCommandHandler receivePackHandler) {
		this.uploadPackHandler = uploadPackHandler;
		this.receivePackHandler = receivePackHandler;
	}

	public void execute(FilteredCommand filteredCommand,
			InputStream inputStream, OutputStream outputStream,
			OutputStream errorStream, ExitCallback exitCallback, 
			Properties configuration,
			AuthorizationLevel authorizationLevel) {
		ISCMCommandHandler handler = null;
		if ("git-upload-pack".equals(filteredCommand.getCommand())){
			handler = uploadPackHandler;
		}
		else if ("git-receive-pack".equals(filteredCommand.getCommand()))
		{
			handler = receivePackHandler;
		}
		if (handler != null)
		{
			handler.execute(filteredCommand, inputStream, outputStream, errorStream, exitCallback, configuration, authorizationLevel);
		}
	}

}

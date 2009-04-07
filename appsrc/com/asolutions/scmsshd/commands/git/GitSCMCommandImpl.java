package com.asolutions.scmsshd.commands.git;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;
import com.asolutions.scmsshd.commands.handlers.ISCMCommandHandler;

public abstract class GitSCMCommandImpl implements ISCMCommandHandler {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public GitSCMCommandImpl() {
		super();
	}

	public void execute(FilteredCommand filteredCommand,
			InputStream inputStream, OutputStream outputStream,
			OutputStream errorStream, ExitCallback exitCallback,
			Properties config, AuthorizationLevel authorizationLevel) {
		try {
			try {
				runCommand(filteredCommand, inputStream, outputStream,
						errorStream, exitCallback, config, authorizationLevel);
			} catch (IOException e) {
				log.error("Error Executing " + filteredCommand, e);
			}
			log.info("command completed normally");
		} finally {
			try {
				outputStream.flush();
			} catch (IOException err) {
				log.error("Error Executing " + filteredCommand, err);
			}
			try {
				errorStream.flush();
			} catch (IOException err) {
				log.error("Error Executing " + filteredCommand, err);
			}
			exitCallback.onExit(0);
		}

	}

	protected abstract void runCommand(FilteredCommand filteredCommand,
									   InputStream inputStream, OutputStream outputStream,
			                           OutputStream errorStream, 
			                           ExitCallback exitCallback, 
			                           Properties config, 
			                           AuthorizationLevel authorizationLevel) throws IOException;

}
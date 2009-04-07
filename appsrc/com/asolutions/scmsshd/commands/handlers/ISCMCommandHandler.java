package com.asolutions.scmsshd.commands.handlers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.sshd.server.CommandFactory.ExitCallback;

import com.asolutions.scmsshd.authorizors.AuthorizationLevel;
import com.asolutions.scmsshd.commands.FilteredCommand;

public interface ISCMCommandHandler {

	void execute(FilteredCommand filteredCommand, InputStream inputStream,
			OutputStream outputStream, OutputStream errorStream,
			ExitCallback exitCallback, Properties configuration, AuthorizationLevel authorizationLevel);

}

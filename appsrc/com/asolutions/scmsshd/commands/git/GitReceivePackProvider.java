package com.asolutions.scmsshd.commands.git;

import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.ReceivePack;

public class GitReceivePackProvider {

	public ReceivePack provide(Repository repository) {
		return new ReceivePack(repository);
	}

}

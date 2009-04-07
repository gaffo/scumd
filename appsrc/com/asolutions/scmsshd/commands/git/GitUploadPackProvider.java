package com.asolutions.scmsshd.commands.git;

import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.UploadPack;

public class GitUploadPackProvider {

	public UploadPack provide(Repository repository) {
		return new UploadPack(repository);
	}

}

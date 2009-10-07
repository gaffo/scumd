package com.asolutions.scmsshd.commands.git;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spearce.jgit.lib.Repository;

public class GitSCMRepositoryProvider {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private static HashMap<String, Repository> repositoryCache = new HashMap<String, Repository>(); 
	
	public Repository provide(File base, String argument) throws IOException {
		synchronized (repositoryCache) {
			if (repositoryCache.containsKey(argument)){
				log.info("Using Cached Repo " + argument);
				return repositoryCache.get(argument);
			}
			File pathToRepo = new File(base, argument);
			log.info("Creating Repo: " + pathToRepo.getAbsolutePath());
			Repository repo = new Repository(pathToRepo);
			log.info("...created");
			repositoryCache.put(argument, repo);
			return repo;
		}
	}

}

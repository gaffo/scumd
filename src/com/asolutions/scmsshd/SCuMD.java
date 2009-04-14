package com.asolutions.scmsshd;

import org.apache.sshd.SshServer;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SCuMD extends SshServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1)
		{
			System.err.println("Usage: SCuMD pathToConfigFile");
			return;
		}
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(args[0]);
	}

}

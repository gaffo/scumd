package com.asolutions.scmsshd;

import java.util.Arrays;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.Cipher;
import org.apache.sshd.common.Compression;
import org.apache.sshd.common.KeyExchange;
import org.apache.sshd.common.Mac;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.Signature;
import org.apache.sshd.common.cipher.AES128CBC;
import org.apache.sshd.common.cipher.AES192CBC;
import org.apache.sshd.common.cipher.AES256CBC;
import org.apache.sshd.common.cipher.BlowfishCBC;
import org.apache.sshd.common.cipher.TripleDESCBC;
import org.apache.sshd.common.compression.CompressionNone;
import org.apache.sshd.common.mac.HMACMD5;
import org.apache.sshd.common.mac.HMACMD596;
import org.apache.sshd.common.mac.HMACSHA1;
import org.apache.sshd.common.mac.HMACSHA196;
import org.apache.sshd.common.random.BouncyCastleRandom;
import org.apache.sshd.common.random.JceRandom;
import org.apache.sshd.common.random.SingletonRandomFactory;
import org.apache.sshd.common.signature.SignatureDSA;
import org.apache.sshd.common.signature.SignatureRSA;
import org.apache.sshd.common.util.SecurityUtils;
import org.apache.sshd.server.ServerChannel;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthPassword;
import org.apache.sshd.server.auth.UserAuthPublicKey;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.kex.DHG1;
import org.apache.sshd.server.kex.DHG14;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SCuMD extends SshServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: SCuMD pathToConfigFile");
			return;
		}
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
				args[0]);
	}

	public SCuMD() {
		if (SecurityUtils.isBouncyCastleRegistered()) {
			setKeyExchangeFactories(Arrays.<NamedFactory<KeyExchange>> asList(new DHG14.Factory(), new DHG1.Factory()));
			setRandomFactory(new SingletonRandomFactory(new BouncyCastleRandom.Factory()));
		} else {
			setKeyExchangeFactories(Arrays.<NamedFactory<KeyExchange>> asList(new DHG1.Factory()));
			setRandomFactory(new SingletonRandomFactory(new JceRandom.Factory()));
		}
		setUserAuthFactories(Arrays.<NamedFactory<UserAuth>>asList(
                new UserAuthPassword.Factory(),
                new UserAuthPublicKey.Factory()
                ));
		setCipherFactories(Arrays.<NamedFactory<Cipher>> asList(new AES128CBC.Factory(), 
				new TripleDESCBC.Factory(),
				new BlowfishCBC.Factory(), 
				new AES192CBC.Factory(),
				new AES256CBC.Factory()));
		setCompressionFactories(Arrays.<NamedFactory<Compression>> asList(new CompressionNone.Factory()));

		setMacFactories(Arrays.<NamedFactory<Mac>> asList(new HMACMD5.Factory(), 
														  new HMACSHA1.Factory(),
														  new HMACMD596.Factory(), 
														  new HMACSHA196.Factory()));

        setChannelFactories(Arrays.<NamedFactory<ServerChannel>>asList(
                new ChannelSession.Factory()));
		setSignatureFactories(Arrays.<NamedFactory<Signature>> asList(new SignatureDSA.Factory(), new SignatureRSA.Factory()));
	}

}

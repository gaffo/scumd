package com.asolutions.scmsshd.test.integration;

import static org.spearce.jgit.lib.Constants.R_HEADS;
import static org.spearce.jgit.lib.Constants.R_REMOTES;
import static org.spearce.jgit.lib.Constants.R_TAGS;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.spearce.jgit.errors.NotSupportedException;
import org.spearce.jgit.errors.TransportException;
import org.spearce.jgit.lib.Constants;
import org.spearce.jgit.lib.NullProgressMonitor;
import org.spearce.jgit.lib.ObjectId;
import org.spearce.jgit.lib.Ref;
import org.spearce.jgit.lib.RefUpdate;
import org.spearce.jgit.lib.Repository;
import org.spearce.jgit.transport.FetchResult;
import org.spearce.jgit.transport.PushResult;
import org.spearce.jgit.transport.RefSpec;
import org.spearce.jgit.transport.RemoteConfig;
import org.spearce.jgit.transport.RemoteRefUpdate;
import org.spearce.jgit.transport.SshSessionFactory;
import org.spearce.jgit.transport.TrackingRefUpdate;
import org.spearce.jgit.transport.Transport;
import org.spearce.jgit.transport.URIish;
import org.spearce.jgit.transport.RemoteRefUpdate.Status;

import com.asolutions.scmsshd.test.integration.util.TestSSHSessionFactory;

public class IntegrationTestCase {

	private ArrayList<File> scratchDirs = new ArrayList<File>();
	private static int port = (int) (10000 + (Math.random() * 20000));

	public IntegrationTestCase() {
		super();
	}
	
	@Before
	public void setTestSessionFactory(){
		SshSessionFactory.setInstance(new TestSSHSessionFactory());
	}

	@After
	public void removeScratchDirs() throws IOException {
		for (File dir : scratchDirs) {
			FileUtils.deleteDirectory(dir);
		}
	}

	public File makeScratchDir() {
		File basedir = new File(System.getProperty("java.io.tmpdir"));
		File scratchDir = new File(basedir, "scumd-test-"
				+ System.currentTimeMillis());
		scratchDirs.add(scratchDir);
		return scratchDir;
	}

	protected int generatePort() {
		return port++;
	}

	protected static char shortTypeOf(final RefUpdate.Result r) {
		if (r == RefUpdate.Result.LOCK_FAILURE)
			return '!';
		if (r == RefUpdate.Result.IO_FAILURE)
			return '!';
		if (r == RefUpdate.Result.NEW)
			return '*';
		if (r == RefUpdate.Result.FORCED)
			return '+';
		if (r == RefUpdate.Result.FAST_FORWARD)
			return ' ';
		if (r == RefUpdate.Result.REJECTED)
			return '!';
		if (r == RefUpdate.Result.NO_CHANGE)
			return '=';
		return ' ';
	}

	protected void showFetchResult(final Transport tn, final FetchResult r,
			PrintStream out, Repository db) {
		boolean shownURI = false;
		for (final TrackingRefUpdate u : r.getTrackingRefUpdates()) {
			if (u.getResult() == RefUpdate.Result.NO_CHANGE) {
				continue;
			}
			final char type = shortTypeOf(u.getResult());
			final String longType = longTypeOf(u, db);
			final String src = abbreviateRef(u.getRemoteName(), false);
			final String dst = abbreviateRef(u.getLocalName(), true);

			if (!shownURI) {
				out.print("From ");
				out.print(tn.getURI());
				out.println();
				shownURI = true;
			}

			out.format(" %c %-17s %-10s -> %s", type, longType, src, dst);
			out.println();
		}
	}

	protected String longTypeOf(final TrackingRefUpdate u, Repository db) {
		final RefUpdate.Result r = u.getResult();
		if (r == RefUpdate.Result.LOCK_FAILURE)
			return "[lock fail]";
		if (r == RefUpdate.Result.IO_FAILURE)
			return "[i/o error]";
		if (r == RefUpdate.Result.REJECTED)
			return "[rejected]";
		if (ObjectId.zeroId().equals(u.getNewObjectId()))
			return "[deleted]";

		if (r == RefUpdate.Result.NEW) {
			if (u.getRemoteName().startsWith(Constants.R_HEADS))
				return "[new branch]";
			else if (u.getLocalName().startsWith(Constants.R_TAGS))
				return "[new tag]";
			return "[new]";
		}

		if (r == RefUpdate.Result.FORCED) {
			final String aOld = u.getOldObjectId().abbreviate(db).name();
			final String aNew = u.getNewObjectId().abbreviate(db).name();
			return aOld + "..." + aNew;
		}

		if (r == RefUpdate.Result.FAST_FORWARD) {
			final String aOld = u.getOldObjectId().abbreviate(db).name();
			final String aNew = u.getNewObjectId().abbreviate(db).name();
			return aOld + ".." + aNew;
		}

		if (r == RefUpdate.Result.NO_CHANGE)
			return "[up to date]";
		return "[" + r.name() + "]";
	}

	protected String abbreviateRef(String dst, boolean abbreviateRemote) {
		if (dst.startsWith(R_HEADS))
			dst = dst.substring(R_HEADS.length());
		else if (dst.startsWith(R_TAGS))
			dst = dst.substring(R_TAGS.length());
		else if (abbreviateRemote && dst.startsWith(R_REMOTES))
			dst = dst.substring(R_REMOTES.length());
		return dst;
	}

	protected void addRemoteConfigForLocalGitDirectory(Repository db,
			File gitDir, String remoteName) throws URISyntaxException,
			IOException {
		URIish remoteUri = new URIish("file://" + gitDir.getAbsolutePath());
		RemoteConfig rc = new RemoteConfig(db.getConfig(), remoteName);
		rc.addURI(remoteUri);
		rc.addFetchRefSpec(new RefSpec().setForceUpdate(true)
				.setSourceDestination(Constants.R_HEADS + "*",
						Constants.R_REMOTES + remoteName + "/*"));
		rc.update(db.getConfig());
		db.getConfig().save();
	}

	protected FetchResult cloneFromRemote(Repository db, String remoteName)
			throws NotSupportedException, URISyntaxException,
			TransportException {
		Transport tn = Transport.open(db, remoteName);
		FetchResult r = tn.fetch(NullProgressMonitor.INSTANCE, null);
		showFetchResult(tn, r, System.out, db);
		return r;
	}

	protected void addRemoteConfigForRemoteGitDirectory(Repository db, String remoteName, int serverPort, String repoDirectoryName)
			throws URISyntaxException, IOException {
		URIish remoteUri = new URIish("ssh://user@localhost:" + serverPort
				+ "/" + repoDirectoryName);
		RemoteConfig rc = new RemoteConfig(db.getConfig(), remoteName);
		rc.addURI(remoteUri);
		rc.addFetchRefSpec(new RefSpec().setForceUpdate(true)
				.setSourceDestination(Constants.R_HEADS + "*",
						Constants.R_REMOTES + remoteName + "/*"));
		rc.update(db.getConfig());
		db.getConfig().save();
	}

	protected Repository createCloneToRepo() throws IOException {
		Repository db = new Repository(makeScratchDir());
		db.create();
		db.getConfig().setBoolean("core", null, "bare", false);
		db.getConfig().save();
		return db;
	}

	protected void push(String remoteName, String refspec,
			Repository fromRepository) throws NotSupportedException,
			URISyntaxException, IOException, TransportException {
		final List<Transport> transports = Transport.openAll(fromRepository,
				remoteName);
		for (final Transport transport : transports) {
			transport.setPushThin(false);
			transport.setDryRun(false);
			ArrayList<RefSpec> refSpecs = new ArrayList<RefSpec>();
			refSpecs.add(new RefSpec(refspec));
			final Collection<RemoteRefUpdate> toPush = transport
					.findRemoteRefUpdatesFor(refSpecs);

			final URIish uri = transport.getURI();
			final PushResult result;
			try {
				result = transport.push(NullProgressMonitor.INSTANCE, toPush);
			} finally {
				transport.close();
			}
			printPushResult(uri, result, System.out, fromRepository);
		}
	}

	protected void createBareRepo(File scratchDir) throws IOException {
		Repository repo = new Repository(scratchDir);
		repo.create();
		repo.getConfig().setBoolean("core", null, "bare", true);
		repo.close();
	}

	protected void printPushResult(final URIish uri, final PushResult result,
			PrintStream out, Repository db) {
		boolean everythingUpToDate = true;

		// at first, print up-to-date ones...
		for (final RemoteRefUpdate rru : result.getRemoteUpdates()) {
			if (rru.getStatus() == Status.UP_TO_DATE) {
				printRefUpdateResult(uri, result, rru, out, db);
			} else
				everythingUpToDate = false;
		}

		for (final RemoteRefUpdate rru : result.getRemoteUpdates()) {
			// ...then successful updates...
			if (rru.getStatus() == Status.OK)
				printRefUpdateResult(uri, result, rru, out, db);
		}

		for (final RemoteRefUpdate rru : result.getRemoteUpdates()) {
			// ...finally, others (problematic)
			if (rru.getStatus() != Status.OK
					&& rru.getStatus() != Status.UP_TO_DATE)
				printRefUpdateResult(uri, result, rru, out, db);
		}

		if (everythingUpToDate)
			out.println("Everything up-to-date");
	}

	protected void printRefUpdateResult(final URIish uri,
			final PushResult result, final RemoteRefUpdate rru,
			PrintStream out, Repository db) {
		out.format("To %s\n", uri);

		final String remoteName = rru.getRemoteName();
		final String srcRef = rru.isDelete() ? null : rru.getSrcRef();

		switch (rru.getStatus()) {
		case OK:
			if (rru.isDelete())
				printUpdateLine('-', "[deleted]", null, remoteName, null, out,
						db);
			else {
				final Ref oldRef = result.getAdvertisedRef(remoteName);
				if (oldRef == null) {
					final String summary;
					if (remoteName.startsWith(Constants.R_TAGS))
						summary = "[new tag]";
					else
						summary = "[new branch]";
					printUpdateLine('*', summary, srcRef, remoteName, null,
							out, db);
				} else {
					boolean fastForward = rru.isFastForward();
					final char flag = fastForward ? ' ' : '+';
					final String summary = oldRef.getObjectId().abbreviate(db)
							.name()
							+ (fastForward ? ".." : "...")
							+ rru.getNewObjectId().abbreviate(db).name();
					final String message = fastForward ? null : "forced update";
					printUpdateLine(flag, summary, srcRef, remoteName, message,
							out, db);
				}
			}
			break;

		case NON_EXISTING:
			printUpdateLine('X', "[no match]", null, remoteName, null, out, db);
			break;

		case REJECTED_NODELETE:
			printUpdateLine('!', "[rejected]", null, remoteName,
					"remote side does not support deleting refs", out, db);
			break;

		case REJECTED_NONFASTFORWARD:
			printUpdateLine('!', "[rejected]", srcRef, remoteName,
					"non-fast forward", out, db);
			break;

		case REJECTED_REMOTE_CHANGED:
			final String message = "remote ref object changed - is not expected one "
					+ rru.getExpectedOldObjectId().abbreviate(db).name();
			printUpdateLine('!', "[rejected]", srcRef, remoteName, message,
					out, db);
			break;

		case REJECTED_OTHER_REASON:
			printUpdateLine('!', "[remote rejected]", srcRef, remoteName, rru
					.getMessage(), out, db);
			break;

		case UP_TO_DATE:
			printUpdateLine('=', "[up to date]", srcRef, remoteName, null, out,
					db);
			break;

		case NOT_ATTEMPTED:
		case AWAITING_REPORT:
			printUpdateLine('?', "[unexpected push-process behavior]", srcRef,
					remoteName, rru.getMessage(), out, db);
			break;
		}
	}

	protected void printUpdateLine(final char flag, final String summary,
			final String srcRef, final String destRef, final String message,
			PrintStream out, Repository db) {
		out.format(" %c %-17s", flag, summary);

		if (srcRef != null)
			out.format(" %s ->", abbreviateRef(srcRef, true));
		out.format(" %s", abbreviateRef(destRef, true));

		if (message != null)
			out.format(" (%s)", message);

		out.println();
	}

}
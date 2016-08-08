package org.nlp.github.insights;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.nlp.github.insights.db.LuceneDb;
import org.nlp.github.insights.files.FilesDb;
import org.nlp.github.insights.server.Server;
import org.nlp.github.insights.topics.TopicsDb;
import org.nlp.github.insights.users.UserDb;

public class GitInsights {

    private static String REMOTE_URL = "https://github.com/apifest/apifest.git";
    private static String REMOTE_URL2 = "https://github.com/apache/lucene-solr.git";
    private static File localPath = new File("/home/martin/marty_projects/node");


    public static void main(String[] args) {
        String path = "";
        if (args.length > 0) {
            path = sanitizePath(args);
        } else {
            path= localPath.getPath()+"/.git";
        }
        LuceneDb index = LuceneDb.getInstance();
        UserDb users = UserDb.getInstance();
        FilesDb files = FilesDb.getInstance();
        try (Git git = getGit(path)) {
            System.out.println("Having repository: " + git.getRepository().getDirectory());
            Iterable<RevCommit> commits = git.log().all().call();
            List<GitComment> comments = StreamSupport.stream(commits.spliterator(), true).map(commit -> processCommit(git, commit)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            System.out.println("Repository parsed. Total commits: " + comments.size());
            TopicsDb topics = new TopicsDb(new CommentSerializer());
            topics.process(comments);
            System.out.println("Keywords extracted");
            index.addDocuments(comments);
            System.out.println("Commits indexed");
            users.processComments(comments);
            files.processComments(comments);
            git.getRepository().close();
            Server server = new Server();
            server.start(args[1], Integer.parseInt(args[2]));
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String sanitizePath(String[] args) {
        String path;
        path = args[0];
        path = path.endsWith(".git") ? path : path+".git";
        return path;
    }

    private static Git getGit(String path) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
        //return Git.cloneRepository().setURI(uri).setDirectory(localPath).call();
        return Git.open(new File(path));
    }

    private static Optional<GitComment> processCommit(Git git, RevCommit commit) {
        try {
            GitComment com = new GitComment();
            com.setAuthor(commit.getAuthorIdent().getEmailAddress());
            com.setFullMessage(commit.getFullMessage());
            com.setShortMessage(commit.getShortMessage());
            com.setSha(commit.getId().toString());
            List<DiffEntry> diffs = getDiff(git, commit);
            com.setDiffs(diffs.stream()
                    .map(toGitDiff())
                    .collect(Collectors.toList()));
            return Optional.of(com);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static Function<? super DiffEntry, ? extends GitDiff> toGitDiff() {
        return diff -> new GitDiff(diff.getChangeType().toString(),diff.getNewPath());
    }

    private static List<DiffEntry> getDiff(Git git, RevCommit commit) throws IOException {
        if (commit.getParentCount()==0) {
            return Collections.emptyList();
        }
        RevCommit parent = commit.getParent(0);
        try (DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);) {
            df.setRepository(git.getRepository());
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            return df.scan(parent.getTree(), commit.getTree());
        } catch (IOException e) {
            throw e;
        }
    }

}

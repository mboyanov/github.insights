package org.nlp.github.insights;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class DiffProccesser {

    private DiffFormatter df;

    public DiffProccesser(Git git) {
        this.df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(git.getRepository());
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
    }

    public List<DiffEntry> getDiff(RevCommit commit) throws IOException {
        if (commit.getParentCount()==0) {
            return Collections.emptyList();
        }
        RevCommit parent = commit.getParent(0);
        return df.scan(parent.getTree(), commit.getTree());

    }
}

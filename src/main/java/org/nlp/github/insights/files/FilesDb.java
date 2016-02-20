package org.nlp.github.insights.files;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nlp.github.insights.GitComment;

public class FilesDb {

    private Map<String, FileStats> fileStats;

    private FilesDb() {
        fileStats = Collections.synchronizedMap(new HashMap<>());
    }

    private static class Loader {
        private static FilesDb instance = new FilesDb();
    }

    public static FilesDb getInstance() {
        return Loader.instance;
    }


    private FileStats getStats(String user) {
        FileStats stats = fileStats.compute(user, (key, oldValue) -> {
            if (oldValue != null) {
                return oldValue;
            }
            return new FileStats(key);
        });
        return stats;
    }


    public FileStats get(String string) {
        return fileStats.get(string);
    }

    public void processComments(List<GitComment> comments) {
        comments.stream().forEach(this::onDiff);
    }

    private void onDiff(GitComment comment) {
        comment.getDiffs().forEach(diff->getStats(diff.getPath()).incrementChanges(comment.getAuthor()));
    }
}

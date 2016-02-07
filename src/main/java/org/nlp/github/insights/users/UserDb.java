package org.nlp.github.insights.users;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nlp.github.insights.GitComment;

public class UserDb {

    private Map<String, UserStats> userStats;

    private UserDb() {
        userStats = Collections.synchronizedMap(new HashMap<>());
    }

    private static class Loader {
        private static UserDb instance = new UserDb();
    }

    public static UserDb getInstance() {
        return Loader.instance;
    }

    public UserStats onCommit(String user) {

        return getStats(user).incrementTotalCommits();
    }

    private UserStats getStats(String user) {
        UserStats stats = userStats.compute(user, (key, oldValue) -> {
            if (oldValue != null) {
                return oldValue;
            }
            return new UserStats(key);
        });
        return stats;
    }


    public UserStats get(String string) {
        return userStats.get(string);
    }

    public void processComments(List<GitComment> comments) {
        comments.stream().map(GitComment::getAuthor).forEach(this::onCommit);
        comments.stream().forEach(this::onDiff);
    }

    private UserStats onDiff(GitComment comment) {
        return getStats(comment.getAuthor()).incrementTotalFileChanges(comment.getDiffs().size());
    }
}

package org.nlp.github.insights.users;

import java.util.concurrent.atomic.AtomicInteger;

public class UserStats {



    private final String email;
    private AtomicInteger totalCommits = new AtomicInteger();
    private AtomicInteger totalFileChanges = new AtomicInteger();

    public UserStats(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public UserStats incrementTotalCommits() {
        totalCommits.incrementAndGet();
        return this;
    }

    public UserStats incrementTotalFileChanges(int value) {
        totalFileChanges.addAndGet(value);
        return this;
    }

    public AtomicInteger getTotalCommits() {
        return totalCommits;
    }

    public AtomicInteger getTotalFileChanges() {
        return totalFileChanges;
    }


}

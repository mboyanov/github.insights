package org.nlp.github.insights;

public class GitDiff {

    private String changeType;

    private String path;

    public GitDiff() {}

    public GitDiff(String changeType, String path) {
        this.changeType=changeType;
        this.path = path;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}

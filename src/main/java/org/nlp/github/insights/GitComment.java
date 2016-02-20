package org.nlp.github.insights;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitComment {

    private String sha;
    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    private String author, shortMessage, fullMessage;
    @JsonProperty("diffs")
    private List<GitDiff> diffs;
    private Set<String> keywords;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public void setDiffs(List<GitDiff> diffs) {
       this.diffs= diffs;

    }

    public List<GitDiff> getDiffs() {
        return diffs;
    }

    public void setKeywords(Set<String> keyWords) {
        this.keywords = keyWords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

}

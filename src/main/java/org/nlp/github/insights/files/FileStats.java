package org.nlp.github.insights.files;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileStats {


    @JsonProperty("path")
    private String key;


    @JsonProperty("changedBy")
    private final Map<String, AtomicInteger> changedBy = Collections.synchronizedMap(new HashMap<>());
    public FileStats(String key) {
        this.key = key;
    }



    public FileStats incrementChanges(String author) {
        changedBy.compute(author, (oldKey, oldValue) ->{
            if (oldValue == null) {
                oldValue = new AtomicInteger();
            }
            oldValue.incrementAndGet();
            return oldValue;
        });
        return this;
    }

    public FileStats(){

    }



    public String getKey() {
        return key;
    }



    public void setKey(String key) {
        this.key = key;
    }



    public Map<String, AtomicInteger> getChangedBy() {
        return changedBy;
    }



}

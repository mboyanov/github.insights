package org.nlp.github.insights.topics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nlp.github.insights.CommentSerializer;
import org.nlp.github.insights.GitComment;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TopicsDb {



    private CommentSerializer serializer;
    private ObjectMapper om;
    private static final Pattern p = Pattern.compile("(^|\\s)0\\.\\d+\\*\\\"([^\"]+)\\\"");


    public TopicsDb(CommentSerializer serializer) {
        this.serializer = serializer;
        this.om = new ObjectMapper();
    }

    public void process(List<GitComment> comments) {
        serializer.serialize(comments);
        try {
            categorizeComments(comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TopicsDb db = new TopicsDb(new CommentSerializer());
        try {
            db.categorizeComments(Arrays.asList(new GitComment()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void categorizeComments(List<GitComment> comments) throws IOException {
        Process p = Runtime.getRuntime().exec("python src/main/resources/topicModel.py");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        int index = 0;
        while((line = in.readLine()) != null) {
            List<String> topics = om.readValue(line, List.class);
            Set<String> keyWords = extractKeywords(topics);
            comments.get(index).setKeywords(keyWords);
            index++;
        }
    }

    private Set<String> extractKeywords(List<String> topics) {
        Set<String> kw = new HashSet<>();
        for (String topic: topics) {
            Matcher m = p.matcher(topic);
            while (m.find()){
                kw.add(m.group(2));
            }
        }
        return kw;
    }
}

package org.nlp.github.insights;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommentSerializer {
    private static final String FILE_NAME = "comments.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public void serialize(List<GitComment> comments) {
        File f = new File(FILE_NAME);
        try {
            FileWriter writer = new FileWriter(f);
            for (GitComment comment: comments) {
                writer.write(MAPPER.writeValueAsString(comment));
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        try {
            Process p = Runtime.getRuntime().exec("python 12.py");
            System.out.println(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

package org.nlp.github.insights.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.nlp.github.insights.GitComment;
import org.nlp.github.insights.GitDiff;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LuceneDb {

    private Analyzer analyzer;
    private RAMDirectory index;
    private IndexWriterConfig config;
    private final ObjectMapper om = new ObjectMapper();


    private static class Loader {
        private static final LuceneDb instance = new LuceneDb();
    }

    public static LuceneDb getInstance() {
        return Loader.instance;
    }

    private LuceneDb() {
        analyzer = new Analyzer();
        index = new RAMDirectory();
        config = new IndexWriterConfig(analyzer);
    }

    public void addDocuments(List<GitComment> comments) throws IOException {
        IndexWriter w = new IndexWriter(index, config);
        List<Document> docs = comments.stream().map(comment-> {
            Document doc = new Document();
            doc.add(new TextField("full", comment.getFullMessage(), Store.YES));
            doc.add(new TextField("short", comment.getShortMessage(), Store.YES));
            doc.add(new StringField("author", comment.getAuthor(), Store.YES));
            try {
                doc.add(new TextField("diffs", om.writeValueAsString(comment.getDiffs()), Store.YES));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }).collect(Collectors.toList());
        w.addDocuments(docs);
        w.close();
    }


    public List<GitComment> query(String querystr) throws IOException {
        // 2. query

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = null;
        try {
            q = new QueryParser("full", analyzer).parse(querystr);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }

        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        List<GitComment> result = new ArrayList<>();
        GitComment comment;
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            comment = new GitComment();
            comment.setAuthor(d.get("author"));
            comment.setFullMessage(d.get("full"));
            comment.setShortMessage(d.get("short"));
            List<GitDiff> diffs = om.readValue(d.get("diffs"), new TypeReference<List<GitDiff>>(){});
            comment.setDiffs(diffs);
            result.add(comment);
            System.out.println((i + 1) + ". " + d.get("author") + "\t" + d.get("full"));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        return result;
    }

    public void close() {
        index.close();
        analyzer.close();
    }
}


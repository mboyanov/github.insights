package org.nlp.github.insights.api;

import javax.ws.rs.core.Response;

import org.nlp.github.insights.files.FileStats;
import org.nlp.github.insights.files.FilesDb;

public class FilesImpl implements Files {

    @Override
    public Response getFileStats(String path) {
        FilesDb db = FilesDb.getInstance();
        FileStats stats = db.get(path);
        return CORSResponse.ok(stats).build();
    }

}

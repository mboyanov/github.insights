package org.nlp.github.insights.api;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.nlp.github.insights.GitComment;
import org.nlp.github.insights.db.LuceneDb;

public class QueryImpl implements Query {

    @Override
    public Response getResults(String query) {
        try {
            List<GitComment> comments = LuceneDb.getInstance().query(query);
            return CORSResponse.ok(Collections.singletonMap("result", comments)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }

    }


}

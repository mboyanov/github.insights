package org.nlp.github.insights.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class CORSResponse{

    public static ResponseBuilder ok(Object entity) {
        return Response.ok().entity(entity)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
            .header("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Content-Length");

    }
}

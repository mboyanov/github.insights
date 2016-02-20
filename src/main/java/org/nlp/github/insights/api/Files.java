package org.nlp.github.insights.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("files")
public interface Files {

    @Path("/{path: .+}")
    @GET
    @Produces("application/json")
    public Response getFileStats(@PathParam("path") String path);

}

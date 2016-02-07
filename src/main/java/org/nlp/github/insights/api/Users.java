package org.nlp.github.insights.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("users")
public interface Users {

    @Path("/{email}")
    @GET
    @Produces("application/json")
    public Response getUserStats(@PathParam("email") String email);

}

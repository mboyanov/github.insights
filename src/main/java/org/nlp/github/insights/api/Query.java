package org.nlp.github.insights.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.jackson.Formatted;


@Path("query")
public interface Query {

    @Path("/")
    @GET
    @Produces("application/json")
    @Formatted
    Response getResults(@QueryParam("query") String query);

}

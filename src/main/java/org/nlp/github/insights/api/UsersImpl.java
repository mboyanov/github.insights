package org.nlp.github.insights.api;

import javax.ws.rs.core.Response;

import org.nlp.github.insights.users.UserDb;
import org.nlp.github.insights.users.UserStats;

public class UsersImpl implements Users {

    @Override
    public Response getUserStats(String email) {
        UserDb db = UserDb.getInstance();
        UserStats stats = db.get(email);
        return CORSResponse.ok(stats).build();
    }

}

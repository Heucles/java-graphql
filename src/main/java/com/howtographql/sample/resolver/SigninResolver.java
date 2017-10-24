package com.howtographql.sample.resolver;

import com.howtographql.sample.model.SigninPayload;
import com.howtographql.sample.model.User;
import io.leangen.graphql.annotations.GraphQLQuery;

public class SigninResolver {

    @GraphQLQuery
    public User user(SigninPayload payload) {
        return payload.getUser();
    }
}
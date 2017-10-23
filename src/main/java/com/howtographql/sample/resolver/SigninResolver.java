package com.howtographql.sample.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sample.model.SigninPayload;
import com.howtographql.sample.model.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload payload) {
        return payload.getUser();
    }
}
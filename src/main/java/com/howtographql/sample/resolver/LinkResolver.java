package com.howtographql.sample.resolver;

import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.User;
import com.howtographql.sample.repository.UserRepository;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;

public class LinkResolver {

    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GraphQLQuery
    public User postedBy(@GraphQLContext Link link) {

        //@GraphQLContext is used to wire external methods into types. This mapping is semantically the same as if the Link class
        //contained a method public User postedBy() {...}.
        //In this manner, it is possible to keep the logic separate from data, yet still produce deeply nested structures.

        if (link.getUserId() == null) {
            return null;
        }
        return userRepository.findById(link.getUserId());
    }
}
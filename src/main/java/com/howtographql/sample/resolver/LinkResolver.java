package com.howtographql.sample.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.User;
import com.howtographql.sample.repository.UserRepository;

public class LinkResolver implements GraphQLResolver<Link> {

    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return userRepository.findById(link.getUserId());
    }
}
package com.howtographql.sample.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.User;
import com.howtographql.sample.model.Vote;
import com.howtographql.sample.repository.LinkRepository;
import com.howtographql.sample.repository.UserRepository;

public class VoteResolver implements GraphQLResolver<Vote> {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    public VoteResolver(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public User user(Vote vote) {
        return userRepository.findById(vote.getUserId());
    }

    public Link link(Vote vote) {
        return linkRepository.findById(vote.getLinkId());
    }
}
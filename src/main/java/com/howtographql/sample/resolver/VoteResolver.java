package com.howtographql.sample.resolver;

import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.User;
import com.howtographql.sample.model.Vote;
import com.howtographql.sample.repository.LinkRepository;
import com.howtographql.sample.repository.UserRepository;
import io.leangen.graphql.annotations.GraphQLQuery;

public class VoteResolver {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    public VoteResolver(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    @GraphQLQuery
    public User user(Vote vote) {
        return userRepository.findById(vote.getUserId());
    }

    @GraphQLQuery
    public Link link(Vote vote) {
        return linkRepository.findById(vote.getLinkId());
    }
}
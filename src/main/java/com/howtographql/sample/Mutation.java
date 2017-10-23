package com.howtographql.sample;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.sample.model.Link;
import com.howtographql.sample.repository.LinkRepository;

import java.util.List;

public class Mutation implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    public Mutation(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }
}
package com.howtographql.sample;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.sample.model.Link;
import com.howtographql.sample.repository.LinkRepository;

import java.util.List;

public class Query implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> allLinks() {
        return linkRepository.getAllLinks();
    }
}
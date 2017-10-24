package com.howtographql.sample;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.LinkFilter;
import com.howtographql.sample.repository.LinkRepository;

import java.util.List;
// Aqui tem que estar na Raiz pois é o que é referenciado no arquivo schema como contendo as queries
public class Query implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> allLinks(LinkFilter filter) {
        return linkRepository.getAllLinks(filter);
    }
}
package com.howtographql.sample;

import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.LinkFilter;
import com.howtographql.sample.repository.LinkRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.List;

public class Query {

    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    // tudo o que tiver que ser resolvido de tipagem complexa no schema tem que ser decorado
    @GraphQLQuery
    public List<Link> allLinks(LinkFilter filter,
                               // a annotation abaixo não é necessária mas se você quiser modificar os nomes ...
                               @GraphQLArgument(name = "skip", defaultValue = "0") Number skip,
                               @GraphQLArgument(name = "first", defaultValue = "0") Number first) {
        return linkRepository.getAllLinks(filter, skip.intValue(), first.intValue());
    }
}
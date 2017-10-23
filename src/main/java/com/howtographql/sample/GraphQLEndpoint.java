package com.howtographql.sample;

import com.coxautodev.graphql.tools.SchemaParser;
import javax.servlet.annotation.WebServlet;

import com.howtographql.sample.repository.LinkRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;


@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;

    static {
        MongoDatabase mongo = new MongoClient("172.17.0.2", 27017).getDatabase("hackernews");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(linkRepository), new Mutation(linkRepository))
                .build()
                .makeExecutableSchema();
    }
}
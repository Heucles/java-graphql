package com.howtographql.sample;

import com.coxautodev.graphql.tools.SchemaParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.howtographql.sample.model.User;
import com.howtographql.sample.repository.LinkRepository;
import com.howtographql.sample.resolver.LinkResolver;
import com.howtographql.sample.resolver.SigninResolver;
import com.howtographql.sample.repository.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

import java.util.Optional;


@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;

    static {
        MongoDatabase mongo = new MongoClient("172.17.0.2", 27017).getDatabase("hackernews");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(linkRepository),
                        new Mutation(linkRepository, userRepository),
                        new SigninResolver(),
                        new LinkResolver(userRepository))
                .build()
                .makeExecutableSchema();
    }

    // Precisa desse override para poder validar os requests que tem o header de authorization e os que não
    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request
                .map(req -> req.getHeader("Authorization"))
                .filter(id -> !id.isEmpty())
                .map(id -> id.replace("Bearer ", ""))
                .map(userRepository::findById)
                .orElse(null);
        return new AuthContext(user, request, response);
    }

}
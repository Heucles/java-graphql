package com.howtographql.sample;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.User;
import com.howtographql.sample.repository.LinkRepository;
import com.howtographql.sample.repository.VoteRepository;
import com.howtographql.sample.resolver.LinkResolver;
import com.howtographql.sample.resolver.SigninResolver;
import com.howtographql.sample.repository.UserRepository;
import com.howtographql.sample.resolver.VoteResolver;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;
import io.leangen.graphql.GraphQLSchemaGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;
    private static final VoteRepository voteRepository;

    static {
        MongoDatabase mongo = new MongoClient("172.17.0.2", 27017).getDatabase("hackernews");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
        voteRepository = new VoteRepository(mongo.getCollection("votes"));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        //create or inject the service beans
        Query query = new Query(linkRepository);
        LinkResolver linkResolver = new LinkResolver(userRepository);
        Mutation mutation = new Mutation(linkRepository, userRepository, voteRepository);

        return new GraphQLSchemaGenerator()
                .withOperationsFromSingletons(query,linkResolver,mutation) // registering the beans
                .generate(); //done
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

    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors){
        return errors.stream()
                .filter(e->e instanceof ExceptionWhileDataFetching || super.isClientError(e))
                .map(e->e instanceof ExceptionWhileDataFetching ?
                        new SanitizedError((ExceptionWhileDataFetching)e):e)
                .collect(Collectors.toList());
    }

}
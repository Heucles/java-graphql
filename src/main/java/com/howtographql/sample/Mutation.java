package com.howtographql.sample;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.sample.model.*;
import com.howtographql.sample.repository.LinkRepository;
import com.howtographql.sample.repository.UserRepository;
import com.howtographql.sample.repository.VoteRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

// Aqui tem que estar na Raiz pois é o que é referenciado no arquivo schema como contendo as mutations
public class Mutation implements GraphQLRootResolver {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public Mutation(LinkRepository linkRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;

    }

    public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByEmail(auth.getEmail());
        if (user.getPassword().equals(auth.getPassword())) {
            return new SigninPayload(user.getId(), user);
        }

        throw new GraphQLException("Invalid Credentials");
    }

    //The way to inject the context is via DataFetchingEnvironment
    public Link createLink(String url, String description, DataFetchingEnvironment env) {
        AuthContext context = env.getContext();
        Link newLink = new Link(url, description, context.getUser().getId());
        linkRepository.saveLink(newLink);
        return newLink;
    }

    public User createUser(String name, AuthData auth) {
        User user = new User(name, auth.getEmail(), auth.getPassword());
        return userRepository.saveUser(user);
    }

    public Vote createVote(String linkId, String userId) {
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
        return voteRepository.saveVote(new Vote(now, userId, linkId));
    }
}
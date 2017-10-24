package com.howtographql.sample.repository;

import com.howtographql.sample.model.Link;
import com.howtographql.sample.model.LinkFilter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class LinkRepository {

    private final MongoCollection<Document> links;

    private Link createLink(Document doc) {
        return new Link(
                doc.get("_id").toString(),
                doc.getString("url"),
                doc.getString("description"),
                doc.getString("postedBy")
        );
    }

    public LinkRepository(MongoCollection<Document> links) {
        this.links = links;
    }

    public List<Link> getAllLinks(LinkFilter filter, int skip, int first) {
        Optional<Bson> mongoFilter = Optional.ofNullable(filter).map(this::buildFilter);
        FindIterable<Document> filteredDocuments = mongoFilter.map(links::find).orElseGet(links::find);
        List<Link> allLinks = new ArrayList<>();
        for (Document doc : filteredDocuments.skip(skip).limit(first)) {
            allLinks.add(createLink(doc));
        }
        return allLinks;
    }

    public void saveLink(Link link) {
        Document doc = new Document();
        doc.append("url", link.getUrl());
        doc.append("description", link.getDescription());
        doc.append("postedBy", link.getUserId());
        links.insertOne(doc);
    }

    public Link findById(String linkId) {
        return this.createLink(
                this.links.find(
                        eq("_id", new ObjectId(linkId))).first());
    }

    private Bson buildFilter(LinkFilter filter) {
        String descriptionPattern = filter.getDescriptionContains();
        String urlPattern = filter.getUrlContains();
        Bson descriptionCondition = null;
        Bson urlCondition = null;

        if (descriptionPattern != null && !descriptionPattern.isEmpty()) {
            descriptionCondition = regex("description", ".*" + descriptionPattern + ".*", "i");
        }

        if (urlPattern != null && !urlPattern.isEmpty()) {
            urlCondition = regex("url", ".*" + urlPattern + ".*", "i");
        }

        if (descriptionCondition != null && urlCondition != null) {
            return and(descriptionCondition, urlCondition);
        }

        return descriptionCondition != null ? descriptionCondition : urlCondition;

    }

}
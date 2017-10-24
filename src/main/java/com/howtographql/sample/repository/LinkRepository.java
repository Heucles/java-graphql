package com.howtographql.sample.repository;

import com.howtographql.sample.model.Link;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class LinkRepository {

    private final MongoCollection<Document> links;

    @NotNull
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

    public List<Link> getAllLinks() {
        List<Link> allLinks = new ArrayList<>();
        for (Document doc : links.find()) {
            Link link = createLink(doc);
            allLinks.add(link);
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
}
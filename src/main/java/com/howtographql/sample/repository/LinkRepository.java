package com.howtographql.sample.repository;

import com.howtographql.sample.model.Link;
import com.mongodb.client.MongoCollection;
import org.bson.Document;


import java.util.ArrayList;
import java.util.List;

public class LinkRepository {

    private final MongoCollection<Document> links;

    private Link link(Document doc) {
        return new Link(
                doc.get("_id").toString(),
                doc.getString("url"),
                doc.getString("description"));
    }

    public LinkRepository(MongoCollection<Document> links) {
        this.links = links;
    }

    public List<Link> getAllLinks() {
        List<Link> allLinks = new ArrayList<Link>();
        for (Document doc :
                this.links.find()) {
            allLinks.add(link(doc));
        }

        return allLinks;
    }

    public void saveLink(Link link) {
        Document doc = new Document();
        doc.append("url", link.getUrl());
        doc.append("description", link.getDescription());
        this.links.insertOne(doc);
    }
}

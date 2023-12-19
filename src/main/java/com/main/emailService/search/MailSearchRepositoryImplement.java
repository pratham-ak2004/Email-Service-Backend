package com.main.emailService.search;

import com.main.emailService.models.Email;
import com.main.emailService.models.Mail;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class MailSearchRepositoryImplement implements MailSearchRepository{

    @Autowired
    private MongoClient mongoClient;

    private Mail convertDocumentToMail(Document document){
        String id = document.getObjectId("_id").toString();
        String sender = document.getString("sender");
        String receiver = document.getString("receiver");
        String subject = document.getString("subject");
        String message = document.getString("message");
        boolean seen = document.getBoolean("seen");

        return new Mail(id,sender,receiver,subject,message,seen);
    }

    @Override
    public List<Mail> findAllMails(Email email) {

        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("mail");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                        new Document("$or", Arrays.asList(new Document("sender", email.getEmail()),
                        new Document("receiver", email.getEmail()))))));

        List<Mail> ans = new ArrayList<>();

        result.forEach(document -> {
            ans.add(convertDocumentToMail(document));
        });

        return ans;

    }

    @Override
    public List<Mail> findAllSent(Email email) {
        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("mail");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match", new Document("sender", email.getEmail()))));

        List<Mail> ans = new ArrayList<>();

        result.forEach(document -> {
            ans.add(convertDocumentToMail(document));
        });

        return ans;
    }

    @Override
    public List<Mail> findAllReceived(Email email) {

        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("mail");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match", new Document("receiver",email.getEmail()))));

        List<Mail> ans = new ArrayList<>();

        result.forEach(document -> {
            ans.add(convertDocumentToMail(document));
        });

        return ans;

    }

    @Override
    public List<Mail> findBySearchText(Email email, String text) {
        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("mail");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                        new Document("$or", Arrays.asList(new Document("sender", email.getEmail()), new Document("receiver", email.getEmail())))),
                new Document("$match",
                        new Document("$or", Arrays.asList(new Document("sender",
                                        new Document("$regex", Pattern.compile(text + "(?i)"))),
                                new Document("receiver",
                                        new Document("$regex", Pattern.compile(text + "(?i)"))),
                                new Document("subject",
                                        new Document("$regex", Pattern.compile(text + "(?i)"))),
                                new Document(" message",
                                        new Document("$regex", Pattern.compile(text + "(?i)"))))))));

        List<Mail> ans = new ArrayList<>();

        result.forEach(document -> {
            ans.add(convertDocumentToMail(document));
        });

        return ans;
    }
}

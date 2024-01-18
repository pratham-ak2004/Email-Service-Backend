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

@Component
public class MailSearchRepositoryImplement implements MailSearchRepository{

    @Autowired
    private MongoClient mongoClient;

    private List<Mail.EmailValue> convertDocumentToEmailValueList(List<Document> documents) {
        List<Mail.EmailValue> emailValues = new ArrayList<>();
        for (Document document : documents) {
            String email = document.getString("email");
            boolean value = document.getBoolean("value");
            emailValues.add(new Mail.EmailValue(email, value));
        }
        return emailValues;
    }

    private Mail convertDocumentToMail(Document document){
        String id = document.getObjectId("_id").toString();
        String sender = document.getString("sender");

        List<Mail.EmailValue> to = convertDocumentToEmailValueList(document.getList("to", Document.class));
        List<Mail.EmailValue> cc = convertDocumentToEmailValueList(document.getList("cc", Document.class));
        List<Mail.EmailValue> bcc = convertDocumentToEmailValueList(document.getList("bcc", Document.class));

        String subject = document.getString("subject");
        String message = document.getString("message");

        return new Mail(id, sender, to, cc, bcc, subject, message);
    }

    @Override
    public List<Mail> findAllMails(Email email) {

        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("mail");

        String targetEmail = email.getEmail();

        AggregateIterable<Document> result = collection.aggregate(List.of(new Document("$match",
                new Document("$or", Arrays.asList(new Document("sender", targetEmail),
                        new Document("to",
                                new Document("$elemMatch",
                                        new Document("email", targetEmail))),
                        new Document("cc",
                                new Document("$elemMatch",
                                        new Document("email", targetEmail))),
                        new Document("bcc",
                                new Document("$elemMatch",
                                        new Document("email", targetEmail))))))));

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

        AggregateIterable<Document> result = collection.aggregate(List.of(new Document("$match", new Document("sender", email.getEmail()))));

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

        String targetEmail = email.getEmail();

        AggregateIterable<Document> result = collection.aggregate(List.of(new Document("$match",
                new Document("$or", Arrays.asList(new Document("to",
                                new Document("$elemMatch",
                                        new Document("email", targetEmail))),
                        new Document("cc",
                                new Document("$elemMatch",
                                        new Document("email", targetEmail))),
                        new Document("bcc",
                                new Document("$elemMatch",
                                        new Document("email", targetEmail))))))));

        List<Mail> ans = new ArrayList<>();

        result.forEach(document -> {
            ans.add(convertDocumentToMail(document));
        });

        return ans;

    }

    @Override
    public List<Mail> findBySearchText(Email email, String targetText) {
        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("mail");

        String targetEmail = email.getEmail();

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                        new Document("$or", Arrays.asList(new Document("sender", targetEmail),
                                new Document("to",
                                        new Document("$elemMatch",
                                                new Document("email", targetEmail))),
                                new Document("cc",
                                        new Document("$elemMatch",
                                                new Document("email", targetEmail))),
                                new Document("bcc",
                                        new Document("$elemMatch",
                                                new Document("email", targetEmail)))))),
                new Document("$match",
                        new Document("$or", Arrays.asList(new Document("subject",
                                        new Document("$regex", targetText)
                                                .append("$options", "i")),
                                new Document("message",
                                        new Document("$regex", targetText)
                                                .append("$options", "i")))))));

        List<Mail> ans = new ArrayList<>();

        result.forEach(document -> {
            ans.add(convertDocumentToMail(document));
        });

        return ans;
    }
}

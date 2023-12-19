package com.main.emailService.search;

import com.main.emailService.models.Email;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EmailSearchRepositoryImplement implements EmailSearchRepository{

    @Autowired
    private MongoClient mongoClient;

    @Override
    public boolean isEmailPresent(Email user) {
        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("email");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match", new Document("email", user.getEmail()))));

        if(result.first() == null){
            return false;
        }else {
            return true;
        }

    }

    @Override
    public boolean isPasswordPresent(Email user) {
        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("email");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                        new Document("$match", new Document("email", user.getEmail())),
                        new Document("$match", new Document("password", user.getPassword()))));

        if(result.first() == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public ObjectId getEmailId(Email user) {
        MongoDatabase database = mongoClient.getDatabase("EmailService");
        MongoCollection<Document> collection = database.getCollection("email");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("email", user.getEmail())),
                new Document("$match", new Document("password", user.getPassword()))));

        List<ObjectId> ans = new ArrayList<>();

        result.forEach(document -> {
            ObjectId id = document.getObjectId("_id");
            ans.add(id);
        });

        if(ans.isEmpty()){
            return null;
        }else if(ans.size() > 1){
            return new ObjectId("security breach");
        }else {
            return ans.get(0);
        }
    }
}

package org.example.helpers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBHelper {
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBHelper() {
        this.mongoClient = MongoClients.create(PropertiesLoader.getMongoUri());
        this.database = mongoClient.getDatabase(PropertiesLoader.getMongoDbName());
    }
    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }
    public Document getDocQueryInMongo(String collectionName, int id, String field){
        MongoCollection<Document> userCollection = getCollection(collectionName);
        Document query = new Document(field, id);
        return userCollection.find(query).first();
    }


    public void close() {
        mongoClient.close();
    }
}
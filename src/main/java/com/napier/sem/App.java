package com.napier.sem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App
{
    public static void main(String[] args)
    {
        // === IMPLEMENT TRY-WITH-RESOURCES HERE ===
        try (MongoClient mongoClient = new MongoClient("localhost", 27000))
        {
            // Get a database - will create when we use it
            MongoDatabase database = mongoClient.getDatabase("mydb");

            // Get a collection from the database
            MongoCollection<Document> collection = database.getCollection("test");

            // Create a document to store
            Document doc = new Document("name", "Kevin Sim")
                    .append("class", "DevOps")
                    .append("year", "2024")
                    .append("result", new Document("CW", 95).append("EX", 85));

            // Add document to collection
            collection.insertOne(doc);

            // Check document in collection
            Document myDoc = collection.find().first();
            System.out.println(myDoc.toJson());
        }
        // Catch any exception that occurs during connection or operation
        catch (Exception e)
        {
            System.err.println("An error occurred during MongoDB operation: " + e.getMessage());
        }
        // NOTE: The 'finally' block is no longer needed!
    }
}
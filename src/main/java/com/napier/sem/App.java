package com.napier.sem;

//import the class for connecting to a MongoDB server
import com.mongodb.MongoClient;
//import the class representing a MongoDB database
import com.mongodb.client.MongoDatabase;
//import the class for interacting with a specific collection (like a table)
import com.mongodb.client.MongoCollection;
//import the class for creating and handling BSON document (MongoDB's data structure)
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        //establish a connection to the MongoDB server using try-with-resource block
        //the connection will automatically close after the block, even if an error occurs.
        try (MongoClient mongoClient = new MongoClient("mongo-dbserver", 27017)) {
            //gat a reference to the 'mydb' database; it will be created if it doesn't exist
            MongoDatabase database = mongoClient.getDatabase("mydb");

            //get a reference to the 'test' collection
            MongoCollection<Document> collection = database.getCollection("test");

            //create a BSON document to insert into the collection
            Document doc = new Document("name", "Kevin Sim")
                    .append("class", "DevOps")
                    .append("year", "2024")
                    //embed a sub-document for 'result'
                    .append("result", new Document("CW", 95).append("EX", 85));

            //insert the created document into the 'test' collection
            collection.insertOne(doc);

            //find the *first* document in the collection
            Document myDoc = collection.find().first();

            //print the retrieved document to the console as a JSON string
            System.out.println(myDoc.toJson());
        }
        //catch any exceptions (e.g. connection failures, database errors)
        catch (Exception e) {
            //print an error message to the standard error stream
            System.err.println("An error occurred during MongoDB operation: " + e.getMessage());
        }
    }
}
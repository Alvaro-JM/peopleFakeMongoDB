package com.mongodb.quickstart;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Introduce personas fake en la colección.
 * Usa diferentes métodos para introducir personas fake como documentos
 * en una colección de MongoDB
 * 
 * @author Álvaro Jiménez
 * @version date 24/05/2021
 */
public class CreatePeopleFake {
    
    /**
     * Introduce una persona fake en la colección.
     * @param peopleFakeCollection colección
     */
    private static void insertOneDocument(MongoCollection<Document> peopleFakeCollection) {
        peopleFakeCollection.insertOne(generateNewPerson());
        System.out.println("One person inserted for peopleFake.");
    }
    
    /**
     * Introduce 50 personas fake en la colección.
     * @param peopleFakeCollection colección
     */
    private static void insertManyDocuments(MongoCollection<Document> peopleFakeCollection) {
        //Se crea un array de documentos
        List<Document> people = new ArrayList<>();
        //Se introducen las personas como documentos en el array
        for (int i = 0; i < 50; i++) {
            people.add(generateNewPerson());
        }
        //Se introduce el array de personas en la colección
        peopleFakeCollection.insertMany(people, new InsertManyOptions().ordered(false));
        System.out.println("Fifty people inserted for peopleFake.");
    }
    
    /**
     * Crea documentos a partir de personas fake.
     * @return document
     */
    private static Document generateNewPerson() {
        Person p = Person.randomPersonGen();
        return new Document("_id", new ObjectId()).append("name", p.getName())
                                                  .append("age", p.getAge());
    }

    /**
     * Main con el que se ejecuta la insercción de documentos.
     * @param args no requiere argumentos
     */
    public static void main(String[] args) {
        //Intenta la conexión con el servidor de MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            //En caso de conexión se establece la base de datos y la colección
            MongoDatabase localDB = mongoClient.getDatabase("local");
            MongoCollection<Document> peopleFakeCollection = localDB.getCollection("peopleFake");
            
            //Se insertan los documentos
            insertOneDocument(peopleFakeCollection);
            insertManyDocuments(peopleFakeCollection);
        }
    }
}

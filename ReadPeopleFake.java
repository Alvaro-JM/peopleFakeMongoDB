
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;


/**
 * Ejemplos de búsquedas.
 * Muestra diferentes formas de buscar en una colección de MongoDB
 * 
 * @author Álvaro Jiménez
 * @version date 24/05/2021
 */
public class ReadPeopleFake {

    /**
     * Búsqueda de un documento con un nuevo documento.
     * @param peopleFakeCollection colección
     * @param name nombre
     */
    private static void findByName(MongoCollection<Document> peopleFakeCollection, String name){
        Document p1 = peopleFakeCollection.find(new Document("name", name)).first();
        System.out.println("Person 1: " + p1.toJson());
    }
    
    /**
     * Búsqueda de un documento con el filtro eq.
     * @param peopleFakeCollection colección
     * @param name nombre
     */
    private static void findByNameEq(MongoCollection<Document> peopleFakeCollection, String name){
        Document p2 = peopleFakeCollection.find(eq("name", name)).first();
        System.out.println("Person 2: " + p2.toJson());
    }
    
    /**
     * Búsqueda de varios documentos usando una lista iterable y un cursor que la recorre.
     * @param peopleFakeCollection colección
     * @param age edad
     */
    private static void findByAgeListIterator(MongoCollection<Document> peopleFakeCollection, int age) {
        FindIterable<Document> iterable = peopleFakeCollection.find(gte("age", age));
        MongoCursor<Document> cursor = iterable.iterator();
        System.out.println("People list with a cursor: ");
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }
    }
    
    /**
     * Búsqueda de varios documentos usando un arraylist y un for que la recorre.
     * @param peopleFakeCollection colección
     * @param age edad
     */
    private static void findByAgeArrayList(MongoCollection<Document> peopleFakeCollection, int age) {
        List<Document> peopleList = peopleFakeCollection.find(gte("age", age)).into(new ArrayList<>());
        System.out.println("People list with an ArrayList:");
        for (Document p : peopleList) {
            System.out.println(p.toJson());
        }
    }
    
    /**
     * Búsqueda de varios documentos usando un Consumer.
     * @param peopleFakeCollection colección
     * @param age edad
     */
    private static void findByAgeConsumer(MongoCollection<Document> peopleFakeCollection, int age) {
        System.out.println("People list using a Consumer:");
        Consumer<Document> printConsumer = document -> System.out.println(document.toJson());
        peopleFakeCollection.find(gte("age", age)).forEach(printConsumer);
    }
    
    /**
     * Búsqueda de varios documentos acotados.
     * Busca documentos con un nombre dado y una edad inferior a la dada.
     * Muestra nombre y edad, no muestra el id.
     * Ordena de forma descendiente, se salta el primer resultado, y solo muestra 4 resultados.
     * @param peopleFakeCollection colección
     * @param name nombre
     * @param age edad
     */
    private static void findByNameLessAge(MongoCollection<Document> peopleFakeCollection, String name, int age) {
        List<Document> docs = peopleFakeCollection.find(and(eq("name", name), lte("name", age)))
                .projection(fields(excludeId(), include("age", "name")))
                .sort(descending("age"))
                .skip(1)
                .limit(4)
                .into(new ArrayList<>());

        System.out.println("People sorted, skipped, limited and projected: ");
        for (Document student : docs) {
            System.out.println(student.toJson());
        }
    }
            
    /**
     * Main donde se ejecutan las búsquedas.
     * 
     * @param args no requiere argumentos
     */
    public static void main(String[] args) {
        //Intenta la conexión con el servidor de MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            //Se establece la base de datos y la colección 
            MongoDatabase localDB = mongoClient.getDatabase("local");
            MongoCollection<Document> peopleFakeCollection = localDB.getCollection("peopleFake");

            findByName(peopleFakeCollection, "Marvin Berry");
            
            findByNameEq(peopleFakeCollection, "Kermit");
            
            findByAgeListIterator(peopleFakeCollection, 78);
            
            findByAgeArrayList(peopleFakeCollection, 99);
            
            findByAgeConsumer(peopleFakeCollection, 105);
            
            findByNameLessAge(peopleFakeCollection, "Marvin Berry", 78);
            
        }
    }
}

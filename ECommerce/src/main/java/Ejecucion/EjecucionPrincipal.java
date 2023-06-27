package Ejecucion;

import Modelos.Producto;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class EjecucionPrincipal {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        mongoClient.listDatabaseNames().forEach(System.out::println);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("ECommerce");

        try {
            mongoDatabase.createCollection("CatalogoProductos");
        } catch (MongoCommandException e) {
            System.out.println("Ya existe la coleccion");
        }

        MongoCollection<Document> collectionCatalogoProductos = mongoDatabase.getCollection("CatalogoProductos");

        Producto producto = new Producto("Campera", "Polar", "Invernal", 20.5, 14.3, collectionCatalogoProductos);
    }
}

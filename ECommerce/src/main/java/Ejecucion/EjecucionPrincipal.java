package Ejecucion;

import Modelos.Producto;
import Modelos.Usuario.Usuario;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class EjecucionPrincipal {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ECommerce");
        try {
            mongoDatabase.createCollection("CatalogoProductos");
        } catch (MongoCommandException e) {
            System.out.println("Collecion creada exitosamente");
        }

        MongoCollection<Document> collectionCatalogoProductos = mongoDatabase.getCollection("CatalogoProductos");
        MongoCollection<Document> collectionUsuario = mongoDatabase.getCollection("Usuarios");


        Producto producto = new Producto("Remera", "Gucci", "Manga abierta", 25.0, 12.6, collectionCatalogoProductos);

        Usuario usuario = new Usuario("Diego", "Calle 123", 12345612, collectionUsuario);
    }
}

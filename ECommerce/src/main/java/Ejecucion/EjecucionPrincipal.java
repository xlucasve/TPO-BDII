package Ejecucion;

import Modelos.CarroCompras.CarroCompra;
import Modelos.Producto.Producto;
import Modelos.Usuario.SesionUsuario;
import Modelos.Usuario.Usuario;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;
import redis.clients.jedis.JedisPooled;

import java.util.Calendar;
import java.util.Date;

public class EjecucionPrincipal {
    public static void main(String[] args) {
        JedisPooled jedis = new JedisPooled("localhost", 6379);

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ECommerce");
        try {
            mongoDatabase.createCollection("CatalogoProductos");
        } catch (MongoCommandException e) {
            System.out.println("Collecion creada exitosamente");
        }

        MongoCollection<Document> collectionCatalogoProductos = mongoDatabase.getCollection("CatalogoProductos");
        MongoCollection<Document> collectionUsuario = mongoDatabase.getCollection("Usuarios");
        MongoCollection<Document> collectionListadoPrecios = mongoDatabase.getCollection("ListadoPrecios");


        Producto producto = new Producto("producto2", "Remera", "Gucci", 12.2, 25.0, 12.6, 20.0, collectionCatalogoProductos, collectionListadoPrecios);
        producto.actualizarPrecioProducto(collectionListadoPrecios, 10.2);
        System.out.println(producto.getProductoId());

        Usuario usuario = new Usuario("Diego", "Calle 123", 12345612, collectionUsuario);
        Date fecha1 = new Date(2023, Calendar.JUNE, 10, 10, 00, 00);
        Date fecha2 = new Date(2023, Calendar.JUNE, 10, 15, 20, 00);
        SesionUsuario sesionUsuario = new SesionUsuario(fecha1, fecha2);


        usuario.agregarSesion(sesionUsuario, collectionUsuario);

        usuario.recuperarSesion(collectionUsuario, "Diego");

        CarroCompra carroCompra = new CarroCompra("CarroCompraTest");

        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.eliminarUnProducto(jedis, producto);



    }
}

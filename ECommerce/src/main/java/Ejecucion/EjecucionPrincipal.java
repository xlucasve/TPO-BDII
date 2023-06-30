package Ejecucion;

import Modelos.CarroCompras.CarroCompra;
import Modelos.Pedidos.Pedido;
import Modelos.Producto.Producto;
import Modelos.Usuario.CategoriaIVA;
import Modelos.Usuario.SesionUsuario;
import Modelos.Usuario.Usuario;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
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
        MongoCollection<Document> collectionPedido = mongoDatabase.getCollection("Pedidos");


        Producto producto = new Producto( "Remera", "Gucci", "Manga abierta",
                25.0, 12.6, 20.0, 4.7, collectionCatalogoProductos, collectionListadoPrecios);
        Producto producto2 = new Producto( "Campera", "Gucci", "Manga Cerrada",
                25.0, 12.6, 150.0, 4.7, collectionCatalogoProductos, collectionListadoPrecios);
        producto.actualizarPrecioProducto(collectionListadoPrecios, 10.2);


        Usuario usuario = new Usuario("Diego", "Gutierrez", "Calle 123", 12345612, CategoriaIVA.A , collectionUsuario);
        Date fecha1 = new Date(2023, Calendar.JUNE, 10, 10, 00, 00);
        Date fecha2 = new Date(2023, Calendar.JUNE, 10, 15, 20, 00);
        SesionUsuario sesionUsuario = new SesionUsuario(fecha1, fecha2);


        usuario.agregarSesion(sesionUsuario, collectionUsuario);
        System.out.println(usuario.getUsuarioId());
        usuario.recuperarSesion(collectionUsuario);
        CarroCompra carroCompra = new CarroCompra(usuario.getUsuarioId());
        System.out.println(carroCompra.getCarroId());

        //Testeo de carro de compra
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto2);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto2);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.eliminarUnProducto(jedis, producto);
        System.out.println("TODOS ITEMS EN EL CARRO ANTES DE ELIMINAR UNA LINEA DE PRODUCTO: \n" + jedis.hgetAll(carroCompra.getCarroId()));
        carroCompra.eliminarUnProducto(jedis, producto);
        System.out.println("TODOS ITEMS EN EL CARRO DESPUES DE ELIMINAR UNA LINEA DE PRODUCTO: \n" + jedis.hgetAll(carroCompra.getCarroId()));


        //Crear nuevo Pedido

        Pedido pedido1 = new Pedido(1, 20.4, carroCompra, usuario, collectionPedido);
        System.out.println(pedido1.getPedidoId());

        //Cerramos la conexión
        jedis.close();
        mongoClient.close();


    }
}

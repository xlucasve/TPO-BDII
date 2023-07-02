package Ejecucion;

import Modelos.CarroCompras.CarroCompra;
import Modelos.Operador.Operador;
import Modelos.Pedidos.Pedido;
import Modelos.Producto.Producto;
import Modelos.Usuario.CategoriaIVA;
import Modelos.Usuario.SesionUsuario;
import Modelos.Usuario.Usuario;
import com.datastax.driver.core.Session;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import conexionesBD.CassandraConnector;
import org.apache.cassandra.schema.SchemaKeyspace;
import org.bson.Document;
import redis.clients.jedis.JedisPooled;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class EjecucionPrincipal {
    public static void main(String[] args) throws SQLException {


        //Conexion con Cassandra

        CassandraConnector conexion = new CassandraConnector();

        //CAMBIAR PUERTO DE CASSANDRA A 9142 SI USAS EL OTRO
        conexion.connect("127.0.0.1", 9042);
        Session session = conexion.getSession();

        //PUSE QUE EL KEYSPACE NO SE CREE SI YA EXISTE PARA EVITAR QUE TIRE ERROR
        String query = "CREATE KEYSPACE IF NOT EXISTS ejemplo WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':1};";
        session.execute(query);
        session.execute("USE ejemplo");

        System.out.println("Incializado Cassandra");
        //Fin Cassandra


        //DEJO COMENTADO SQL HASTA QUE ME PONGA A TRABAJAR EN ELLO
        String connectionUrl = "jdbc:sqlserver://0.0.0.0:1433;encrypt=false;databaseName=ECommerce;user=sa;password=SuperAdmin#";

        System.out.println("Inicializando SQL...");
        Connection conn = DriverManager.getConnection(connectionUrl);
        if (conn != null) {
            System.out.println("Conectado exitosamente");
        }
        System.out.println("SQL inicializado correctamente");

        Statement stmt = conn.createStatement();


        //Creacion de conexión a Redis
        System.out.println("Inicializando Redis...");
        JedisPooled jedis = new JedisPooled("localhost", 6379);
        System.out.println("Inicializado Redis");

        System.out.println("Inicializando MongoDB...");
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ECommerce");
        System.out.println("Incializado MongoDB");
        System.out.println("");

        //Creacion de collecciones mongo
        MongoCollection<Document> collectionCatalogoProductos = mongoDatabase.getCollection("CatalogoProductos");
        MongoCollection<Document> collectionUsuario = mongoDatabase.getCollection("Usuarios");
        MongoCollection<Document> collectionListadoPrecios = mongoDatabase.getCollection("ListadoPrecios");
        MongoCollection<Document> collectionPedido = mongoDatabase.getCollection("Pedidos");


        //Creacion de productos y actualizacion de precio de 1
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
        usuario.recuperarSesion(collectionUsuario);
        CarroCompra carroCompra = new CarroCompra(usuario.getUsuarioId());

        //Testeo de carro de compra
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto2);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.agregarProducto(jedis, producto2);
        carroCompra.agregarProducto(jedis, producto);
        carroCompra.eliminarUnProducto(jedis, producto);
        carroCompra.eliminarUnProducto(jedis, producto);

        //Crear nuevo Pedido
        Operador operador = new Operador(1, "Damian Wacho", "Galvez", 1243650);
        Pedido pedido1 = new Pedido(1, 20.4, carroCompra.getPrecioTotal(), carroCompra.getCarroId(), usuario, operador, collectionPedido, jedis);
        String insert = "insert into operadores values ('Damian', 'Galvez', 13456)";
        String insertOperador = "insert into operadores values ('" + operador.getNombreOperador() + "', " + "'" + operador.getApellidoOperador() + "', " + operador.getDniOperador() + " )";
        stmt.executeUpdate(insertOperador);

        System.out.println("Fin");
    }
}

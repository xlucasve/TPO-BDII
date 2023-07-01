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
        conexion.connect("127.0.0.1", 9142);
        Session session = conexion.getSession();
        String query = "CREATE KEYSPACE ejemplo WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':1};";
        session.execute(query);
        session.execute("USE ejemplo");


        //Fin Cassandra

        String connectionUrl = "jdbc:sqlserver://0.0.0.0:1433;encrypt=false;databaseName=ECommerce;user=sa;password=SuperAdmin#";

        Connection conn = DriverManager.getConnection(connectionUrl);
        if (conn != null) {
            System.out.println("Conectado exitosamente");
        }

        Statement stmt = conn.createStatement();

        String select = "select * from operadores";
        System.out.println("Peticion: " + select);
        ResultSet rset = stmt.executeQuery(select);

        while (rset.next()){
            String nombre = rset.getString("nombre");
            System.out.println("Nombre: " + nombre);
            String apellido = rset.getString("apellido");
            System.out.println("Apellido: " + apellido);
            Integer dni = rset.getInt("dni");
            System.out.println("DNI: " + dni);
        }


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
        carroCompra.eliminarUnProducto(jedis, producto);

        //Obtener items de carro
        Map<String, String> pedidosCarro = jedis.hgetAll(carroCompra.getCarroId());
        for (String key : pedidosCarro.keySet()){
            String key1 = key;
            Integer cantidad = Integer.parseInt(pedidosCarro.get(key));
            System.out.println("Clave: " + key1);
            System.out.println("CANTIDAD: " + cantidad);
        }
        System.out.println("Pedido 1: " + producto.getProductoId());
        System.out.println("Pedido 2: " + producto2.getProductoId());


        //Crear nuevo Pedido
        Operador operador = new Operador(1, "Damian", "Galvez", 1243650);
        Pedido pedido1 = new Pedido(1, 20.4, carroCompra, usuario, operador, collectionPedido);
        System.out.println(pedido1.getPedidoId());

        //Cerramos la conexión
        jedis.close();
        mongoClient.close();


    }
}

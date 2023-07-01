package Modelos.Pedidos;
import Modelos.CarroCompras.CarroCompra;
import Modelos.CarroCompras.LineaProducto;
import Modelos.Operador.Operador;
import Modelos.Usuario.SesionUsuario;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import Modelos.Usuario.Usuario;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Pedido {

    private String pedidoId;

    private Integer condicionIVA;

    private Double descuento;

    private String idCarroCompra;

    private Usuario cliente;

    private Operador operadorResponsable;


    public Pedido(Integer condicionIVA, Double descuento,Double precioTotal, String idCarroCompra, Usuario cliente, Operador operadorResponsable, MongoCollection<Document> collectionPedido, JedisPooled jedis) {
        this.condicionIVA = condicionIVA;
        this.descuento = descuento;
        this.idCarroCompra = idCarroCompra;
        this.cliente = cliente;
        this.operadorResponsable = operadorResponsable;
        guardarPedido(precioTotal, collectionPedido, jedis);
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public Integer getCondicionIVA() {
        return condicionIVA;
    }

    public Double getDescuento() {
        return descuento;
    }

    public String getIdCarroCompra() {
        return idCarroCompra;
    }

    public Operador getOperadorResponsable() {
        return operadorResponsable;
    }

    private void guardarPedido (Double precioTotal, MongoCollection<Document> collectionPedido, JedisPooled jedis) {

        //Datos Cliente
        BasicDBObject objetoCliente = obtenerDatosCliente(this.cliente);

        //Datos Operador Responsable
        BasicDBObject objetoOpeardor = obtenerDatosOperador(this.operadorResponsable);


        //Datos items de pedido
        ArrayList<BasicDBObject> productosPedidos  = obtenerDatosPedido(getIdCarroCompra(), jedis);




        //Introduccion datos y otros varios
        Document document = new Document();
        document.put("cliente", objetoCliente);
        document.put("operador", objetoOpeardor);
        document.put("condicionIVA", getCondicionIVA());
        document.put("pedidos", productosPedidos);
        document.put("precioTotal", precioTotal);

        this.pedidoId = Objects.requireNonNull(collectionPedido.insertOne(document).getInsertedId()).asObjectId().getValue().toString();
    }

    private BasicDBObject obtenerDatosCliente(Usuario cliente){
        BasicDBObject objetoCliente = new BasicDBObject();
        objetoCliente.put("nombreCliente", cliente.getNombre());
        objetoCliente.put("apellidoCliente", cliente.getApellido());
        objetoCliente.put("direccionCliente", cliente.getDireccion());
        objetoCliente.put("ivaCliente", cliente.getCategoriaIVA());
        return objetoCliente;
    }

    private BasicDBObject obtenerDatosOperador(Operador operador){
        BasicDBObject objetoOpeardor = new BasicDBObject();
        objetoOpeardor.put("idOperador", operador.getIdOperador());
        objetoOpeardor.put("nombreOperador", operador.getNombreOperador());
        objetoOpeardor.put("apellidoOperador", operador.getApellidoOperador());
        objetoOpeardor.put("dniOperador", operador.getDniOperador());
        return objetoOpeardor;
    }

    private ArrayList<BasicDBObject> obtenerDatosPedido(String idCarroCompra, JedisPooled jedis){
        ArrayList<BasicDBObject> productosPedidos  = new ArrayList<>();
        Map<String, String> pedidosCarro = jedis.hgetAll(getIdCarroCompra());
        for (String key : pedidosCarro.keySet()) {
            BasicDBObject objetoProducto = new BasicDBObject();
            Integer cantidadProducto = Integer.parseInt(pedidosCarro.get(key));
            objetoProducto.put("idProducto", key);
            objetoProducto.put("cantidad", cantidadProducto);
            productosPedidos.add(objetoProducto);
        }
        return productosPedidos;
    }

}

package Modelos.Pedidos;
import Modelos.CarroCompras.CarroCompra;
import Modelos.CarroCompras.LineaProducto;
import Modelos.Operador.Operador;
import Modelos.Usuario.SesionUsuario;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import Modelos.Usuario.Usuario;
import java.util.ArrayList;
import java.util.Objects;

public class Pedido {

    private String pedidoId;

    private Integer condicionIVA;

    private Double descuento;

    private CarroCompra carroCompra;

    private Usuario cliente;

    private Operador operadorResponsable;


    public Pedido(Integer condicionIVA, Double descuento, CarroCompra carroCompra, Usuario cliente, Operador operadorResponsable, MongoCollection<Document> collectionPedido) {
        this.condicionIVA = condicionIVA;
        this.descuento = descuento;
        this.carroCompra = carroCompra;
        this.cliente = cliente;
        this.operadorResponsable = operadorResponsable;
        guardarPedido(collectionPedido);
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

    public Operador getOperadorResponsable() {
        return operadorResponsable;
    }

    private void guardarPedido (MongoCollection<Document> collectionPedido) {

        //Datos Cliente
        BasicDBObject objectoCliente = new BasicDBObject();
        objectoCliente.put("nombreCliente", this.cliente.getNombre());
        objectoCliente.put("apellidoCliente", this.cliente.getApellido());
        objectoCliente.put("direccionCliente", this.cliente.getDireccion());
        objectoCliente.put("ivaCliente", this.cliente.getCategoriaIVA());

        //Datos Operador Responsable
        BasicDBObject objectoOpeardor = new BasicDBObject();
        objectoOpeardor.put("idOperador", this.operadorResponsable.getIdOperador());
        objectoOpeardor.put("nombreOperador", this.operadorResponsable.getNombreOperador());
        objectoOpeardor.put("apellidoOperador", this.operadorResponsable.getApellidoOperador());
        objectoOpeardor.put("dniOperador", this.operadorResponsable.getDniOperador());

        //Datos items de pedido
        ArrayList<BasicDBObject> productosPedidos  = new ArrayList<>();
        for(LineaProducto lineaProducto : this.carroCompra.getLineaProductos()){
            BasicDBObject objetoProducto = new BasicDBObject();
            objetoProducto.put("idProducto", lineaProducto.getIdLinea());
            objetoProducto.put("cantidad", lineaProducto.getCantidad());
            objetoProducto.put("precioUnitario", lineaProducto.getProducto().getPrecioProducto());
            objetoProducto.put("nombre", lineaProducto.getProducto().getNombreProducto());
            objetoProducto.put("marca", lineaProducto.getProducto().getMarcaProducto());
            objetoProducto.put("modelo", lineaProducto.getProducto().getModeloProducto());
            productosPedidos.add(objetoProducto);
        }

        //Introduccion datos y otros varios
        Document document = new Document();
        document.put("cliente", objectoCliente);
        document.put("operador", objectoOpeardor);
        document.put("condicionIVA", getCondicionIVA());
        document.put("pedidos", productosPedidos);
        document.put("precioTotal", this.carroCompra.getPrecioTotal());

        this.pedidoId = Objects.requireNonNull(collectionPedido.insertOne(document).getInsertedId()).asObjectId().getValue().toString();
    }
}

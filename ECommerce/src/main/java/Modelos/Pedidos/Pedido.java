package Modelos.Pedidos;
import Modelos.CarroCompras.CarroCompra;
import Modelos.CarroCompras.LineaProducto;
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


    public Pedido(Integer condicionIVA, Double descuento, CarroCompra carroCompra, Usuario cliente, MongoCollection<Document> collectionPedido) {
        this.condicionIVA = condicionIVA;
        this.descuento = descuento;
        this.carroCompra = carroCompra;
        this.cliente = cliente;
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

    private void guardarPedido (MongoCollection<Document> collectionPedido) {
            //FALTA TERMINAR LA LOGICA

        BasicDBObject objectoCliente = new BasicDBObject();
        objectoCliente.put("nombreCliente", this.cliente.getNombre());
        objectoCliente.put("apellidoCliente", this.cliente.getApellido());
        objectoCliente.put("direccionCliente", this.cliente.getDireccion());
        objectoCliente.put("ivaCliente", this.cliente.getCategoriaIVA());

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

        Document document = new Document();
        document.put("cliente", objectoCliente);
        document.put("condicionIVA", getCondicionIVA());
        document.put("pedidos", productosPedidos);
        document.put("precioTotal", this.carroCompra.getPrecioTotal());
        document.put("descuento", getDescuento());

        this.pedidoId = Objects.requireNonNull(collectionPedido.insertOne(document).getInsertedId()).asObjectId().getValue().toString();
    }
}

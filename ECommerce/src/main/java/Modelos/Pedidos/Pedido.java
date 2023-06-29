package Modelos.Pedidos;
import Modelos.CarroCompras.CarroCompra;
import Modelos.Usuario.SesionUsuario;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import Modelos.Usuario.Usuario;
import java.util.ArrayList;

public class Pedido {

    private Integer pedidoId;

    private Integer ordenCompra;

    private Integer condicionIVA;

    private Integer descuento;

    private CarroCompra CarroCompra;

    private Usuario Usuario;

    private ArrayList<Usuario> Cliente;

    public Pedido(Integer pedidoId, Integer ordenCompra, Integer condicionIVA, Integer descuento, MongoCollection<Document> collectionPedido) {
        this.pedidoId = pedidoId;
        this.ordenCompra = ordenCompra;
        this.condicionIVA = condicionIVA;
        this.descuento = descuento;
        this.CarroCompra = CarroCompra;
        this.Usuario = Usuario;
    }

    public Integer pedidoId() {
        return pedidoId;
    }

    public Integer ordenCompra() {
        return ordenCompra;
    }

    public Integer condicionIVA() {
        return condicionIVA;
    }

    public Integer descuento() {
        return descuento;
    }

    public void almacenarPedido (CarroCompra CarroCompra, MongoCollection<Document> collectionPedido, Usuario Usuario) {


        Document document = new Document();

        document.put("pedidoId", this.pedidoId);
        document.put("ordenCompra", this.ordenCompra);
        document.put("condicionIVA", this.condicionIVA);
        document.put("descuento", this.descuento);

        collectionPedido.insertOne(document);

    }
}

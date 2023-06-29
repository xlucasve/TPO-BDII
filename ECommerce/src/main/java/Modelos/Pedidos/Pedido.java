package Modelos.Pedidos;
import Modelos.CarroCompras.CarroCompra;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Pedido {

    private Integer pedidoId;

    private Integer condicionIVA;

    private CarroCompra CarroCompra;

    public Pedido(Integer pedidoId, Integer condicionIVA, CarroCompra CarroCompra, MongoCollection<Document> collectionPedido) {
        this.pedidoId = pedidoId;
        this.condicionIVA = condicionIVA;
        this.CarroCompra = CarroCompra;
    }

    public Integer pedidoId() {
        return pedidoId;
    }

    public Integer condicionIVA() {
        return condicionIVA;
    }

    public CarroCompra CarroCompra() {
        return CarroCompra;
    }

    public void almacenarPedido () {


    }
}

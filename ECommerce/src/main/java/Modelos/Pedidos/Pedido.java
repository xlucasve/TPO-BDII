package Modelos.Pedidos;
import Modelos.CarroCompras.CarroCompra;
import Modelos.Usuario.SesionUsuario;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import Modelos.Usuario.Usuario;
import java.util.ArrayList;

public class Pedido {

    private String pedidoId;

    private Integer ordenCompra;

    private Integer condicionIVA;

    private Integer descuento;

    private CarroCompra CarroCompra;

    private Usuario Usuario;

    private ArrayList<Usuario> Cliente;

    public Pedido(String pedidoId, Integer ordenCompra, Integer condicionIVA, Integer descuento, MongoCollection<Document> collectionPedido) {
        this.pedidoId = pedidoId;
        this.ordenCompra = ordenCompra;
        this.condicionIVA = condicionIVA;
        this.descuento = descuento;
        this.CarroCompra = CarroCompra;
        this.Usuario = Usuario;
    }

    public String pedidoId() {
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
        ArrayList<BasicDBObject> datosClienteArray = new ArrayList<>();
        for (int i = 0; i < this.Cliente.size(); i++){

            //FALTA TERMINAR LA LOGICA
           if(this.Cliente.get(i).getUsuarioId() == this.CarroCompra.getCarroId()) {
               BasicDBObject objetoSesion = new BasicDBObject();
               objetoSesion.put("nombre", this.Cliente.get(i).getNombre());
               objetoSesion.put("dni", this.Cliente.get(i).getDocumentoIdentidad());
               objetoSesion.put("direccion", this.Cliente.get(i).getDireccion());
               datosClienteArray.add(objetoSesion);
           }
        }

        Document document = new Document();

        document.put("pedidoId", this.pedidoId);
        document.put("ordenCompra", this.ordenCompra);
        document.put("condicionIVA", this.condicionIVA);
        document.put("descuento", this.descuento);

        collectionPedido.insertOne(document);

    }
}

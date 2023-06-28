package Modelos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Producto {
    private String productoId;
    private String nombreProducto;
    private String marcaProducto;
    private String modeloProducto;
    private Double anchoEnPulgadas;
    private Double alturaEnPulgadas;
    private Double precioProducto;

    public Producto(String productoId, String nombreProducto, String marcaProducto, String modeloProducto,
                    Double anchoEnPulgadas, Double alturaEnPulgadas, Double precioProducto,
                    MongoCollection<Document> collectionCatalogoProductos,
                    MongoCollection<Document> collectionListadoPrecios) {
        this.nombreProducto = nombreProducto;
        this.marcaProducto = marcaProducto;
        this.modeloProducto = modeloProducto;
        this.anchoEnPulgadas = anchoEnPulgadas;
        this.alturaEnPulgadas = alturaEnPulgadas;
        this.precioProducto = precioProducto;
        agregarProductoACatalogo(collectionCatalogoProductos);
        agregarProductoAListadoPrecios(collectionListadoPrecios);
    }

    public String getProductoId(){return productoId;}

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public String getModeloProducto() {
        return modeloProducto;
    }

    public Double getAnchoEnPulgadas() {
        return anchoEnPulgadas;
    }

    public Double getAlturaEnPulgadas() {
        return alturaEnPulgadas;
    }

    public Double getPrecioProducto() {
        return precioProducto;
    }

    private void agregarProductoACatalogo(MongoCollection<Document> collectionCatalogoProductos){
        Document document = new Document();
        document.put("nombreProducto", this.nombreProducto);
        document.put("marcaProducto", this.marcaProducto);
        document.put("modeloProducto", this.modeloProducto);
        document.put("anchoEnPulgadas", this.anchoEnPulgadas);
        document.put("alturaEnPulgadas", this.alturaEnPulgadas);

        this.productoId = collectionCatalogoProductos.insertOne(document).getInsertedId().toString();
    }

    private void agregarProductoAListadoPrecios(MongoCollection<Document> collectionListadoPrecios){
        Document document = new Document();
        document.put("productoId", this.productoId);
        document.put("precio", this.precioProducto);
        collectionListadoPrecios.insertOne(document);
    }

    public void actualizarPrecioProducto(MongoCollection<Document> collectionListadoPrecios, Double nuevoPrecio){
        this.precioProducto = nuevoPrecio;

        Document query = new Document();
        query.put("productoId", this.productoId);

        Bson updatePrecio = Updates.set("precio", this.precioProducto);

        collectionListadoPrecios.updateOne(query, updatePrecio);

    }
}

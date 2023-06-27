package Modelos;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Producto {
    private String nombreProducto;
    private String marcaProducto;
    private String modeloProducto;
    private Double anchoEnPulgadas;
    private Double alturaEnPulgadas;

    private MongoCollection<Document> collectionCatalogoProductos;

    public Producto(String nombreProducto, String marcaProducto, String modeloProducto, Double anchoEnPulgadas, Double alturaEnPulgadas, MongoCollection<Document> collectionCatalogoProductos) {
        this.nombreProducto = nombreProducto;
        this.marcaProducto = marcaProducto;
        this.modeloProducto = modeloProducto;
        this.anchoEnPulgadas = anchoEnPulgadas;
        this.alturaEnPulgadas = alturaEnPulgadas;
        this.collectionCatalogoProductos = collectionCatalogoProductos;
        agregarProductoACatalogo();
    }

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

    public MongoCollection<Document> getCollectionCatalogoProductos() {
        return collectionCatalogoProductos;
    }

    public void agregarProductoACatalogo(){
        Document document = new Document();
        document.put("nombreProducto", this.nombreProducto);
        document.put("marcaProducto", this.marcaProducto);
        document.put("modeloProducto", this.modeloProducto);
        document.put("anchoEnPulgadas", this.anchoEnPulgadas);
        document.put("alturaEnPulgadas", this.alturaEnPulgadas);

        collectionCatalogoProductos.insertOne(document);
    }
}

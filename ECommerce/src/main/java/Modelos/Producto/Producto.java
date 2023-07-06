package Modelos.Producto;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;

import Modelos.LogCambiosProducto.CambioProducto;
import Modelos.LogCambiosProducto.ProductChangeHandler;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Objects;

public class Producto {
    private String productoId;
    private String nombreProducto;
    private String marcaProducto;
    private String modeloProducto;
    private Double anchoEnPulgadas;
    private Double alturaEnPulgadas;
    private Double precioProducto;
    private ArrayList<Review> reviews;
    private Double calificacionProducto;
    private Double precioAnterior;

    public Producto(String nombreProducto, String marcaProducto, String modeloProducto,
                    Double anchoEnPulgadas, Double alturaEnPulgadas, Double precioProducto,
                    Double calificacionProducto,
                    MongoCollection<Document> collectionCatalogoProductos,
                    MongoCollection<Document> collectionListadoPrecios) {
        this.nombreProducto = nombreProducto;
        this.marcaProducto = marcaProducto;
        this.modeloProducto = modeloProducto;
        this.anchoEnPulgadas = anchoEnPulgadas;
        this.alturaEnPulgadas = alturaEnPulgadas;
        this.precioProducto = precioProducto;
        Review review = new Review("Mateo", "Muy buen producto", 4.4);
        this.reviews = new ArrayList<>();
        this.reviews.add(review);
        this.calificacionProducto = calificacionProducto;
        agregarProductoACatalogo(collectionCatalogoProductos);
        agregarProductoAListadoPrecios(collectionListadoPrecios);
        this.precioAnterior = precioProducto;
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

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public Double getCalificacionProducto() {
        return calificacionProducto;
    }

    private CambioProducto cambiosProductoDTO(String operadorId) {
        return new CambioProducto(
            this.productoId,
            this.nombreProducto,
            this.marcaProducto,
            this.modeloProducto,
            this.anchoEnPulgadas,
            this.alturaEnPulgadas,
            this.precioProducto,
            this.calificacionProducto,
            this.precioAnterior,
            operadorId

            );
    }


    private void agregarProductoACatalogo(MongoCollection<Document> collectionCatalogoProductos){

        ArrayList<BasicDBObject> reviewsArray = new ArrayList<>();
        for(Review review : this.reviews){
        BasicDBObject objetoSesion = new BasicDBObject();
            objetoSesion.put("nombreUsuario", review.getNombreUsuarioReview());
            objetoSesion.put("comentario", review.getComentario());
            objetoSesion.put("calificacion", review.getCalificacion());
            objetoSesion.put("likesReview", review.getLikeCounter());
            reviewsArray.add(objetoSesion);
        }

        Document document = new Document();
        document.put("nombreProducto", this.nombreProducto);
        document.put("marcaProducto", this.marcaProducto);
        document.put("modeloProducto", this.modeloProducto);
        document.put("anchoEnPulgadas", this.anchoEnPulgadas);
        document.put("alturaEnPulgadas", this.alturaEnPulgadas);
        document.put("calificacion", this.calificacionProducto);
        document.put("reviews", reviewsArray);

        this.productoId = Objects.requireNonNull(collectionCatalogoProductos.insertOne(document).getInsertedId()).asObjectId().getValue().toString();

        //Para insertar producto nuevo en Cassandra log

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO("0"));
        //
    }

    private void agregarProductoAListadoPrecios(MongoCollection<Document> collectionListadoPrecios){
        Document document = new Document();
        document.put("productoId", this.productoId);
        document.put("precio", this.precioProducto);
        collectionListadoPrecios.insertOne(document);
    }

    public void actualizarPrecioProducto(MongoCollection<Document> collectionListadoPrecios, Double nuevoPrecio, String operadorId){
        
        this.precioAnterior = this.precioProducto;
        this.precioProducto = nuevoPrecio;

        Document query = new Document();
        query.put("productoId", this.productoId);

        Bson updatePrecio = Updates.set("precio", this.precioProducto);

        collectionListadoPrecios.updateOne(query, updatePrecio);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }

    public void actualizarNombreProducto(MongoCollection<Document> collectionCatalogoProductos, String nuevoNombre, String operadorId) {
        this.nombreProducto = nuevoNombre;

        Document query = new Document();
        query.put("_id", new ObjectId(this.productoId));

        Bson updateNombre = Updates.set("nombreProducto", this.nombreProducto);
        collectionCatalogoProductos.updateOne(query, updateNombre);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }

    public void actualizarMarcaProducto(MongoCollection<Document> collectionCatalogoProductos, String nuevaMarca, String operadorId) {
         this.marcaProducto = nuevaMarca;

        Document query = new Document();
        query.put("_id", new ObjectId(this.productoId));

        Bson updateMarca = Updates.set("marcaProducto", this.marcaProducto);
        collectionCatalogoProductos.updateOne(query, updateMarca);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }

    public void actualizarModeloProducto(MongoCollection<Document> collectionCatalogoProductos, String nuevoModelo, String operadorId) {
        this.modeloProducto = nuevoModelo;

        Document query = new Document();
        query.put("_id", new ObjectId(this.productoId));

        Bson updateModelo = Updates.set("modeloProducto", this.modeloProducto);
        collectionCatalogoProductos.updateOne(query, updateModelo);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }

    public void actualizarAnchoEnPulgadas(MongoCollection<Document> collectionCatalogoProductos, Double nuevoAnchoEnPulgadas, String operadorId) {
        this.anchoEnPulgadas = nuevoAnchoEnPulgadas;

        Document query = new Document();
        query.put("_id", new ObjectId(this.productoId));

        Bson updateField = Updates.set("anchoEnPulgadas", this.anchoEnPulgadas);
        collectionCatalogoProductos.updateOne(query, updateField);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }

    public void actualizarAlturaEnPulgadas(MongoCollection<Document> collectionCatalogoProductos, Double nuevaAlturaEnPulgadas, String operadorId) {
        this.alturaEnPulgadas = nuevaAlturaEnPulgadas;

        Document query = new Document();
        query.put("_id", new ObjectId(this.productoId));

        Bson updateField = Updates.set("alturaEnPulgadas", this.alturaEnPulgadas);
        collectionCatalogoProductos.updateOne(query, updateField);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }

    public void actualizarCalificacionProducto(MongoCollection<Document> collectionCatalogoProductos, Double nuevaCalificacion, String operadorId) {
        this.calificacionProducto = nuevaCalificacion;

        Document query = new Document();
        query.put("_id", new ObjectId(this.productoId));

        Bson updateField = Updates.set("calificacionProducto", this.calificacionProducto);
        collectionCatalogoProductos.updateOne(query, updateField);

        ProductChangeHandler.getInstance(null).saveProductChange(cambiosProductoDTO(operadorId));
    }



}

package Modelos.LogCambiosProducto;

import java.util.ArrayList;

import Modelos.Producto.Review;

public class CambioProducto {
    private String productoId;
    //private String fechaModificacion;
    private String nombreProducto;
    private String marcaProducto;
    private String modeloProducto;
    private Double anchoEnPulgadas;
    private Double alturaEnPulgadas;
    private Double precioProducto;
    // private ArrayList<Review> reviews;
    private Double calificacionProducto;

    public CambioProducto(String productoId, String nombreProducto, String marcaProducto,
            String modeloProducto, Double anchoEnPulgadas, Double alturaEnPulgadas, Double precioProducto,
            Double calificacionProducto) {
        this.productoId = productoId;
       // this.fechaModificacion = fechaModificacion;
        this.nombreProducto = nombreProducto;
        this.marcaProducto = marcaProducto;
        this.modeloProducto = modeloProducto;
        this.anchoEnPulgadas = anchoEnPulgadas;
        this.alturaEnPulgadas = alturaEnPulgadas;
        this.precioProducto = precioProducto;
        this.calificacionProducto = calificacionProducto;
    }

    public String getProductoId() {
        return productoId;
    }
    /* 
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    */
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

    // public ArrayList<Review> getReviews() {
    //     return reviews;
    // }

    public Double getCalificacionProducto() {
        return calificacionProducto;
    }
}

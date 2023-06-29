package Modelos.CarroCompras;

import Modelos.Producto.Producto;

public class LineaProducto {
    private String idLinea;
    private Producto producto;
    private Integer cantidad;

    public LineaProducto(String idLinea, Producto producto) {
        this.idLinea = idLinea;
        this.producto = producto;
        this.cantidad = 1;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public Producto getProducto() {
        return producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void agregarUno(){
        this.cantidad++;
    }

    public boolean eliminarUno(){
        if (this.cantidad > 0) {
            this.cantidad--;
            return true;
        }
        return false;
    }

}

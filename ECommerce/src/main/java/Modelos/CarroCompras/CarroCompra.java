package Modelos.CarroCompras;

import Modelos.Producto;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.Objects;

public class CarroCompra {

    private String carroId;
    private ArrayList<LineaProducto> lineaProductos;
    private Integer cantidadItems;
    private Double precioTotal;

    public CarroCompra(String carroId) {
        this.carroId = carroId;
        this.lineaProductos = new ArrayList<>();
        this.cantidadItems = 0;
        this.precioTotal = 0.0;
    }

    public void agregarProducto(JedisPooled jedis, Producto producto){
        boolean agregado = false;
        for (LineaProducto lineaProducto : this.lineaProductos){
            if (Objects.equals(lineaProducto.getProducto().getProductoId(), producto.getProductoId())){
                lineaProducto.agregarUno();
                this.precioTotal += producto.getPrecioProducto();
                jedis.hset(this.carroId, lineaProducto.getIdLinea(), lineaProducto.getCantidad().toString());
                agregado = true;
            }
        }
        if(!agregado) {
            agregarLineaProducto(jedis, producto);
        }
    }

    private void agregarLineaProducto(JedisPooled jedis, Producto producto){
        LineaProducto lineaProducto = new LineaProducto(producto.getProductoId(), producto);
        this.lineaProductos.add(lineaProducto);
        jedis.hset(this.carroId, lineaProducto.getIdLinea(), lineaProducto.getCantidad().toString());
        System.out.println("LINEA PRODUCTO ID: " + lineaProducto.getIdLinea());
        this.precioTotal += producto.getPrecioProducto();
        this.cantidadItems++;
    }

    public void eliminarUnProducto(JedisPooled jedis, Producto producto){
        for (LineaProducto lineaProducto : this.lineaProductos){
            if (lineaProducto.getProducto().equals(producto)){
                boolean eliminado = lineaProducto.eliminarUno();
                if(eliminado){
                    this.precioTotal -= producto.getPrecioProducto();
                    jedis.hset(this.carroId, lineaProducto.getIdLinea(), lineaProducto.getCantidad().toString());

                    System.out.println("Se elimino el producto");
                } else{
                    this.lineaProductos.remove(lineaProducto);
                    jedis.hdel(this.carroId, lineaProducto.getIdLinea());
                    System.out.println("La linea de producto fue eliminada");
                }
            }
        }
    }
}

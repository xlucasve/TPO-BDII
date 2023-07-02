package Modelos.CarroCompras;

import Modelos.CarroCompras.MementoPattern.Memento;
import Modelos.Producto.Producto;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class CarroCompra {

    private String carroId;
    private ArrayList<LineaProducto> lineaProductos;
    private Integer cantidadItems;
    private Double precioTotal;
    private Deque<Memento> mementos;

    public CarroCompra(String carroId) {
        this.carroId = carroId;
        this.lineaProductos = new ArrayList<>();
        this.cantidadItems = 0;
        this.precioTotal = 0.0;
        this.mementos = new LinkedList<>();
    }

    public String getCarroId() {
        return carroId;
    }

    public ArrayList<LineaProducto> getLineaProductos() {
        return lineaProductos;
    }

    public Integer getCantidadItems() {
        return cantidadItems;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void undo(JedisPooled jedis){
        if(mementos.size() > 0){
            lineaProductos.clear();
            copiarLineas(mementos.getLast().obtenerLineas());
            mementos.removeLast();
            jedis.del(this.carroId);

            for (LineaProducto lineaProducto : lineaProductos){
                jedis.hset(this.carroId, lineaProducto.getIdLinea(), lineaProducto.getCantidad().toString());
            }
        }
        else{
            jedis.del(this.carroId);
            System.out.println("No hay estados previos");
        }

    }

    public void copiarLineas(ArrayList<LineaProducto> lineasCopiar){
        lineaProductos.addAll(lineasCopiar);
    }

    public void tomarSnapshot(){
        mementos.add(new Memento(lineaProductos));
    }

    public void agregarProducto(JedisPooled jedis, Producto producto){
        tomarSnapshot();
        boolean agregado = false;
        for (LineaProducto lineaProducto : this.lineaProductos){
            if (Objects.equals(lineaProducto.getProducto().getProductoId(), producto.getProductoId())){
                lineaProducto.agregarUno();
                aumentarPrecio(producto);
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
        aumentarPrecio(producto);
        this.cantidadItems++;
    }

    public void eliminarUnProducto(JedisPooled jedis, Producto producto){
        tomarSnapshot();
        for (LineaProducto lineaProducto : this.lineaProductos){
            if (lineaProducto.getProducto().equals(producto)){
                boolean eliminado = lineaProducto.eliminarUno();
                if(eliminado){
                    jedis.hset(this.carroId, lineaProducto.getIdLinea(), lineaProducto.getCantidad().toString());

                } else{
                    this.lineaProductos.remove(lineaProducto);
                    jedis.hdel(this.carroId, lineaProducto.getIdLinea());
                }
            }
        }
        this.precioTotal -= producto.getPrecioProducto();
        System.out.println("producto eliminado");
    }


    private void aumentarPrecio(Producto producto){
        this.precioTotal += producto.getPrecioProducto();
    }
}

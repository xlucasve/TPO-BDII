package Modelos.CarroCompras.MementoPattern;

import Modelos.CarroCompras.LineaProducto;

import java.util.ArrayList;

public class Memento {
    private final ArrayList<LineaProducto> lineas;

    public Memento(ArrayList<LineaProducto> lineas) {
        this.lineas = new ArrayList<>();
        this.lineas.addAll(lineas);
    }

    public ArrayList<LineaProducto> obtenerLineas(){
        return lineas;
    }
}

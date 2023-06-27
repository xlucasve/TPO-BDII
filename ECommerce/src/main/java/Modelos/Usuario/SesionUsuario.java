package Modelos.Usuario;

import java.util.Date;

public class SesionUsuario {

    private Date tiempoInicio;
    private Date tiempoFinalizado;

    public SesionUsuario(Date tiempoInicio, Date tiempoFinalizado) {
        this.tiempoInicio = tiempoInicio;
        this.tiempoFinalizado = tiempoFinalizado;
    }

    public Date getTiempoInicio() {
        return tiempoInicio;
    }

    public Date getTiempoFinalizado() {
        return tiempoFinalizado;
    }

    public CategoriaUsuario calcularDiferenciaEnMinutos(){
        Long diferenciaEnMs = this.tiempoFinalizado.getTime() - this.tiempoInicio.getTime();
        Long diferenciaEnMinutos = diferenciaEnMs / (60 * 1000) % 60;
        if (diferenciaEnMinutos > 240){
            return CategoriaUsuario.TOP;
        } else if (diferenciaEnMinutos > 120){
            return CategoriaUsuario.MEDIUM;
        }else {
            return CategoriaUsuario.LOW;
        }
    }
}

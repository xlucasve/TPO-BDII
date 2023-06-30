package Modelos.Operador;

public class Operador {
    private Integer idOperador;
    private String nombreOperador;
    private String apellidoOperador;
    private Integer dniOperador;

    public Operador(Integer idOperador, String nombreOperador, String apellidoOperador, Integer dniOperador) {
        this.idOperador = idOperador;
        this.nombreOperador = nombreOperador;
        this.apellidoOperador = apellidoOperador;
        this.dniOperador = dniOperador;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public String getNombreOperador() {
        return nombreOperador;
    }

    public String getApellidoOperador() {
        return apellidoOperador;
    }

    public Integer getDniOperador() {
        return dniOperador;
    }
}

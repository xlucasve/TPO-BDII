package Modelos.Operador;

import javax.xml.transform.Result;
import java.sql.*;

public class Operador {
    private Integer idOperador;
    private String nombreOperador;
    private String apellidoOperador;
    private Integer dniOperador;

    public Operador(String nombreOperador, String apellidoOperador, Integer dniOperador, Connection connectionSQL) throws SQLException {
        this.nombreOperador = nombreOperador;
        this.apellidoOperador = apellidoOperador;
        this.dniOperador = dniOperador;
        insertarOperador(connectionSQL);
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


    public void insertarOperador(Connection connectionSQL) throws SQLException {
        PreparedStatement statementOperador = connectionSQL.prepareStatement("insert into operadores values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statementOperador.setString(1, getNombreOperador());
        statementOperador.setString(2, getApellidoOperador());
        statementOperador.setInt(3, getDniOperador());

        statementOperador.executeUpdate();

        ResultSet rset = statementOperador.getGeneratedKeys();

        if(rset.next()){
            this.idOperador = rset.getInt(1);
        } else{
            System.out.println("No se genero correctamente el id");
        }
    }
}

package Modelos.Factura;

import Modelos.Operador.Operador;
import Modelos.Pedidos.Pedido;
import Modelos.Usuario.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneradorFactura {
    private static GeneradorFactura instancia;

    public static GeneradorFactura getInstancia() {
        if (instancia == null) {
            instancia = new GeneradorFactura();
        }
        return instancia;
    }

    public void generarFactura(Pedido pedido, Operador operadorResponsable, Usuario cliente, Integer monto, String metodoPago, Connection connectionSQL) throws SQLException {
        int idPago = insertarPago(monto, metodoPago ,connectionSQL);
        if (idPago == 0){
            System.out.println("Error al registrar el pago");
        } else{
            PreparedStatement statementFactura = connectionSQL.prepareStatement("insert into facturas values (?,?,?)");
            statementFactura.setInt(1, operadorResponsable.getIdOperador());
            statementFactura.setInt(2, cliente.getUsuarioIdSQL());
            statementFactura.setInt(3, idPago);

            statementFactura.executeUpdate();
        }

    }

    private int insertarPago(Integer monto, String metodoPago, Connection connectionSQL) throws SQLException {
        PreparedStatement statementPago = connectionSQL.prepareStatement("insert into pagos values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        java.util.Date fechaNow = new java.util.Date();
        Long fechaNowLong = fechaNow.getTime();
        Date fecha = new Date(fechaNowLong);

        statementPago.setDate(1, fecha);
        statementPago.setString(2, metodoPago);
        statementPago.setInt(3, monto);

        statementPago.executeUpdate();

        ResultSet rset = statementPago.getGeneratedKeys();

        if(rset.next()){
            return rset.getInt(1);
        }else{
            return 0;
        }
    }


}


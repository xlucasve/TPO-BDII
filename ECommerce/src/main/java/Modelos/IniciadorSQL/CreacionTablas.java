package Modelos.IniciadorSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreacionTablas {

    public void CreacionInicialTablas (String conexionURL) {
 

        try (Connection connection = DriverManager.getConnection(conexionURL);
             Statement statement = connection.createStatement()) {

            String comandosSQL = "USE ECommerce;\n" +
                    "CREATE TABLE [facturas] (\n" +
                    "  [factura_id] integer IDENTITY(1,1) PRIMARY KEY,\n" +
                    "  [operador_responsable] integer,\n" +
                    "  [cliente] integer,\n" +
                    "  [pago_pedido] integer\n" +
                    ");\n" +
                    "CREATE TABLE [pagos] (\n" +
                    "  [pago_id] integer IDENTITY(1,1) PRIMARY KEY,\n" +
                    "  [fecha_pago] datetime,\n" +
                    "  [metodo_pago] varchar(255),\n" +
                    "  [monto] integer\n" +
                    ");\n" +
                    "CREATE TABLE [clientes] (\n" +
                    "  [cliente_id] integer IDENTITY(1,1) PRIMARY KEY,\n" +
                    "  [cliente_id_sistema] varchar(255),\n" +
                    "  [nombre] varchar(255),\n" +
                    "  [apellido] varchar(255),\n" +
                    "  [direccion] varchar(255),\n" +
                    "  [dni] integer\n" +
                    ");\n" +
                    "CREATE TABLE [operadores] (\n" +
                    "  [operador_id] integer IDENTITY(1,1) PRIMARY KEY,\n" +
                    "  [nombre] varchar(255),\n" +
                    "  [apellido] varchar(255),\n" +
                    "  [dni] integer\n" +
                    ");\n" +
                    "ALTER TABLE [facturas] ADD FOREIGN KEY ([operador_responsable]) REFERENCES [operadores] ([operador_id]);\n" +
                    "ALTER TABLE [facturas] ADD FOREIGN KEY ([pago_pedido]) REFERENCES [pagos] ([pago_id]);\n" +
                    "ALTER TABLE [facturas] ADD FOREIGN KEY ([cliente]) REFERENCES [clientes] ([cliente_id]);";
            
            statement.executeUpdate(comandosSQL);
            System.out.println("Tablas Creadas.");
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
}

   
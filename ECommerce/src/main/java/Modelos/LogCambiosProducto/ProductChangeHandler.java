package Modelos.LogCambiosProducto;
import conexionesBD.CassandraConnector;
import conexionesBD.KeyspaceRepository;

import java.time.ZoneOffset;

import org.apache.cassandra.schema.SchemaKeyspace;
import com.datastax.driver.core.Session;
import com.mysql.cj.protocol.Resultset;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

//import org.apache.commons.text.


public class ProductChangeHandler {
    
    private static ProductChangeHandler instance;
    private Session session;

    private ProductChangeHandler(Session session) {
        this.session = session;
    }

    public static final ProductChangeHandler getInstance(Session session) {
        if (instance == null) {
            instance = new ProductChangeHandler(session);
            System.out.println("New Instance!");
        }
        return instance;
    }

  public void createProductChangesTable() {
    session.execute("CREATE TABLE IF NOT EXISTS product_logs.product_changes (" +
        "productId text, " +
        "fechaModificacion timestamp, " +
        "nombreProducto text, " +
        "marcaProducto text, " +
        "modeloProducto text, " +
        "anchoEnPulgadas double, " + 
        "alturaEnPulgadas double, " +
        "precioAnterior double, " +
        "precioProducto double, " +
        "calificacionProducto double, " + 
        "PRIMARY KEY (productId, fechaModificacion)" +
        ")");
  }

    public void saveProductChange(CambioProducto cambioProducto) {

    LocalDateTime fechaModificacion = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String fechaFormateada = fechaModificacion.format(formatter);
    long timestamp = fechaModificacion.toInstant(ZoneOffset.UTC).toEpochMilli();    

    session.execute("INSERT INTO product_logs.product_changes " +
        "(productId, fechaModificacion, nombreProducto, marcaProducto, modeloProducto, anchoEnPulgadas, alturaEnPulgadas, precioAnterior, precioProducto, calificacionProducto) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        cambioProducto.getProductoId(),
        timestamp,
        cambioProducto.getNombreProducto(),
        cambioProducto.getMarcaProducto(),
        cambioProducto.getModeloProducto(),
        cambioProducto.getAnchoEnPulgadas(),
        cambioProducto.getAlturaEnPulgadas(),
        //
        cambioProducto.getPrecioAnterior(),

        cambioProducto.getPrecioProducto(),
        cambioProducto.getCalificacionProducto()
        
        );
    }

    

    //quizas dividir en dos metodos, uno que devuelva el objeto resultado y otro que lo parsee/imprima.
    public void consultarLogPorId(String idProducto) {

        String query = "SELECT * FROM product_changes WHERE productid = '" + idProducto + "' ORDER BY fechamodificacion;";
        ResultSet resultado = this.session.execute(query);

        System.out.println("productid                | fechamodificacion         | alturaenpulgadas | anchoenpulgadas | calificacionproducto | marcaproducto | modeloproducto | nombreproducto | precioanterior | precioproducto");
        System.out.println("--------------------------+---------------------------------+------------------+-----------------+----------------------+---------------+----------------+----------------+----------------");

        for(Row row: resultado) {
            
            System.out.print(row.getString("productid") + "|");
            System.out.print(" " + row.getTimestamp("fechamodificacion") + "|");
            System.out.print(" " + row.getDouble("alturaenpulgadas") + "           |");
            System.out.print(" " + row.getDouble("anchoenpulgadas") + "           |");
            System.out.print(" "+row.getDouble("calificacionproducto") + "                  |");
            System.out.print(" "+row.getString("marcaproducto") + "         |");
            System.out.print(" " + row.getString("modeloproducto") + "  |");
            System.out.print(" " + row.getString("nombreproducto") + "         |");
            System.out.println(" " + row.getDouble("precioanterior") + "            |");
            System.out.print(" " + row.getDouble("precioproducto"));

            System.out.println();


        }





    }


}

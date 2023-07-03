package Modelos.LogCambiosProducto;
import conexionesBD.CassandraConnector;
import conexionesBD.KeyspaceRepository;

import java.time.ZoneOffset;

import org.apache.cassandra.schema.SchemaKeyspace;
import com.datastax.driver.core.Session;

import com.datastax.driver.core.Cluster;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;



public class ProductChangeHandler {
    
    private static ProductChangeHandler instance;
    private Session session;

    private ProductChangeHandler(Session session) {
        this.session = session;
    }

    public static synchronized ProductChangeHandler getInstance(Session session) {
        if (instance == null) {
            instance = new ProductChangeHandler(session);
        }
        return instance;
    }

  public void createProductChangesTable() {
    session.execute("CREATE TABLE IF NOT EXISTS ejemplo.product_changes (" +
        "productId text, " +
        "fechaModificacion timestamp, " +
        "nombreProducto text, " +
        "marcaProducto text, " +
        "modeloProducto text, " +
        "anchoEnPulgadas double, " + 
        "alturaEnPulgadas double, " +
        "precioProducto double, " +
        "calificacionProducto double, " + 
        "PRIMARY KEY (productId, fechaModificacion)" +
        ")");
  }

    public void saveProductChange(CambioProducto cambioProducto) {

    LocalDateTime fechaModificacion = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDate = fechaModificacion.format(formatter);
    long timestamp = fechaModificacion.toInstant(ZoneOffset.UTC).toEpochMilli();    

    session.execute("INSERT INTO ejemplo.product_changes " +
        "(productId, fechaModificacion, nombreProducto, marcaProducto, modeloProducto, anchoEnPulgadas, alturaEnPulgadas, precioProducto, calificacionProducto) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
        cambioProducto.getProductoId(),
        //cambioProducto.getFechaModificacion(),
        timestamp,
        cambioProducto.getNombreProducto(),
        cambioProducto.getMarcaProducto(),
        cambioProducto.getModeloProducto(),
        cambioProducto.getAnchoEnPulgadas(),
        cambioProducto.getAlturaEnPulgadas(),
        cambioProducto.getPrecioProducto(),
        cambioProducto.getCalificacionProducto()
        
       

    );
}


}

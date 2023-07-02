package Modelos.Usuario;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Date;

public class Usuario {

    private String usuarioId;
    private String nombre;
    private String apellido;
    private String direccion;
    private Integer documentoIdentidad;
    private CategoriaIVA categoriaIVA;
    private CategoriaUsuario categoriaUsuario;
    private ArrayList<SesionUsuario> sesionesDeUsuario;

    public Usuario(String nombre, String apellido, String direccion, Integer documentoIdentidad, CategoriaIVA categoriaIVA, MongoCollection<Document> collectionUsuarios) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.documentoIdentidad = documentoIdentidad;
        this.categoriaUsuario = CategoriaUsuario.LOW;
        this.categoriaIVA = categoriaIVA;
        this.sesionesDeUsuario = new ArrayList<>();
        SesionUsuario sesionUsuario = new SesionUsuario(new Date(), new Date());
        this.sesionesDeUsuario.add(sesionUsuario);
        agregarUsuarioAColeccion(collectionUsuarios);
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public Integer getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public CategoriaIVA getCategoriaIVA() {
        return categoriaIVA;
    }

    public void agregarUsuarioAColeccion(MongoCollection<Document> collectionUsuarios){

        ArrayList<BasicDBObject> sesionesArray = new ArrayList<>();
        for (SesionUsuario sesionUsuario : this.sesionesDeUsuario) {
            BasicDBObject objetoSesion = new BasicDBObject();
            objetoSesion.put("inicio", sesionUsuario.getTiempoInicio().getTime());
            objetoSesion.put("finalizado", sesionUsuario.getTiempoFinalizado().getTime());
            sesionesArray.add(objetoSesion);
        }

        Document document = new Document();
        document.put("nombre", this.nombre);
        document.put("apellido", this.apellido);
        document.put("direccion", this.direccion);
        document.put("dni", this.documentoIdentidad);
        document.put("categoria", this.categoriaUsuario);
        document.put("sesiones", sesionesArray);
        this.usuarioId = Objects.requireNonNull(collectionUsuarios.insertOne(document).getInsertedId()).asObjectId().getValue().toString();

    }

    public void agregarSesion(SesionUsuario sesionUsuario, MongoCollection<Document> collectionUsuarios){
        this.sesionesDeUsuario.add(sesionUsuario);
        this.categoriaUsuario = sesionUsuario.calcularNuevaCategoria();

        BasicDBObject objetoSesion = new BasicDBObject();
        objetoSesion.put("inicio", sesionUsuario.getTiempoInicio().getTime());
        objetoSesion.put("finalizado", sesionUsuario.getTiempoFinalizado().getTime());

        //Se actualiza la categoria y se guarda la nueva sesion
        UpdateResult updateQueryResult = collectionUsuarios.updateOne(Filters.eq("dni", this.documentoIdentidad),
                Updates.combine(Updates.set("categoria", this.categoriaUsuario), Updates.push("sesiones", objetoSesion)));

    }

    public void recuperarSesion(MongoCollection<Document> collectionUsuarios){
        //Completar para obtener la ultima sesion unicamente
        Document searchQuery = new Document();
        searchQuery.put("nombre", getNombre());
        FindIterable<Document> cursor = collectionUsuarios.find(searchQuery);
        MongoCursor<Document> cursorIterator = cursor.cursor();
        System.out.println("Arreglar obtener solo ultima sesion");
    }

}

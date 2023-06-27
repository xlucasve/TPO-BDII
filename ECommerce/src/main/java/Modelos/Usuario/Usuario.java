package Modelos.Usuario;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;

public class Usuario {
    private String nombre;
    private String direccion;
    private Integer documentoIdentidad;
    private CategoriaUsuario categoriaUsuario;
    private ArrayList<SesionUsuario> sesionesDeUsuario;

    public Usuario(String nombre, String direccion, Integer documentoIdentidad, MongoCollection<Document> collectionUsuarios) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.documentoIdentidad = documentoIdentidad;
        this.categoriaUsuario = CategoriaUsuario.LOW;
        this.sesionesDeUsuario = new ArrayList<>();
        SesionUsuario sesionUsuario = new SesionUsuario(new Date(), new Date());
        this.sesionesDeUsuario.add(sesionUsuario);
        agregarUsuarioAColeccion(collectionUsuarios);
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public Integer getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void agregarUsuarioAColeccion(MongoCollection<Document> collectionUsuarios){

        ArrayList<BasicDBObject> sesionesArray = new ArrayList<>();
        for (int i = 0; i < this.sesionesDeUsuario.size(); i++){
            BasicDBObject objetoSesion = new BasicDBObject();
            objetoSesion.put("inicio", this.sesionesDeUsuario.get(i).getTiempoInicio().getTime());
            objetoSesion.put("finalizado", this.sesionesDeUsuario.get(i).getTiempoFinalizado().getTime());
            sesionesArray.add(objetoSesion);
        }

        Document document = new Document();
        document.put("nombre", this.nombre);
        document.put("direccion", this.direccion);
        document.put("dni", this.documentoIdentidad);
        document.put("categoria", this.categoriaUsuario);
        document.put("sesiones", sesionesArray);
        collectionUsuarios.insertOne(document);
    }

    public void agregarSesionDeUsuario(MongoCollection<Document> collectionUsuarios){

    }

    public void agregarSesion(SesionUsuario sesionUsuario, MongoCollection<Document> collectionUsuarios){
        this.sesionesDeUsuario.add(sesionUsuario);
        this.categoriaUsuario = sesionUsuario.calcularNuevaCategoria();
        Document query = new Document();
        query.put("dni", this.documentoIdentidad);

        BasicDBObject objetoSesion = new BasicDBObject();
        objetoSesion.put("inicio", sesionUsuario.getTiempoInicio().getTime());
        objetoSesion.put("finalizado", sesionUsuario.getTiempoFinalizado().getTime());

        UpdateResult updateQueryResult = collectionUsuarios.updateOne(Filters.eq("dni", this.documentoIdentidad),
                Updates.combine(Updates.set("categoria", this.categoriaUsuario), Updates.push("sesiones", objetoSesion)));

        System.out.println(updateQueryResult);
    }
}

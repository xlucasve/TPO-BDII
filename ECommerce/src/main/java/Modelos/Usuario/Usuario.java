package Modelos.Usuario;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
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
}

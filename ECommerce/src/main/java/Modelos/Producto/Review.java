package Modelos.Producto;

public class Review {

    private String nombreUsuarioReview;
    private String comentario;
    private Double calificacion;
    private Integer likeCounter;

    public Review(String nombreUsuarioReview, String comentario, Double calificacion) {
        this.nombreUsuarioReview = nombreUsuarioReview;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.likeCounter = 0;
    }

    public String getNombreUsuarioReview() {
        return nombreUsuarioReview;
    }

    public String getComentario() {
        return comentario;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public Integer getLikeCounter() {
        return likeCounter;
    }

    public void like(){
        this.likeCounter++;
    }

    public void dislike(){
        this.likeCounter--;
    }
}

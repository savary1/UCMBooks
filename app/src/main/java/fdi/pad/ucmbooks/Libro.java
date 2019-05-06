package fdi.pad.ucmbooks;

import android.graphics.Bitmap;

public class Libro {

    private String titulo;
    private String idLibro;
    private String autor;
    private String idAutor;
    private String rating;
    private String imageURL;
    private Bitmap image;
    private Boolean imageLoaded;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(String idAutor) {
        this.idAutor = idAutor;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Boolean getImageLoaded() {
        return imageLoaded;
    }

    public void setImageLoaded(Boolean imageLoaded) {
        this.imageLoaded = imageLoaded;
    }
}

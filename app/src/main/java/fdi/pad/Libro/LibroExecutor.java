package fdi.pad.Libro;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Clase que gestiona todos los apartados de los libros. Esta clase es pública, por lo que todas las operaciones que se realicen sobre
 * ellos pasan por aquí.
 * Clase fachada.
 */
public class LibroExecutor {

    private LibroHandler lH;

    public LibroExecutor(Context context) {
        this.lH = new LibroHandler(context);
    }

    /**
     * Añade un libro a la lista de libros, sin imagen asociada a él
     * Después lo guarda
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @return Devuelve verdadero si el libro se ha añadido y guardado correctamente. Falso en caso contrario
     */
    public boolean addLibroSinImagen(Context context, String titulo, String idLibro, String autor, String idAutor, String rating) {
        this.lH.addLibroSinImagen(context, titulo, idLibro, autor, idAutor, rating);
        return saveAll();
    }

    /**
     * Añade un libro a la lista de libros con imagen asociada a él
     * Después lo guarda
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageURL URL de la imagen. Obtenida de la API
     * @return Devuelve verdadero si el libro se ha añadido y guardado correctamente. Falso en caso contrario
     */
    public boolean addLibro(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                         Bitmap image, String imageURL) {
        this.lH.addLibro(context, titulo, idLibro, autor, idAutor, rating, image, imageURL);
        return saveAll();
    }

    /**
     * Método para guardadr todos los libros
     * Es privado porque solo se le llama cuando se hace alguna modificación sobre los seguidos, es decir, cuando se añade un libro a
     * la lista porque se ha seguido o cuando se borra porque se ha dejado de seguir
     * @return Devuelve verdadero si se ha guardado correctamente. Falso en caso contrario
     */
    private boolean saveAll() {
        return this.lH.saveAll();
    }

    /**
     * Método para borrar un libro según su ID
     * Este método no devuelve nada porque borrar un libro de la lista es algo trivial (en teoría)
     * @param id Identificador único de cada libro
     */
    public void deleteLibro(String id) {
        this.lH.deleteLibro(id);
        saveAll();
    }

    /**
     * Método para borrar todos los libros de la lista
     * Este método no devuelve nada porque borrar un libro de la lista es algo trivial (en teoría)
     */
    public void deleteAllFromList() {
        this.lH.deleteAllFromList();
        saveAll();
    }

    /**
     * Método que borra el fichero de guardado completo
     * @return Devuelve verdadero si se ha borrado correctamente, falso en caso contrario
     */
    public boolean deleteAllFromFileSystem() {
        return this.lH.deleteAllFromFileSystem();
    }

    /**
     * Marca un libro como leído
     * @param id Identificador único del libro
     */
    public void libroLeido(String id) {
        this.lH.getLibro(getIndex(id)).libroLeido();
    }

    /**
     * Marca un libro como no leido
     * @param id Identificador único del libro
     */
    public void libroNoLeido(String id) {
        this.lH.getLibro(getIndex(id)).libroNoLeido();
    }

    /**
     * Establece un porcentaje leído del libro
     * @param id Identificador único del libro
     * @param value El porcentaje leído. Si es mayor que 100, se pone a 100
     */
    public void setPorcentajeLeido(String id, int value) { this.lH.getLibro(getIndex(id)).setPorcentajeLeido(value); }

    /**
     * Cuando se pulsa el botón de seguir libro, si ya estaba seguido se deja de seguir y si no, se sigue
     * Después guarda el libro en cualquier caso
     * @return Verdadero si se ha seguido el libro, falso si se ha dejado de seguir
     */
    public boolean buttonSeguir(String id) {
        //Se ha seguido. Trivial. El libro guardado siempre está seguido
        if (this.lH.getLibro(getIndex(id)).buttonSeguir()) {
            saveAll();
            return true;
        }
        //Se ha dejado de seguir
        else {
            this.lH.deleteLibro(id);
            saveAll();
            return false;
        }
    }

    /**
     * Llama al libro correspondiente según la ID para establecer una valoración del 0 al 10
     * @param id ID única del libro sobre el que se quiere realizar la valoración
     * @param userRating Nota que el usuario da a ese libro
     * @return Devuelve verdadero si la nota es válida, falso en caso contrario
     */
    public boolean stablishRating(String id, int userRating) { return this.lH.getLibro(getIndex(id)).stablishRating(userRating); }

    /**
     * Realiza una reseña sobre el libro correspondiente a la ID
     * @param id ID única del libro sobre el que se quiere realizar al reseña
     * @param userReview La reseña propiamente dicha. El control de la longitud del campo lo realiza la propia interfaz de Android
     */
    public void makeReview(String id, String userReview) { this.lH.getLibro(getIndex(id)).makeReview(userReview); }

    //TODO Poner aquí todos los getters de los libros no creo que sea muy correcto. Convendría buscar otra manera
    public Libro getLibro(int i) { return this.lH.getLibro(i); }

    public Context getContext(String id) { return this.lH.getLibro(getIndex(id)).getContext(); }

    public String getTitulo(String id) { return this.lH.getLibro(getIndex(id)).getTitulo(); }

    public String getIdLibro(String id) { return this.lH.getLibro(getIndex(id)).getIdLibro(); }

    public String getAutor(String id) { return this.lH.getLibro(getIndex(id)).getAutor(); }

    public String getIdAutor(String id) { return this.lH.getLibro(getIndex(id)).getIdAutor(); }

    public String getRating(String id) { return this.lH.getLibro(getIndex(id)).getRating(); }

    public Bitmap getImage(String id) { return this.lH.getLibro(getIndex(id)).getImage(); }

    public String getImageURL(String id) { return this.lH.getLibro(getIndex(id)).getImageURL(); }

    public ArrayList<Libro> getListaLibros() { return this.lH.getListaLibros(); }

    public String getId(int i) { return this.lH.getId(i); }

    public int getIndex(String id) { return this.lH.getIndex(id); }

    public ArrayList<String> getAllId() { return this.lH.getAllId(); }

    public boolean isLibroSeguido(String id) { return this.lH.isLibroSeguido(id); }

    public String getFechaSeguido(String id) { return this.lH.getFechaSeguido(id); }

    public boolean isLibroLeido(String id) { return this.lH.isLibroLeido(id); }

    public String getFechaLeido(String id) { return this.lH.getFechaLeido(id); }

    public ArrayList<Libro> getLibrosSeguidos() { return this.lH.getLibrosSeguidos(); }

    public ArrayList<Libro> getLibrosLeidos() { return this.lH.getLibrosLeidos(); }

}

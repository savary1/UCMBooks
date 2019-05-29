package fdi.pad.libro;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.webkit.JavascriptInterface;

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
     * Añade un libro a la lista de seguidos con imagen asociada a él y lo guarda en memoria
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
    public boolean addToSeguidos(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                            Bitmap image, String imageURL) {
        if(this.lH.addToSeguidos(context, titulo, idLibro, autor, idAutor, rating, image, imageURL))
        {
            return this.lH.saveAll();
        }
        return false;
    }

    public boolean  addToSeguidos(Libro l) {
        this.lH.addToSeguidos(l);
        return this.lH.saveAll();
    }

    /**
     * Añade un libro a la lista de leidos con imagen asociada a él y lo guarda en memoria
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
    public boolean addToLeidos(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                                 Bitmap image, String imageURL) {
        if(this.lH.addToLeidos(context, titulo, idLibro, autor, idAutor, rating, image, imageURL))
        {
            return this.lH.saveAll();
        }
        return false;
    }

    /**
     * Añade un libro a la lista de busqueda con imagen asociada a él
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
    public boolean addToBusqueda(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                               Bitmap image, String imageURL) {
        return this.lH.addToBusqueda(context, titulo, idLibro, autor, idAutor, rating, image, imageURL);
    }

    /**
     * Método para borrar un libro de seguidos según su ID
     * También s eelimina de la memoria
     * Este método no devuelve nada porque borrar un libro de la lista es algo trivial (en teoría)
     * @param id Identificador único de cada libro
     */
    public void deleteFromSeguidos(String id) {
        this.lH.deleteFromSeguidos(id);
        this.lH.saveAll();
    }

    /**
     * Método para borrar un libro de leidos según su ID
     * También s eelimina de la memoria
     * Este método no devuelve nada porque borrar un libro de la lista es algo trivial (en teoría)
     * @param id Identificador único de cada libro
     */
    public void deleteFromLeidos(String id) {
        this.lH.deleteFromLeidos(id);
        this.lH.saveAll();
    }

    /**
     * Elimina todos los libros de la lista de búsqueda
     */
    public void deleteAllFromBusqueda(){
        this.lH.deleteAllFromBusqueda();
    }

    /**
     * Método que borra el fichero de guardado completo y vacia las listas
     * @return Devuelve verdadero si se ha borrado correctamente, falso en caso contrario
     */
    public boolean deleteAllFromFileSystem() {
        return this.lH.deleteAllFromFileSystem();
    }

    public boolean loadList() throws ClassNotFoundException { return this.lH.loadLists(); }

    /**
     * Devuelve una copia de la lista de libros sguidos
     * @return  lista seguidos
     */
    public ArrayList<Libro> getSeguidos(){
        return this.lH.getLibrosSeguidos();
    }

    /**
     * Devuelve el tamaño de la lista seguidos
     * @return  tamaño de la lista
     */
    public int getSeguidosSize(){
        return this.lH.getSeguidosSize();
    }

    /**
     * Devuelve una copia de la lista de libros leidos
     * @return  lista seguidos
     */
    public ArrayList<Libro> getLeidos(){
        return this.lH.getLibrosLeidos();
    }

    /**
     * Devuelve el tamaño de la lista leiods
     * @return  tamaño de la lista
     */
    public int getLeidosSize(){
        return this.lH.getLeidosSize();
    }

    /**
     * Devuelve una copia de la lista de busqueda
     * @return  lista busqueda
     */
    public ArrayList<Libro> getBusqueda(){
        return this.lH.getLibrosBusqueda();
    }

    /**
     * Devuelve el tamaño de la lista busqueda
     * @return  tamaño de la lista
     */
    public int getBusquedaSize(){
        return this.lH.getBusquedaSize();
    }

    /**
     * Marca un libro de la lista seguidos como leído, mo mete en la lsita seguidos y actualiaz to-do en memoria
     * @param id Identificador único del libro
     */
    public void libroLeido(String id) {
        Libro l = this.lH.deleteFromSeguidos(id);
        l.libroLeido();
        this.lH.addToLeidos(l);
        this.lH.saveAll();
    }

    /**
     * Marca un libro como no leido
     * @param id Identificador único del libro
     */
    public void libroNoLeido(String id) {
        Libro l = this.lH.deleteFromLeidos(id);
        l.libroNoLeido();
        this.lH.addToSeguidos(l);
        this.lH.saveAll();
    }

    /**
     * Establece un porcentaje leído del libro
     * @param id Identificador único del libro
     * @param value El porcentaje leído. Si es mayor que 100, se pone a 100
     */
    public void setPorcentajeLeido(String id, int value) {
        Libro l = this.lH.getFromLeidos(id);
        if(l != null){
            l.setPorcentajeLeido(value);
            this.lH.actualizaFromLeidos(l);
        }
    }

    /**
     * Devuelve la copia de un libro de la lista seguidos
     * @param i indice en la lista
     * @return  copia del libro
     */
    public Libro getFromSeguidos(int i){
        return this.lH.getFromSeguidos(i);
    }

    /**
     * Devuelve la copia de un libro de la lista leidos
     * @param i indice en la lista
     * @return  copia del libro
     */
    public Libro getFromLeidos(int i){
        return this.lH.getFromLeidos(i);
    }


    /**
     * Devuelve la copia de un libro de la lista busqueda
     * @param i indice en la lista
     * @return  copia del libro
     */
    public Libro getFromBusqueda(int i){
        return this.lH.getFromBusqueda(i);
    }

    /**
     * Cuando se pulsa el botón de seguir libro, si ya estaba seguido se deja de seguir y si no, se sigue
     * Después guarda el libro en cualquier caso
     * @return Verdadero si se ha seguido el libro, falso si se ha dejado de seguir
     */
    /*
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
    */

    /**
     * Llama al libro correspondiente según la ID para establecer una valoración del 0 al 10
     * @param id ID única del libro sobre el que se quiere realizar la valoración
     * @param userRating Nota que el usuario da a ese libro
     * @return Devuelve verdadero si la nota es válida, falso en caso contrario
     */
    /*
    public boolean stablishRating(String id, int userRating) { return this.lH.getLibro(getIndex(id)).stablishRating(userRating); }
    */

    /**
     * Realiza una reseña sobre el libro correspondiente a la ID
     * @param id ID única del libro sobre el que se quiere realizar al reseña
     * @param userReview La reseña propiamente dicha. El control de la longitud del campo lo realiza la propia interfaz de Android
     */
    /*
    public void makeReview(String id, String userReview) { this.lH.getLibro(getIndex(id)).makeReview(userReview); }
    */
    /*

    //TODO Poner aquí todos los getters de los libros no creo que sea muy correcto. Convendría buscar otra manera
    public Libro getLibro(int i) { return this.lH.getLibro(i); }

    public Context getContext(String id) { return this.lH.getLibro(getIndex(id)).getContext(); }

    @JavascriptInterface
    public String getTitulo(String id) { return this.lH.getLibro(getIndex(id)).getTitulo(); }

    public String getIdLibro(String id) { return this.lH.getLibro(getIndex(id)).getIdLibro(); }

    @JavascriptInterface
    public String getAutor(String id) { return this.lH.getLibro(getIndex(id)).getAutor(); }

    public String getIdAutor(String id) { return this.lH.getLibro(getIndex(id)).getIdAutor(); }

    @JavascriptInterface
    public String getRating(String id) { return this.lH.getLibro(getIndex(id)).getRating(); }

    @JavascriptInterface
    public Bitmap getImage(String id) { return this.lH.getLibro(getIndex(id)).getImage(); }

    public String getImageURL(String id) { return this.lH.getLibro(getIndex(id)).getImageURL(); }

    public ArrayList<Libro> getListaLibros() { return this.lH.getListaLibros(); }

    public String getId(int i) { return this.lH.getId(i); }

    public int getIndex(String id) { return this.lH.getIndex(id); }

    public ArrayList<String> getAllId() { return this.lH.getAllId(); }

    public boolean isLibroSeguido(String id) { return this.lH.isLibroSeguido(id); }

    @JavascriptInterface
    public String getFechaSeguido(String id) { return this.lH.getFechaSeguido(id); }

    public boolean isLibroLeido(String id) { return this.lH.isLibroLeido(id); }

    @JavascriptInterface
    public String getFechaLeido(String id) { return this.lH.getFechaLeido(id); }

    public ArrayList<Libro> getLibrosSeguidos() { return this.lH.getLibrosSeguidos(); }

    public ArrayList<Libro> getLibrosLeidos() { return this.lH.getLibrosLeidos(); }
    */

}

package fdi.pad.libro;

import android.content.Context;
import android.graphics.Bitmap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Esta clase se encarga de guardar las características de cada libro para que mostrarlas más tarde sea algo sencillo.
 * Además, permite procesar las imágenes y los datos del libro para poder modificarlos.
 * Esta clase se guarda en el dispositivo si el atributo "seguido" se encuentra a True
 * Por motivos de seguridad, la clase solo es accesible desde el propio paquete donde se encuentra
 */
public class Libro implements java.io.Serializable {

    /** Título del libro */
    private String titulo;
    /** ID del libro. Valor único */
    private String idLibro;
    /** Autor del libro */
    private String autor;
    /** ID del autor. Valor único */
    private String idAutor;
    /** URL de la imagen. Obtenido de la API */
    private String imageURL;
    /** Imagen en formato Bitmap */
    private Bitmap image;
    /** Booleano que indica si la imagen ha sido cargada (True) o no (False) */
    private Boolean imageLoaded;
    /** Puntuación del libro */
    private String rating;

    /** True si el libro se ha leído, False en caso contrario */
    private boolean leido;
    /** Fecha en la que se marcó el libro como leído. La hora es indiferente */
    private String fechaLeido;
    /** Entero de 0 a 100 que refleja el porcentaje del libro que ha sido leído */
    private int porcentajeLeido; //Se podría usar en el futuro
    /** Si está a True, "this" se guarda en el dispositivo. Si False, no se hace nada */
    private boolean seguido;
    /** Fecha en la que se dio a seguir al libro. La hora es indiferente */
    private String fechaSeguido;
    /** Review que el usuario hace del libro */
    private String userReview;
    /** Nota personal que el usuario ha dado al libro. Va de 1 a 10 */
    private Integer userRating;



    /**
     * Constructora general sin imagen asociada al libro.
     * No se indica si el libro se ha leído porque al agregarlo nunca se encuentra leído.
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     */
    public Libro(String titulo, String idLibro, String autor, String idAutor, String rating) {
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.autor = autor;
        this.idAutor = idAutor;
        this.rating = rating;
        this.imageURL = "";
        this.image = null;
        this.imageLoaded = false;
        init();
    }

    /**
     * Constructora general con imagen asociada al libro.
     * No se indica si el libro se ha leído porque al agregarlo nunca se encuentra leído.
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    Libro(String titulo, String idLibro, String autor, String idAutor, String rating,
          Bitmap image, String imageURL) {
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.autor = autor;
        this.idAutor = idAutor;
        this.rating = rating;
        this.image = image;
        this.imageURL = imageURL;
        this.imageLoaded = true;
        init();
    }

    Libro (Libro l){
        this.titulo = l.getTitulo();
        this.idLibro = l.getIdLibro();
        this.autor = l.getAutor();
        this.idAutor = l.getIdAutor();
        this.imageURL = l.getImageURL();
        this.image = l.getImage();
        this.imageLoaded = l.getImageLoaded();
        this.rating = l.getRating();
        this.leido = l.isLeido();
        this.fechaLeido = l.getFechaLeido();
        this.porcentajeLeido = l.getPorcentajeLeido();
        this.seguido = l.isSeguido();
        this.fechaSeguido = l.getFechaSeguido();
        this.userReview = l.getUserReview();
        this.userRating = l.getUserRating();
    }

    /**
     * Función auxiliar
     */
    private void init() {
        this.leido = false;
        this.fechaSeguido = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        this.porcentajeLeido = 0;
        this.seguido = false;
        this.userRating = null;
        this.userReview = "";
    }

    /**
     * Si un libro se ha leído, se pone a true el atributo y a 100 el porcentaje leído
     */
    public void libroLeido() {
        if(this.seguido) {
            this.leido = true;
            this.porcentajeLeido = 100;
            this.fechaLeido = "Leido: " + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }
    }

    /**
     * Si un libro se ha leído, se pone a true el atributo y a 100 el porcentaje leído. La fecha se actualiza según el parámetro
     * @param fechaLeido Fecha en la que el libro fue leído
     */
    void libroLeido(String fechaLeido) {
        if(this.seguido) {
            this.leido = true;
            this.porcentajeLeido = 100;
            this.fechaLeido = "Leido: " + fechaLeido;
        }
    }

    /**
     * Si un libro no se ha leído o se pone a false el atributo y a 0 el porcentaje leído
     */
    void libroNoLeido() {
        if(this.seguido) {
            this.leido = false;
            this.porcentajeLeido = 0;
            this.fechaLeido = "Sin leer (por ahora) ;-)";
        }
    }

    /**
     * Establece un porcentaje leído del libro. Si es 100 o menor que 0, se llama a los métodos que cambian el valor de leído
     * @param value El porcentaje leído. Si es mayor que 100, se pone a 100
     */
    void setPorcentajeLeido(int value) {
        this.porcentajeLeido = value;
        if(this.porcentajeLeido >= 100) {
            this.porcentajeLeido = 100;
            libroLeido();
        }
        else if(this.porcentajeLeido < 0) {
            libroNoLeido();
        }
        else {
            libroNoLeido();
            this.porcentajeLeido = value;
        }
    }

    /**
     * Cuando se pulsa el botón de seguir libro, si ya estaba seguido se deja de seguir y si no, se sigue
     */
    public void  buttonSeguir() {
        seguirLibro();
    }

    /**
     * Método para seguir un libro.
     * Primero se guarda en la memoria interna, si es que se puede, y después se actualiza el valor de seguido
     * @return Verdadero si el libro se ha guardado y seguido correctamente, falso en caso contrario
     */
    private boolean seguirLibro() {
        if(!this.seguido) {
            /*if (!saveBook())
                return false; //Si no se puede guardar la imagen, se devuelve falso*/
            this.seguido = true; //Se pone seguido al final porque si no se realiza el guardado correctamente, no cuenta como seguido
            return true; //se devuelve true si se ha guardado y se ha seguido correctamente
        }
        else return false; //Si el libro ya había sido seguido, no se puede volver a seguir
    }

    /**
     * Método para dejar de seguir un libro.
     * Primero se borra de la memoria interna, si es que no hay ningún fallo, y después se actualiza el valor de seguido
     * @return Verdadero si el libro se ha borrado y dejado de seguir correctamente, falso en caso contrario
     */
    public boolean dejarDeSeguirLibro() {
        if(this.seguido) {
            /*if(!deleteBook())
                return false; //Si no se ha borrado el libro, devuelve falso*/
            this.seguido = false; //A pesar de que se borre de la memoria interna, el valor se actualiza para mostrar "this" si es necesario
            return true; //Se devuelve true si se ha borrado y se ha dejado de seguir correctamente
        }
        else return false; //Si el libro no estaba seguido, no se puede quitar de seguido
    }

    boolean stablishRating(Integer userRating) {
        if(userRating == null || userRating < 0 || userRating > 10)
            return false;
        this.userRating = userRating;
        return true;
    }

    void makeReview(String userReview) {
        this.userReview = userReview;
    }

    void setFechaSeguido(String fechaSeguido) {
        this.fechaSeguido = fechaSeguido;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isLeido() {
        return leido;
    }

    private int getPorcentajeLeido() {
        return porcentajeLeido;
    }

    public boolean isSeguido() {
        return seguido;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public String getIdAutor() {
        return idAutor;
    }

    public String getRating() {
        return rating;
    }

    String getImageURL() {
        return imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    private Boolean getImageLoaded() {
        return imageLoaded;
    }

    Integer getUserRating() {
        return userRating;
    }

    String getUserReview() {
        return userReview;
    }

    String getFechaLeido() {
        return fechaLeido;
    }

    String getFechaSeguido() {
        return fechaSeguido;
    }

}

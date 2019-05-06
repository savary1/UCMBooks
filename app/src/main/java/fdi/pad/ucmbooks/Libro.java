package fdi.pad.ucmbooks;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Esta clase se encarga de guardar las características de cada libro para que mostrarlas más tarde sea algo sencillo.
 * Además, permite procesar las imágenes y los datos del libro para poder modificarlos.
 * Esta clase se guarda en el dispositivo si el atributo "seguido" se encuentra a True
 */
public class Libro {

    /** Título del libro */
    private String titulo;
    /** ID del libro. Valor único */
    private String idLibro;
    /** Autor del libro */
    private String autor;
    /** ID del autor. Valor único */
    private String idAutor;
    /** Imagen asociada al libro */
    private Drawable imagen;
    /** Nombre de la imagen asociada al libro. Si no hay ningún nombre, será "Default.jpg" */
    private String imageName;
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
    /** Entero de 0 a 100 que refleja el porcentaje del libro que ha sido leído */
    private int porcentajeLeido; //Se podría usar en el futuro
    /** True si el libro se ha seguido tras haberlo consultado en la api. Se pone a False cuando se consulta pero no se agrega.
     * Si está a True, "this" se guarda en el dispositivo. Si False, no se hace nada */
    private boolean seguido;

    /*
    ****************************************************************************************************************
    FALTA POR HACER TODO LO RELATIVO A LAS IMAGENES. MODIFICAR LAS CONSTRUCTORAS SEGÚN CONVENGA. LA PÁGINA DE DEBAJO
    ES UNA BUENA GUÍA
    https://androidstudiofaqs.com/tutoriales/guardar-una-imagen-android-studio
    ADEMÁS, REVISAR SI EL NOMBRE DE LA IMAGEN VIENE CON ".JPG" O HAY QUE AÑADIRLO MANUALMENTE
    ****************************************************************************************************************
    */

    /**
     * Constructora general sin imagen asociada al libro.
     * No se indica si el libro se ha leído porque al agregarlo nunca se encuentra leído.
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     */
    public Libro(String titulo, String idLibro, String autor, String idAutor) {
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.autor = autor;
        this.idAutor = idAutor;
        this.imagen = null;
        this.imageURL = "";
        this.imageName = "Default.jpg";
        this.image = null;
        this.imageLoaded = false;
        this.leido = false;
        this.porcentajeLeido = 0;
        this.seguido = false;
    }

    /**
     * Constructora general con imagen asociada al libro.
     * No se indica si el libro se ha leído porque al agregarlo nunca se encuentra leído.
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param imagen Imagen asociada al libro
     * @param imageName Nombre de la imagen del libro. Interpretamos que cada imagen tiene un nombre único
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    public Libro(String titulo, String idLibro, String autor, String idAutor, Drawable imagen, String imageName, String imageURL) {
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.autor = autor;
        this.idAutor = idAutor;
        this.imagen = imagen;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.image = null;
        this.imageLoaded = true;
        this.leido = false;
        this.porcentajeLeido = 0;
        this.seguido = false;
    }

    /**
     * Si un libro se ha leído, se pone a true el atributo y a 100 el porcentaje leído
     */
    public void libroLeido() {
        this.leido = true;
        this.porcentajeLeido = 100;
    }

    /**
     * Si un libro no se ha leído o se pone a false el booleano "leido", establece el valor del porcentaje a 0
     */
    public void libroNoLeido() {
        this.leido = false;
        this.porcentajeLeido = 0;
    }

    public void setPorcentajeLeido(int value) {
        this.porcentajeLeido = value;
        if(this.porcentajeLeido >= 100) {
            this.porcentajeLeido = 100;
            libroLeido();
        }
    }

    public boolean seguirLibro() {
        if(!this.seguido) {
            this.seguido = true;
            /*
            **********************************************************************************************
            Hay que Guardar el libro
            **********************************************************************************************
            */
            return true; //se devuelve true si se ha guardado correctamente
        }
        else return false; //Si el libro ya había sido seguido, no se puede volver a seguir
    }

    public boolean dejarDeSeguirLibro() {
        if(this.seguido) {
            this.seguido = false;
            /*
            **********************************************************************************************
            Hay que borrar el libro guardado
            **********************************************************************************************
            */
            return true; //Se devuelve true si se ha borrado correctamente
        }
        else return false; //Si el libro no estaba seguido, no se puede quitar de seguido
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public boolean isLeido() {
        return leido;
    }

    public int getPorcentajeLeido() {
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

    public String getImageURL() {
        return imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public Boolean getImageLoaded() {
        return imageLoaded;
    }
}

package fdi.pad.ucmbooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
    /** Nombre de la carpeta donde se va a guardar la imagen. No será la misma que la de esta clase (para no llenarla) */
    private String imageFolder;
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
    /** Contexto */
    private Context context;

    /*
    ****************************************************************************************************************
    TODO Las pruebas necesarias para comprobar que todo funcione correctamente
    TODO El método de update
    TODO Borrado de comentarios si las imágenes se guardan bien con el sistema actual
    ****************************************************************************************************************
    */

    /**
     * Constructora general sin imagen asociada al libro.
     * No se indica si el libro se ha leído porque al agregarlo nunca se encuentra leído.
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     */
    public Libro(Context context, String titulo, String idLibro, String autor, String idAutor, String rating) {
        this.context = context;
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.autor = autor;
        this.idAutor = idAutor;
        this.rating = rating;
        this.imagen = null;
        this.imageURL = "";
        this.imageName = "Default.jpg";
        this.image = null;
        this.imageLoaded = false;
        init();
    }

    /**
     * Constructora general con imagen asociada al libro.
     * No se indica si el libro se ha leído porque al agregarlo nunca se encuentra leído.
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageName Nombre de la imagen del libro. Interpretamos que cada imagen tiene un nombre único
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    public Libro(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                 Bitmap image/*, String imageName*/, String imageURL) {
        this.context = context;
        this.titulo = titulo;
        this.idLibro = idLibro;
        this.autor = autor;
        this.idAutor = idAutor;
        this.rating = rating;
        //this.imagen = imagen;
        this.image = image;
        //this.imageName = imageName + ".jpg";
        this.imageURL = imageURL;
        this.image = null;
        this.imageLoaded = true;
        init();
    }

    private void init() {
        this.leido = false;
        this.porcentajeLeido = 0;
        this.seguido = false;
        this.imageFolder = "/Bookimages";
    }

    /**
     * Si un libro se ha leído, se pone a true el atributo y a 100 el porcentaje leído
     */
    public void libroLeido() {
        this.leido = true;
        this.porcentajeLeido = 100;
    }

    /**
     * Si un libro no se ha leído o se pone a false el atributo y a 0 el porcentaje leído
     */
    public void libroNoLeido() {
        this.leido = false;
        this.porcentajeLeido = 0;
    }

    /**
     * Establece un porcentaje leído del libro. Si es 100 o menor que 0, se llama a los métodos que cambian el valor de leído
     * @param value El porcentaje leído. Si es mayor que 100, se pone a 100
     */
    public void setPorcentajeLeido(int value) {
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
     * @return Verdadero si se ha seguido el libro, falso en caso contrario
     */
    public boolean buttonSeguir() {
        return !this.seguido ? seguirLibro() : dejarDeSeguirLibro();
    }

    /**
     * Método para seguir un libro.
     * Primero se guarda en la memoria interna, si es que se puede, y después se actualiza el valor de seguido
     * @return Verdadero si el libro se ha guardado y seguido correctamente, falso en caso contrario
     */
    private boolean seguirLibro() {
        if(!this.seguido) {
            if (!saveBook())
                return false; //Si no se puede guardar la imagen, se devuelve falso
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
    private boolean dejarDeSeguirLibro() {
        if(this.seguido) {
            if(!deleteBook())
                return false; //Si no se ha borrado el libro, devuelve falso
            this.seguido = false; //A pear de que se borre de la memoria interna, el valor se actualiza para mostrar "this" si es necesario
            return true; //Se devuelve true si se ha borrado y se ha dejado de seguir correctamente
        }
        else return false; //Si el libro no estaba seguido, no se puede quitar de seguido
    }

    /**
     * Método que se llama cuando se deben actualizar los valores de un libro seguido. Llama al método para dejar
     * de seguirlo y así borrarlo de la memoria. Después
     * NO FUNCIONA ASÍ, TENGO LA IMPRESION
     */
    /* ******************************************************************************************
    POR HACER esto
     */
    /*public void updateContent() {
        if(!this.seguido)
            this.seguido = true;
        dejarDeSeguirLibro();
    }*/

    /*private boolean saveImage() {
        //Creamos la carpeta donde vamos a guardar la imagen
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + imageFolder);

        //Comprobamos que exista y que si no, la cree
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //Creamos el archivo sobre el que vamos a guardar la imagen
        File file = new File(dir, imageName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            this.image.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);

            return true;
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            falloGuardado();
        }
        catch(IOException e) {
            e.printStackTrace();
            falloGuardado();
        }

        return false;
    }

    /**
     * Comprueba que el archivo se ha creado
     * @param file El archivo a comprobar
     */
    /*private void checkFileCreation(File file){
        MediaScannerConnection.scanFile(context,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }*/

    /**
     * Guarda el libro en la memoria interna del teléfono
     * @return Verdadero si se ha guardado correctamente. Falso en caso contrario
     */
    private boolean saveBook() {
        try {
            FileOutputStream fos = context.openFileOutput(this.idLibro, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            /*String s = this.titulo + "\n" + this.idLibro + "\n" + this.autor + "\n" + this.idAutor + "\n" +
                    this.imageName + "\n" + this.imageURL + "\n" + this.rating + "\n" + this.leido + "\n" +
                    this.porcentajeLeido;*/
            //fos.write(s.getBytes());
            oos.writeObject(this.titulo);
            oos.writeObject(this.idLibro);
            oos.writeObject(this.autor);
            oos.writeObject(this.idAutor);
            oos.writeObject(this.imageName);
            oos.writeObject(this.imageURL);
            oos.writeObject(this.image);
            oos.writeObject(this.rating);
            oos.writeObject(this.leido);
            oos.close();
            fos.close();
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            falloGuardado();
        }
        catch (IOException e) {
            e.printStackTrace();
            falloGuardado();
        }

        return false;
    }

    /**
     * Borra el libro de la memoria interna del teléfono
     * @return Verdadero si se ha borrado correctamente. Falso en caso contrario
     */
    private boolean deleteBook() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + this.idLibro);

        if(file.delete()) {
            exitoBorrado();
            return true;
        }

        falloBorrado();
        return false;
    }

    /**
     * Mensaje de éxito si se ha guardado correctamente
     */
    private void exitoGuardado() {
        Toast.makeText(this.context, "El libro se ha seguido correctamente", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de fallo si se ha guardado correctamente
     */
    private void falloGuardado() {
        Toast.makeText(this.context, "No se ha podido seguir el libro. Comprueba tu conectividad y la memoria disponible en tu dispositivo", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de éxito si se ha borrado correctamente
     */
    private void exitoBorrado() {
        Toast.makeText(this.context, "Ya no sigues a este libro", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje de fallo si se ha borrado correctamente
     */
    private void falloBorrado() {
        Toast.makeText(this.context, "Error al dejar de seguir el libro. Prueba más tarde", Toast.LENGTH_SHORT).show();
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

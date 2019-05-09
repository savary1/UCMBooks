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
 * Clase que gestiona todos los apartados de la lista de libros, así como su guardado o borrado de la memoria interna del teléfono.
 * Por motivos de seguridad, la clase solo es accesible desde el propio paquete donde se encuentra
 */
class LibroHandler {

    private ArrayList<Libro> libros;
    private String fileName;
    private Context context;

    /**
     * COnstructora general. El único requisito de esta constructora es el contexto
     * @param context Contexto de la aplicación
     */
    LibroHandler(Context context) {
        this.libros = new ArrayList<>();
        this.context = context;
        this.fileName = "Libros";
    }

    /**
     * Añade un libro a la lista de libros sin imagen asociada a él
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     */
    void addLibroSinImagen(Context context, String titulo, String idLibro, String autor, String idAutor, String rating) {
        this.libros.add(new Libro(context, titulo, idLibro, autor, idAutor, rating));
    }

    /**
     * Añade un libro a la lista de libros con imagen asociada a él
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    void addLibro(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                         Bitmap image, String imageURL) {
        this.libros.add(new Libro(context, titulo, idLibro, autor, idAutor, rating, image, imageURL));
    }

    /**
     * Guarda los datos de un libro en la memoria interna del teléfono
     * MÉTODO EN DESUSO //TODO Borrado de este método si la nueva forma de guarado funciona
     * @param oos ObjectOutputStream
     * @param i índice del libro dentro de la lista de ellos
     * @return Ddevuelve verdadero si el libro se ha guardado correctamente, falso en caso contrario
     */
    boolean saveLibroIndividual(ObjectOutputStream oos, int i) {
        try {
            /*String s = this.titulo + "\n" + this.idLibro + "\n" + this.autor + "\n" + this.idAutor + "\n" +
                    this.imageName + "\n" + this.imageURL + "\n" + this.rating + "\n" + this.leido + "\n" +
                    this.porcentajeLeido;*/
            //fos.write(s.getBytes());
            //TODO Borrado de este comentario si se carga bien la información con el método de abajo
            oos.writeObject(this.libros.get(i).getTitulo());
            oos.writeObject(this.libros.get(i).getIdLibro());
            oos.writeObject(this.libros.get(i).getAutor());
            oos.writeObject(this.libros.get(i).getIdAutor());
            oos.writeObject(this.libros.get(i).getImageURL());
            oos.writeObject(this.libros.get(i).getImage());
            oos.writeObject(this.libros.get(i).getRating());
            oos.writeObject(this.libros.get(i).isLeido());
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Guarda toda la lista de libros en la memoria interna del teléfono
     * @return Devuelve verdadero si lo ha guardado correctamente, falso en caso contrario
     */
    boolean saveAll() {
        try {
            FileOutputStream fos = this.context.openFileOutput(this.fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this.libros);
            /*for(int i = 0; i < this.libros.size(); ++i) {
                saveLibroIndividual(oos, i);
            }*/ //TODO Comprobar si esto funciona

            oos.close();
            fos.close();
            //exitoGuardado(); TODO Toast de exitos y fallos
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            //falloGuardado();
        }
        catch (IOException e) {
            e.printStackTrace();
            //falloGuardado();
        }

        return false;
    }

    /**
     * Borra un libro concreto de la lista.
     * ¡Importante! No borra el libro de la memoria interna del teléfono
     * @param id la ID única del libro
     */
    void deleteLibro(String id) {
        this.libros.remove(getIndex(id));
    }

    /**
     * Borra toda la lista de libros, dejándola vacía
     * ¡Importante! No borra los libros de la memoria interna del teléfono
     */
    void deleteAllFromList() {
        this.libros.clear();
    }

    /**
     * Borra el archivo de guardado de la memoria interna del teléfono
     * @return Devuelve verdadero si el borrado se ha ejecutado correctamente, falso en caso contrario
     */
    boolean deleteAllFromFileSystem() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + this.fileName);

        if(file.delete()) {
            //exitoBorrado();
            return true;
        }

        //falloBorrado();
        return false;
    }

    Libro getLibro(int i) {
        return this.libros.get(i);
    }

    ArrayList<Libro> getListaLibros() {
        return this.libros;
    }

    String getId(int i) {
        return this.libros.get(i).getIdLibro();
    }

    int getSize() {
        return this.libros.size();
    }

    int getIndex(String id) {
        for(int i = 0; i < this.libros.size(); ++i)
            if (this.libros.get(i).getIdLibro().equals(id)) //TODO Comprobar que "equals" funciona
                return i;
        return -1;
    }

    ArrayList<String> getAllId() {
        ArrayList<String> a = new ArrayList<>();
        for(int i = 0; i < this.libros.size(); ++i)
            a.add(this.libros.get(i).getIdLibro());
        return a;
    }

    boolean isLibroSeguido(String id) {
        return getIndex(id) != -1;
    }

    boolean isLibroLeido(String id) {
        return getLibro(getIndex(id)).isLeido();
    }

}

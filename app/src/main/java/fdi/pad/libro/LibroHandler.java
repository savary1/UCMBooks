package fdi.pad.libro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que gestiona todos los apartados de la lista de libros, así como su guardado o borrado de la memoria interna del teléfono.
 * Por motivos de seguridad, la clase solo es accesible desde el propio paquete donde se encuentra.
 * Esta clase no se usa para realizar operaciones sobre ella. Por eso, es conveniente usar los "getters" de la forma adecuada
 */
class LibroHandler implements Serializable {

    private ArrayList<Libro> libros;
    private String fileName;
    private Context context;

    private String exitoGuardado, falloGuardado, exitoBorrado, falloBorrado;

    /**
     * COnstructora general. El único requisito de esta constructora es el contexto
     * @param context Contexto de la aplicación
     */
    LibroHandler(Context context) {
        this.libros = new ArrayList<>();
        this.context = context;
        this.fileName = "Libros";

        this.exitoGuardado = "Libro seguido correctamente";
        this.falloGuardado = "Fallo al seguir el libro";
        this.exitoBorrado = "Ya no se sigue a este libro";
        this.falloBorrado = "No se pudo dejar de seguir el libro";
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
     * Guarda los datos de un libro en la memoria interna del teléfono //TODO Borrado de este método si la nueva forma de guarado funciona
     * @param oos ObjectOutputStream
     * @param i índice del libro dentro de la lista de ellos
     * @return Ddevuelve verdadero si el libro se ha guardado correctamente, falso en caso contrario
     */
    boolean saveLibroIndividual(ObjectOutputStream oos, int i) {
        try {
            oos.writeObject(this.libros.get(i).getTitulo());
            oos.writeObject(this.libros.get(i).getIdLibro());
            oos.writeObject(this.libros.get(i).getAutor());
            oos.writeObject(this.libros.get(i).getIdAutor());
            oos.writeObject(this.libros.get(i).getImageURL());

            if(this.libros.get(i).getImage() != null) {
                //Este pedazo de código de aquí es para guardar las imágenes. No se puede hacer directamente
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                this.libros.get(i).getImage().compress(Bitmap.CompressFormat.PNG, 0, byteStream);
                byte bitmapBytes[] = byteStream.toByteArray();
                oos.write(bitmapBytes, 0, bitmapBytes.length);
            }
            else
                oos.writeObject(null);

            oos.writeObject(this.libros.get(i).getRating());
            oos.writeObject(this.libros.get(i).getFechaSeguido());
            oos.writeObject(this.libros.get(i).isLeido());
            oos.writeObject(this.libros.get(i).getFechaLeido());
            oos.writeObject(this.libros.get(i).getUserReview());
            oos.writeObject(this.libros.get(i).getUserRating());

            //TODO poner todo el código de abajo en la sección de cargado, donde corresponda. Es para la imagen
            //la variable "in" es un ObjectInputStream. No hay que olvidarse de hacer el FileOutputStream
            /*ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int b;
            while((b = in.read()) != -1)
                byteStream.write(b);
            byte bitmapBytes[] = byteStream.toByteArray();
            this.image = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);*/

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

            for(int i = 0; i < this.libros.size(); ++i) {
                saveLibroIndividual(oos, i);
            }

            oos.close();
            fos.close();
            printToast(this.exitoGuardado);
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            printToast(this.falloGuardado);
        }
        catch (IOException e) {
            e.printStackTrace();
            printToast(this.falloGuardado);
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
        File file = new File(this.context.getFilesDir().getAbsolutePath() + "/");
        String[] children = file.list();

        if(children.length == 1) {
            if (new File(file, children[0]).delete()) {
                this.printToast(this.exitoBorrado);
                return true;
            }
        }
        //Nunca debería entrar aquí
        else if(children.length > 1) {
            for (int i = 0; i < children.length; i++) {
                new File(file, children[i]).delete();
            }
        }

        this.printToast(this.falloBorrado);
        return false;
    }

    //TODO Hacer esto, aunque pensandolo puede que no sea necesario porque la creación de la clase debería venir de fuera, así como la carga de las cosas
    void loadList() {

    }

    /**
     * Mensaje de éxito si se ha guardado correctamente
     */
    private void printToast(final String s) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast t = Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_LONG);
                t.show();
            }
        });
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
            if (this.libros.get(i).getIdLibro().equals(id))
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

    String getFechaSeguido(String id) {return getLibro(getIndex(id)).getFechaSeguido(); }

    String getFechaLeido(String id) { return getLibro(getIndex(id)).getFechaLeido(); }

    boolean isLibroLeido(String id) {
        return getLibro(getIndex(id)).isLeido();
    }

    ArrayList<Libro> getLibrosSeguidos() {
        //Método trivial
        ArrayList<Libro> a = new ArrayList<>();
        for(int i = 0; i < this.libros.size(); ++i)
            if(this.libros.get(i).isSeguido())
                a.add(this.libros.get(i));
        return a;
    }

    ArrayList<Libro> getLibrosLeidos() {
        ArrayList<Libro> a = new ArrayList<>();
        for(int i = 0; i < this.libros.size(); ++i)
            if(this.libros.get(i).isLeido())
                a.add(this.libros.get(i));
        return a;
    }



}

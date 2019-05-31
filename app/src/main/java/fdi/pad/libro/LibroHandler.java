package fdi.pad.libro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Clase que gestiona todos los apartados de la lista de libros, así como su guardado o borrado de la memoria interna del teléfono.
 * Por motivos de seguridad, la clase solo es accesible desde el propio paquete donde se encuentra.
 * Esta clase no se usa para realizar operaciones sobre ella. Por eso, es conveniente usar los "getters" de la forma adecuada
 */
class LibroHandler implements java.io.Serializable {

    /**Listas que representan los libros que se muestran en cada pestaña de la aplicación*/
    private static ArrayList<Libro> seguidos;
    private static ArrayList<Libro> leidos;
    private static ArrayList<Libro> busqueda;
    /**Variables que indica si las listas han sido cambiadas desde la última vez que se leyeron*/
    private static boolean seguidosChange;
    private static boolean leidosChange;
    private static boolean busquedaChange;

    private String fileName;
    private Context context;

    private String exitoGuardado, falloGuardado, exitoBorrado, falloBorrado;

    /**
     * COnstructora general. El único requisito de esta constructora es el contexto
     * @param context Contexto de la aplicación
     */
    LibroHandler(Context context) {
        this.context = context;
        this.fileName = "Libros";

        this.exitoGuardado = "Libros guardados correctamente";
        this.falloGuardado = "Fallo al guardar los libros";
        this.exitoBorrado = "Ya no se sigue a este libro";
        this.falloBorrado = "No se pudo dejar de seguir el libro";
    }

    /**
     * Añade un libro a la lista de seguidos con imagen asociada a él
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    boolean addToSeguidos(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                          Bitmap image, String imageURL) {
        seguidos.add(new Libro(context, titulo, idLibro, autor, idAutor, rating, image, imageURL));
        seguidosChange = true;
        return seguidos.get(seguidos.size() - 1).getIdLibro().equals(idLibro);
    }

    void addToSeguidos(Libro l){
        seguidos.add(l);
        seguidosChange = true;
    }

    /**
     * Borra un libro concreto de seguidos.
     * ¡Importante! No borra el libro de la memoria interna del teléfono
     * @param id la ID única del libro
     */
    Libro deleteFromSeguidos(String id) {
        boolean encontrado = false;
        Libro l = null;
        for(int i = 0; i < seguidos.size() && !encontrado; i++){
            if(seguidos.get(i).getIdLibro().equals(id)){
                l = seguidos.get(i);
                seguidos.remove(i);
                encontrado = true;
            }
        }
        seguidosChange = true;
        return l;
    }

    /**
     * Borra toda la lista de seguidos, dejándola vacía
     * ¡Importante! No borra los libros de la memoria interna del teléfono
     */
    void deleteAllFromSeguidos() {
        seguidos = new ArrayList<>();
        seguidosChange = true;
    }

    /**
     * Añade un libro a la lista de leidos con imagen asociada a él
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    boolean addToLeidos(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                          Bitmap image, String imageURL) {
        leidos.add(new Libro(context, titulo, idLibro, autor, idAutor, rating, image, imageURL));
        leidosChange = true;
        return leidos.get(leidos.size() - 1).getIdLibro().equals(idLibro);
    }

    void addToLeidos(Libro l){
        leidos.add(l);
        leidosChange = true;
    }

    /**
     * Borra un libro concreto de leidos.
     * ¡Importante! No borra el libro de la memoria interna del teléfono
     * @param id la ID única del libro
     */
    Libro deleteFromLeidos(String id) {
        boolean encontrado = false;
        Libro l = null;
        for(int i = 0; i < leidos.size() && !encontrado; i++){
            if(leidos.get(i).getIdLibro().equals(id)){
                l = leidos.get(i);
                leidos.remove(i);
                encontrado = true;
            }
        }
        leidosChange = true;
        return l;
    }

    /**
     * Borra toda la lista de seguidos, dejándola vacía
     * ¡Importante! No borra los libros de la memoria interna del teléfono
     */
    void deleteAllFromLeidos() {
        leidos = new ArrayList<>();
        leidosChange = true;
    }

    /**
     * Añade un libro a la lista de busqueda con imagen asociada a él
     * @param context Contexto
     * @param titulo Título del libro
     * @param idLibro ID asociada al libro. Valor único
     * @param autor Autor del libro
     * @param idAutor ID asociada al autor. Valor único
     * @param rating Valoración del libro
     * @param image Imagen en formato Bitmap. Se pasa este parámetro para poder guardarla en el dispositivo
     * @param imageURL URL de la imagen. Obtenida de la API
     */
    boolean addToBusqueda(Context context, String titulo, String idLibro, String autor, String idAutor, String rating,
                        Bitmap image, String imageURL) {
        busqueda.add(new Libro(context, titulo, idLibro, autor, idAutor, rating, image, imageURL));
        busquedaChange = true;
        return busqueda.get(busqueda.size() - 1).getIdLibro().equals(idLibro);
    }

    /**
     * Sustituye la lista de busqueda
     * @param libros lista nueva
     */
    void setBusqueda(ArrayList<Libro> libros){
        busqueda = libros;
        busquedaChange = true;
    }

    /**
     * Borra toda la lista de seguidos, dejándola vacía
     * ¡Importante! No borra los libros de la memoria interna del teléfono
     */
    void deleteAllFromBusqueda() {
        busqueda = new ArrayList<>();
        busquedaChange = true;
    }

    /**
     * Guarda los datos de un libro en la memoria interna del teléfono //TODO Borrado de este método si la nueva forma de guarado funciona
     * @param oos ObjectOutputStream
     * @param libro libro que hay que guardar
     * @return Ddevuelve verdadero si el libro se ha guardado correctamente, falso en caso contrario
     */
    private boolean saveLibroIndividual(ObjectOutputStream oos, Libro libro) {
        try {
            oos.writeObject(libro.getTitulo());
            oos.writeObject(libro.getIdLibro());
            oos.writeObject(libro.getAutor());
            oos.writeObject(libro.getIdAutor());
            oos.writeObject(libro.getImageURL());

            if(libro.getImage() != null) {
                //Este pedazo de código de aquí es para guardar las imágenes. No se puede hacer directamente
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                libro.getImage().compress(Bitmap.CompressFormat.PNG, 0, byteStream);
                byte[] bitmapBytes = byteStream.toByteArray();
                oos.write(bitmapBytes, 0, bitmapBytes.length);
            }
            else
                oos.writeObject(null);

            oos.writeObject(libro.getRating());
            oos.writeObject(libro.getFechaSeguido());
            oos.writeObject(libro.isLeido());
            oos.writeObject(libro.getFechaLeido());
            oos.writeObject(libro.getUserReview());
            oos.writeObject(libro.getUserRating());

            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Guarda toda la lista de libros en la memoria interna del teléfono. Solo guarda los seguidos
     * Guarda los datos de la siguiente forma:
     *  - Número de libros seguidos
     *  - Datos del libro n. Es un bucle a partir de aquí
     *      - Título del libro n
     *      - ID del libro n
     *      - Autor del libro n
     *      - ID del autor del libro n
     *      - URL de la imagen del libro n
     *      - Array de bits de la imagen del libro n
     *      - Puntuación del libro n
     *      - Fecha seguido del libro n
     *      - Booleano de si está leído el libro n
     *      - Fecha de leído del libro n
     *      - Review del libro n
     *      - Rating del usuario del libro n
     * @return Devuelve verdadero si lo ha guardado correctamente, falso en caso contrario
     */
    boolean saveAll() {
        try {
            FileOutputStream fos = this.context.openFileOutput(this.fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            //Necesitamos un índice para saber cuantos libros guardamos. Si es 0, no se guarda nada
            int numGuardados = seguidos.size() + leidos.size();
            oos.writeInt(numGuardados);

            for(int i = 0; i < seguidos.size(); ++i) {
                saveLibroIndividual(oos, seguidos.get(i));
            }

            for(int i = 0; i < leidos.size(); ++i) {
                saveLibroIndividual(oos, leidos.get(i));
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
     * Borra el archivo de guardado de la memoria interna del teléfono y vacia las listas
     * @return Devuelve verdadero si el borrado se ha ejecutado correctamente, falso en caso contrario
     */
    boolean deleteAllFromFileSystem() {
        File file = new File(this.context.getFilesDir().getAbsolutePath() + "/");
        String[] children = file.list();
        boolean result = true;

        if(children.length == 1) {
            if (new File(file, children[0]).delete()) {
                this.printToast(this.exitoBorrado);
                result = true;
            }
        }
        //Nunca debería entrar aquí
        else if(children.length > 1) {
            for (int i = 0; i < children.length; i++) {
                new File(file, children[i]).delete();
            }
            result = false;
            this.printToast(this.falloBorrado);
        }

        seguidos = new ArrayList<>();
        seguidosChange = true;
        leidos = new ArrayList<>();
        leidosChange = true;
        busqueda = new ArrayList<>();
        busquedaChange = true;

        return result;
    }

    /**
     * Carga los libros seguidos de la memoria interna a la lista
     * @return Devuelve verdadero si la carga se ha ejecutado correctamente, falso en caso contrario
     * @throws ClassNotFoundException
     */
    boolean loadLists() throws ClassNotFoundException {
        seguidos = new ArrayList<>();
        leidos = new ArrayList<>();
        busqueda = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("data/data/fdi.pad.ucmbooks/files/Libros");
            ObjectInputStream ois = new ObjectInputStream(fis);

            //Cogemos cuántos libros están guardados
            int n = ois.readInt();

            for(int i = 0; i < n; ++i) {
                //Cargamos las variables
                String titulo = (String)ois.readObject();
                String idLibro = (String)ois.readObject();
                String autor = (String)ois.readObject();
                String idAutor = (String)ois.readObject();
                String imageURL = (String)ois.readObject();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                int b;
                while((b = ois.read()) != -1)
                    byteStream.write(b);
                byte[] bitmapBytes = byteStream.toByteArray();
                Bitmap image = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                String rating = (String)ois.readObject();
                String fechaSeguido = (String)ois.readObject();
                boolean leido = (boolean)ois.readObject();
                String fechaLeido = (String)ois.readObject();
                String review = (String)ois.readObject();
                Integer userRating = (Integer)ois.readObject();

                //Añadimos el libro
                if(leido){
                    addToLeidos(this.context, titulo, idLibro, autor, idAutor, rating, image, imageURL);
                    leidos.get(leidos.size() - 1).buttonSeguir();
                    leidos.get(leidos.size() - 1).libroLeido(fechaLeido);
                    leidos.get(leidos.size() - 1).makeReview(review);
                    leidos.get(leidos.size() - 1).stablishRating(userRating);
                    leidos.get(leidos.size() - 1).setFechaSeguido(fechaSeguido);
                }
                else{
                    addToSeguidos(this.context, titulo, idLibro, autor, idAutor, rating, image, imageURL);
                    seguidos.get(seguidos.size() - 1).buttonSeguir();
                    seguidos.get(seguidos.size() - 1).setFechaSeguido(fechaSeguido);
                }

            }

            ois.close();
            fis.close();

            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        seguidosChange = true;
        leidosChange = true;
        busquedaChange = true;

        return false;
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

    /**
     * Devuelve una copia de la lista de libros seguidos
     * @return  libros seguidos
     */
    ArrayList<Libro> getLibrosSeguidos() {
        //Método trivial
        ArrayList<Libro> a = new ArrayList<>(seguidos);
        seguidosChange = false;
        return a;
    }

    /**
     * Devuelve el tamaño de la lista seguidos
     * @return  tamaño de la lista
     */
    int getSeguidosSize(){
        return seguidos.size();
    }

    /**
     * Devuelve una copia de la lista de libros leídos
     * @return libros leidos
     */
    ArrayList<Libro> getLibrosLeidos() {
        ArrayList<Libro> a = new ArrayList<>(leidos);
        leidosChange = false;
        return a;
    }

    /**
     * Devuelve el tamaño de la lista leidos
     * @return tamaño de la lista
     */
    int getLeidosSize(){
        return leidos.size();
    }

    /**
     * Devuelve una copia de la lista de la última búsqueda
     * @return  Ultima búsqueda
     */
    ArrayList<Libro> getLibrosBusqueda() {
        ArrayList<Libro> a = new ArrayList<>(busqueda);
        busquedaChange = false;
        return a;
    }

    /**
     * Devuelve el tamaño de la lista busqueda
     * @return  tamaño de la lista
     */
    int getBusquedaSize(){
        return busqueda.size();
    }

    /**
     * Si la id de l coincide con la id de algun libro de la lista, lo actualiza
     * @param l libro que se quiera actualizar
     * @return  True si se ha actualizado un libro, false si no hay ninguno con la id de l
     */
    boolean actualizaFromSeguidos(Libro l){
        for(int i = 0; i < seguidos.size(); i++){
            if(l.getIdLibro().equals(seguidos.get(i))){
                seguidos.remove(i);
                seguidos.add(i, l); //Se borra esa posición y se inserta l en esa posición
                return true;
            }
        }
        return false;
    }

    /**
     * Si la id de l coincide con la id de algun libro de la lista, lo actualiza
     * @param l libro que se quiera actualizar
     * @return  True si se ha actualizado un libro, false si no hay ninguno con la id de l
     */
    boolean actualizaFromLeidos(Libro l){
        for(int i = 0; i < leidos.size(); i++){
            if(l.getIdLibro().equals(leidos.get(i))){
                leidos.remove(i);
                leidos.add(i, l); //Se borra esa posición y se inserta l en esa posición
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve una copia si hay un libro con id en la lista
     * @param id id que se quiere buscar
     * @return  copia del libro de la lista
     */
    Libro getFromSeguidos(String id){
        Libro l = null;
        for(int i = 0; i<seguidos.size(); i++){
            if(seguidos.get(i).getIdLibro().equals(id)){
                l = new Libro(seguidos.get(i));        //Devuelve una copis
                return l;
            }
        }
        return l;
    }

    /**
     * Devuelve la copia de un libro de la lista seguidos
     * @param i indice en la lista
     * @return  copia del libro
     */
    Libro getFromSeguidos(int i){
        return new Libro(seguidos.get(i));
    }

    /**
     * Devuelve una copia si hay un libro con id en la lista
     * @param id id que se quiere buscar
     * @return  copia del libro de la lista
     */
    Libro getFromLeidos(String id){
        Libro l = null;
        for(int i = 0; i<leidos.size(); i++){
            if(leidos.get(i).getIdLibro().equals(id)){
                l = new Libro(leidos.get(i));        //Devuelve una copis
                return l;
            }
        }
        return l;
    }


    /**
     * Devuelve la copia de un libro de la lista Leidos
     * @param i indice en la lista
     * @return  copia del libro
     */
    Libro getFromLeidos(int i){
        return new Libro(leidos.get(i));
    }


    /**
     * Devuelve la copia de un libro de la lista busqueda
     * @param i indice en la lista
     * @return  copia del libro
     */
    Libro getFromBusqueda(int i){
        return new Libro(busqueda.get(i));
    }

    boolean isSeguidosChanged(){
        return seguidosChange;
    }

    boolean isLeidosChanged(){
        return leidosChange;
    }

    boolean isBusquedaChanged(){
        return busquedaChange;
    }

}

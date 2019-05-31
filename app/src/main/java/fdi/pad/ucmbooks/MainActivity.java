package fdi.pad.ucmbooks;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import java.lang.ref.WeakReference;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fdi.pad.about.AboutActivity;
import fdi.pad.clearData.ClearDataActivity;
import fdi.pad.libro.LibroExecutor;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    private LibreriaFragment libreria = new LibreriaFragment();
    private BuscarFragment buscar = new BuscarFragment();
    private LeidosFragment leidos = new LeidosFragment();
    private LibroExecutor libros = new LibroExecutor(this);
    public static Context mainContext;

    /**
     * Indica el tipo de fragmento accesible desde la navBar inferior de la aplicación
     */
    private enum FR_TYPE{
        LIBRERIA, BUSCAR, LEIDOS
    }

    FR_TYPE currentFragment = FR_TYPE.LIBRERIA;

    /**
     * Clase que implementa el listener de la caja de búsqueda
     */
    private class OnQueryTextListener implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchOnline(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    /**
     * Clase que implementa la busqueda en segundo plano
     */
    private static class AsyncServerRequest extends AsyncTask<String, Void, LibroExecutor> {
        private WeakReference<MainActivity> activityReference;

        AsyncServerRequest(MainActivity context){
            activityReference = new WeakReference<>(context);
        }

        /**
         * Realiza una búsuqeda en segundo plano
         * @param search palabras claves de la búsqueda
         * @return  resultado de la busqueda
         */
        @Override
        public LibroExecutor doInBackground(String... search){

            LibroExecutor libros = activityReference.get().libros;
            libros.deleteAllFromBusqueda();
            String apiKey = "ZjAhPX6VC8YMHCZIO5w6g";
            String urlText = "https://www.goodreads.com/search.xml?key=" + apiKey + "&q=" + search[0];

            try {

                URL url = new URL(urlText);
                URLConnection conn = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());
                NodeList nodes = doc.getElementsByTagName("work");
                for (int i = 0; i < nodes.getLength() && i < 10; i++) {
                    Element entry = (Element) nodes.item(i);
                    NodeList tmp = entry.getElementsByTagName("best_book");
                    Element entryLibro = (Element) tmp.item(0);
                    tmp = entryLibro.getElementsByTagName("author");
                    Element entryAutor = (Element) tmp.item(0);
                    libros.addToBusqueda(
                            mainContext,
                            entryLibro.getElementsByTagName("title").item(0).getTextContent(),
                            entryLibro.getElementsByTagName("id").item(0).getTextContent(),
                            entryAutor.getElementsByTagName("name").item(0).getTextContent(),
                            entryAutor.getElementsByTagName("id").item(0).getTextContent(),
                            entry.getElementsByTagName("average_rating").item(0).getTextContent(),
                            BitmapFactory.decodeStream(new URL(entryLibro.getElementsByTagName("image_url").item(0).getTextContent()).openConnection().getInputStream()),
                            entryLibro.getElementsByTagName("image_url").item(0).getTextContent()
                    );
                }
            } catch (IOException | ParserConfigurationException | SAXException e){
                e.printStackTrace();
            }
            return libros;
        }

        /**
         * Llamado tras la búsqueda. Notifica del resultado a la MainActivity
         * @param v resultado de la busqueda
         */
        @Override
        protected void onPostExecute(LibroExecutor v) {
            MainActivity activity = activityReference.get();
            if(activity == null || activity.isFinishing()) return;
            activity.searchNotifier(v);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        mainContext = this;
        try {
            libros.loadList();      //Carga la información de los libros almacenados en memoria
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, this.libreria).commit();
    }

    /**
     * Incluye en la barra superior de la aplicación un campo de búsqueda
     * @param menu layout de la caja de busqueda
     * @return resultado de la operacion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            if(currentFragment == FR_TYPE.BUSCAR) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.search_menu, menu);

                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false);
                searchView.setOnQueryTextListener(new OnQueryTextListener());
            }
            else {
                getMenuInflater().inflate(R.menu.app_menu, menu);
            }


        return true;
    }

    /**
     * Intercambia y muestra el fragment de la MainActivity por el que se especifica
     * @param fragment  fragment que se quiere mostrar
     * @return resultado de la operación
     */
    private boolean switchFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }

    /**
     * Intercambia y muestra el fargment de la MainActivity y añade el anterior al stack de vistas
     * @param fragment  fragmento que se quiere mostrar
     */
    public void switchWebViewFragment(int id, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Listener de la navbar inferior de la aplicación
     * @param menuItem  opción que se ha pulsado
     * @return  fragmento que se tiene que mostrar
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_libreria:
                fragment = this.libreria;
                currentFragment = FR_TYPE.LIBRERIA;
                break;

            case R.id.navigation_buscar:
                fragment = this.buscar;
                currentFragment = FR_TYPE.BUSCAR;
                break;

            case R.id.navigation_leidos:
                fragment = this.leidos;
                currentFragment = FR_TYPE.LEIDOS;
                break;
        }

        invalidateOptionsMenu();

        return switchFragment(fragment);
    }

    /**
     * Listener del menú de la aplicación
     * @param item  opción que s eha pulsado
     * @return resultado de la operación
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.menu_acercaDe){
            System.out.println("Pulsado Acerca de");
            Intent intent= new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.menu_deleteData){
            System.out.println("Pulsado Borrar Datos");
            Intent intent= new Intent(this, ClearDataActivity.class);
            startActivity(intent);
        }
        return true;
    }

    /**
     * Ejecuta en segundo plano una búsqueda de libros con las palabras indicadas. Actualiza la lista de buscados en el LibroExecutor
     * @param query palabas clave de la búsqueda
     */
    private void searchOnline(String query){
        AsyncServerRequest request = new AsyncServerRequest(this);
        request.execute(query);
    }

    /**
     * Indica al fragment Buscar que los resultados de una búsuqeda están listos
     * @param results resultado de la busqueda
     */
    private void searchNotifier(LibroExecutor results){
        buscar.refreshList();
    }
}

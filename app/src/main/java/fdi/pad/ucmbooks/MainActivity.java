package fdi.pad.ucmbooks;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import java.lang.ref.WeakReference;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import fdi.pad.Libro.LibroExecutor;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private LibreriaFragment libreria = new LibreriaFragment();
    private BuscarFragment buscar = new BuscarFragment();
    private LeidosFragment leidos = new LeidosFragment();
    public static Context mainContext;

    private enum FR_TYPE{
        LIBRERIA, BUSCAR, LEIDOS
    }

    FR_TYPE currentFragment = FR_TYPE.LIBRERIA;

    private class OnQueryTextListener implements SearchView.OnQueryTextListener{
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchOnline(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    private static class AsyncServerRequest extends AsyncTask<String, Void, LibroExecutor> {
        private WeakReference<MainActivity> activityReference;

        AsyncServerRequest(MainActivity context){
            activityReference = new WeakReference<>(context);
        }
        @Override
        public LibroExecutor doInBackground(String... search){

            LibroExecutor libros = new LibroExecutor(mainContext);
            String apiKey = "ZjAhPX6VC8YMHCZIO5w6g";
            String urlText = "https://www.goodreads.com/search.xml?key=" + apiKey + "&q=" + search[0];

            try {
                URL url = new URL(urlText);
                URLConnection conn = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());
                NodeList nodes = doc.getElementsByTagName("work");
                //Borramos los libros guardados en la lista y que no estén seguidos para liberar memoria
                libros.deleteNonFollowedFromList();
                //Realizamos la búsqueda
                for (int i = 0; i < nodes.getLength() && i < 10; i++) {
                    Element entry = (Element) nodes.item(i);
                    NodeList tmp = entry.getElementsByTagName("best_book");
                    Element entryLibro = (Element) tmp.item(0);
                    tmp = entryLibro.getElementsByTagName("author");
                    Element entryAutor = (Element) tmp.item(0);
                    libros.addLibro( //TODO addLibro devuelve true o false. Convendría hacer logs en consola diciendo si fallo?
                            mainContext, //Funciona?
                            entryLibro.getElementsByTagName("title").item(0).getTextContent(),
                            entryLibro.getElementsByTagName("id").item(0).getTextContent(),
                            entryAutor.getElementsByTagName("name").item(0).getTextContent(),
                            entryAutor.getElementsByTagName("id").item(0).getTextContent(),
                            entry.getElementsByTagName("average_rating").item(0).getTextContent(),
                            BitmapFactory.decodeStream(new URL(entryLibro.getElementsByTagName("image_url").item(0).getTextContent()).openConnection().getInputStream()),
                            entryLibro.getElementsByTagName("image_url").item(0).getTextContent()
                    );
                    //libros.deleteAllFromFileSystem(); //TODO Borrar esto cuando las pruebas finalicen
                }
            } catch (IOException | ParserConfigurationException | SAXException e){
                e.printStackTrace();
            }
            return libros;
        }

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
        /*Para probar que el fragmento libreria funciona*/
        /*Libro prueba = new Libro(this, "Titulo", "id", "Autor", "IDAutor", "Rating");
        Libro prueba2 = new Libro(this, "Titulo2", "id2", "Autor2", "IDAutor2", "Rating2");
        ArrayList<Libro> listaPrueba = new ArrayList<>();
        listaPrueba.add(prueba);
        listaPrueba.add(prueba2);
        this.libreria.setLista(listaPrueba);
        this.leidos.setLista(listaPrueba);*/ //TODO Quitar esto cuando no sean necesarias más pruebas
        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, this.libreria).commit();
        mainContext = this;
        //Prueba de fragment de webview
        /*setContentView(R.layout.fragment_libro);
        LibroFragment libro = new LibroFragment();
        LibroExecutor l = new LibroExecutor(mainContext);
        l.addLibroSinImagen(mainContext, "patatas", "14", "JAVI", "19", "4");
        libro.setLista(l);*/
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.

            if(currentFragment == FR_TYPE.BUSCAR) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.search_menu, menu);

                // Get the SearchView and set the searchable configuration
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
                // Assumes current activity is the searchable activity
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
                searchView.setOnQueryTextListener(new OnQueryTextListener());
            }
            else {
                getMenuInflater().inflate(R.menu.app_menu, menu);
            }


        return true;
    }

    private boolean switchFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }

    public void switchWebViewFragment(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }


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

    private void searchOnline(String query){
        AsyncServerRequest request = new AsyncServerRequest(this);
        request.execute(query);
    }

    private void searchNotifier(LibroExecutor results){
        for(int i = 0; i < results.getListaLibros().size(); i++){
            System.out.println(results.getTitulo(results.getId(i)));
        }
        buscar.refreshList(results);
    }
}

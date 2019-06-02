package fdi.pad.ucmbooks;

import fdi.pad.buscarUI.ButtonRVAdapter;
import fdi.pad.libro.LibroExecutor;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BuscarFragment extends Fragment {

    LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);
    boolean loadingBooks = false;
    RecyclerView.Adapter rvAdapter;
    String ultimaBusqueda;
    int pagina = 1;

    /**
     * Clase que implementa la busqueda en segundo plano
     */
    private class AsyncServerRequest extends AsyncTask<String, Void, Void> {

        /**
         * Realiza una búsuqeda en segundo plano
         * @param search palabras claves de la búsqueda
         * @return  resultado de la busqueda
         */
        @Override
        public Void doInBackground(String... search){

            LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);
            String apiKey = "ZjAhPX6VC8YMHCZIO5w6g";
            String urlText = "https://www.goodreads.com/search.xml?key=" + apiKey + "&q=" + search[0] + "&page=" + pagina;

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
            return null;
        }

        /**
         * Llamado tras la búsqueda. Notifica del resultado a la MainActivity
         * @param param resultado de la busqueda
         */
        @Override
        protected void onPostExecute(Void param) {
            rvAdapter.notifyDataSetChanged();
            loadingBooks= false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        RecyclerView rv = view.findViewById(R.id.books_list_view);
        final RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        rvAdapter = new ButtonRVAdapter(ButtonRVAdapter.TIPO_BRVA.BUSCADO);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = rvLayoutManager.getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) rvLayoutManager).findLastVisibleItemPosition();
                if (!loadingBooks && totalItemCount <= (lastVisibleItem + 2)) {
                    loadingBooks=true;
                    addToList();
                }
            }
        });
        rv.setAdapter(rvAdapter);
        return view;
    }

    /**
     * Actualiza el fragmento Buscar para mostrar la última búsqueda que se ha realizado
     */
    public void refreshList(String busqueda){
        this.ultimaBusqueda = busqueda;
        this.pagina = 1;
        this.loadingBooks = true;
        libros.deleteAllFromBusqueda();
        rvAdapter.notifyDataSetChanged();
        AsyncServerRequest request = new AsyncServerRequest();
        request.execute(busqueda);
    }

    private void addToList(){
        this.pagina++;
        AsyncServerRequest request = new AsyncServerRequest();
        request.execute(this.ultimaBusqueda);
    }
}
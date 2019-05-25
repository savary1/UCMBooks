package fdi.pad.ucmbooks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import fdi.pad.Libro.LibroExecutor;

public class LibroFragment extends Fragment {

    private RecyclerView rv;

    private WebView webView;
    private String fileName = "LibroView.html";

    private LibroExecutor libro;

    /**
     * Establece la lista de libros, ya que los libros individuales son privados. La lista solo tiene un libro (índice 0)
     * @param libro el libro para ser pasado. Es el índice 0 de la lista para los libros
     */
    public void setLista(LibroExecutor libro){
        this.libro=libro;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_libro, container, false);

        webView = (WebView) webView.findViewById(R.id.LibroWebView);
        webView.loadUrl("file:///android_asset/" + fileName);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(libro, "Android");

        return view;
    }

}

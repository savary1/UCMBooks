package fdi.pad.ucmbooks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import fdi.pad.Libro.LibroExecutor;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibroFragment extends Fragment {

    String fileName = "LibroView.html";
    WebView webView;
    LibroExecutor libro;

    public LibroFragment() {
        // Required empty public constructor
    }

    public void setList(LibroExecutor libro) {
        this.libro = libro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libro, container, false);
        webView = webView.findViewById(R.id.LibroWebView);
        webView.loadUrl("file:///android_asset/" + fileName);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(libro, "Android");

        /*TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);*/
        return view;
    }

}

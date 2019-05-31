package fdi.pad.ucmbooks;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import fdi.pad.libro.Libro;
import fdi.pad.libro.LibroExecutor;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibroFragment extends Fragment {

    String fileName = "LibroView.html";
    WebView webView;
    Libro libro;
    LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);

    public LibroFragment() {
        setRetainInstance(true);
    }

    public void setList(Libro libro) {
        this.libro = libro;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_libro, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView)view.findViewById(R.id.LibroWebView);
        //webView.loadUrl("file:///android_asset/" + fileName);
        String html = "";
        //this.libro.buttonSeguir(libro.getId(this.libroIndex));

        html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body style=\"background-color:#F2F4F4;\"></body>" +
                "<head>\n" +
                "<center>" +
                "<button onClick=\"goBackScript()\">Atrás</button>\t" +
                "<input onClick=\"seguirLibroScript()\" type=\"button\" id=\"seguir\" value='{SEGUIR_PLACEHOLDER}'</input>\t" +
                "<input onClick=\"libroLeidoScript()\" type=\"button\" id=\"leido\" value='{LEIDO_PLACEHOLDER}' '{DISABLED_PlACEHOLDER}'</input>\t" +
                "</center>" +
                "<h2> <font color=#212F3D>" + this.libro.getTitulo() + "</h2>" +
                "<h3> <font color=#707B7C>" + this.libro.getAutor() + "</font> </h3>\n" +
                "<h4> <font color=#7F8C8D>Puntuación: </font><font color='{COLOR_PLACEHOLDER}'>" + this.libro.getRating() + "</font><font color=#A9A9A9> / 5 </font> </h4>\n" +
                "</head>\n" +
                "<center> <img src = '{IMAGE_PLACEHOLDER}' /> </center>";

        html += "<foot>" +
                "<p> <font color=#95A5A6>ID del libro:" + this.libro.getIdLibro() + "</font> </p>\n" +
                "<p> <font color=#95A5A6>ID del autor:" + this.libro.getIdAutor() + "</font> </p>\n" +
                "</foot>";

        html += "<script>" +
                "function goBackScript() {" +
                "   Android.goBack();" +
                "} " +
                "function seguirLibroScript() {" +
                "   Android.seguirLibro(); " +
                "   var elem = document.getElementById(\"seguir\");" +
                "   var elemL = document.getElementById(\"leido\");" +
                "   if (elem.value==\"Seguir\") {" +
                "       elem.value = \"Dejar de seguir\";" +
                "       elemL.disabled=false;" +
                "       elemL.value = \"Marcar como leido\";" +
                "   }" +
                "   else {" +
                "       elem.value = \"Seguir\";" +
                "       elemL.disabled=true;" +
                "       elemL.value = \"Marcar como leido\";" +
                "   }" +
                "}" +
                "function libroLeidoScript() {" +
                "   Android.libroLeido();" +
                "   var elem = document.getElementById(\"leido\");" +
                "   if (elem.value==\"Marcar como leído\") elem.value = \"Marcar como no leído\";" +
                "   else elem.value = \"Marcar como leído\";" +
                "}" +
                "</script>" +
                "</html>";

        // Convert bitmap to Base64 encoded image for web
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        libro.getImage().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String image = "data:image/png;base64," + imgageBase64;

        // Cambiamos la imagen
        html = html.replace("{IMAGE_PLACEHOLDER}", image);
        //Cambiamos si el libro es o no seguido
        if(!this.libro.isSeguido())
            html = html.replace("{SEGUIR_PLACEHOLDER}", "Seguir");
        else
            html = html.replace("{SEGUIR_PLACEHOLDER}", "Dejar de seguir");
        //Cambiamos si el libro es o no leído
        if(!this.libro.isLeido())
            html = html.replace("{LEIDO_PLACEHOLDER}", "Marcar como leído");
        else
            html = html.replace("{LEIDO_PLACEHOLDER}", "Marcar como no leído");
        if(Double.parseDouble(libro.getRating()) < 2.5)
            html = html.replace("{COLOR_PLACEHOLDER}", "#B22222"); //ROJO
        else if (Double.parseDouble(libro.getRating()) >= 2.5 &&
                Double.parseDouble(libro.getRating()) < 3.5)
            html = html.replace("{COLOR_PLACEHOLDER}", "#FFA500"); //NARANJA
        else if (Double.parseDouble(libro.getRating()) >= 3.5 &&
                Double.parseDouble(libro.getRating()) < 4.5)
            html = html.replace("{COLOR_PLACEHOLDER}", "#CDDC39"); //VERDE-AMARILLO
        else
            html = html.replace("{COLOR_PLACEHOLDER}", "#32CD32"); //VERDE


        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");

        //webView.loadData(html, "text/html", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "Android");

    }

    @JavascriptInterface
    public void goBack() {
        getFragmentManager().popBackStack();
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @JavascriptInterface
    public void seguirLibro() {
        if(this.libro.isSeguido()){
            this.libros.deleteFromSeguidos(this.libro.getIdLibro());
        }
        else{
            this.libro.buttonSeguir();
            this.libros.addToLeidos(libro);
        }
    }

    @JavascriptInterface
    public void libroLeido() {
        if(!this.libro.isLeido()) {
            this.libro.buttonSeguir();
            this.libro.libroLeido();
            this.libros.deleteFromSeguidos(this.libro.getIdLibro());
            this.libros.addToLeidos(this.libro);
        }
    }

    @JavascriptInterface
    public void libroNoLeido() {
        if(this.libro.isLeido()){
            this.libro.dejarDeSeguirLibro();
            this.libros.deleteFromLeidos(libro.getIdLibro());
            this.libros.addToSeguidos(this.libro);
        }
    }

}

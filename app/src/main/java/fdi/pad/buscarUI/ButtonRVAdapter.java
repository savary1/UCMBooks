package fdi.pad.buscarUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fdi.pad.libro.Libro;
import fdi.pad.libro.LibroExecutor;
import fdi.pad.ucmbooks.R;

public class ButtonRVAdapter extends RecyclerView.Adapter<ButtonRVAdapter.ButtonBookViewHolder> {

    public static class ButtonBookViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookTitle;
        TextView bookAuthor;
        ImageView bookCover;
        Button boton;

        ButtonBookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.book_view);
            bookTitle = (TextView)itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView)itemView.findViewById(R.id.book_author);
            bookCover = (ImageView)itemView.findViewById(R.id.cover_photo);
            boton = (Button) itemView.findViewById(R.id.seguir_button);
        }
    }

    public enum TIPO_BRVA{
        SEGUIDO, BUSCADO
    }

    private LibroExecutor libros;
    private TIPO_BRVA tipo;

    public ButtonRVAdapter (LibroExecutor libros, TIPO_BRVA tipo){
        this.libros = libros;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ButtonRVAdapter.ButtonBookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        switch(tipo){
            case BUSCADO:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.buscar_card_view, viewGroup, false);
                break;
            case SEGUIDO:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seguido_card_view, viewGroup, false);
                break;
            default:
                v = null;
                break;
        }
        ButtonRVAdapter.ButtonBookViewHolder pvh = new ButtonRVAdapter.ButtonBookViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonRVAdapter.ButtonBookViewHolder bookViewHolder, int i) {
        final int index = i;
        final Libro l;
        if(tipo == TIPO_BRVA.SEGUIDO){
            l = libros.getFromSeguidos(i);
        }
        else {
            l = libros.getFromBusqueda(i);
        }
        bookViewHolder.bookTitle.setText(l.getTitulo());
        bookViewHolder.bookAuthor.setText(l.getAutor());
        bookViewHolder.bookCover.setImageBitmap(l.getImage());
        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            /*
            TODO Poner aki el kodigo de lanzar la viu del book
             */
                System.out.println("Click en libro: " + l.getTitulo());
            }
        });
        bookViewHolder.boton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            /*
            TODO Poner aki el kodigo del boton del libro
             */
                System.out.println("Click en boton de libro: " + l.getTitulo());
                libros.addToSeguidos(l);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(libros != null)
            if(tipo == TIPO_BRVA.SEGUIDO)
                return libros.getSeguidosSize();
            if(tipo == TIPO_BRVA.BUSCADO)
                return libros.getBusquedaSize();
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

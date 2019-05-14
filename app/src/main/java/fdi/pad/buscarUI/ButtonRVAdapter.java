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

import fdi.pad.ucmbooks.Libro;
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

    ArrayList<Libro> libros;
    TIPO_BRVA tipo;

    public ButtonRVAdapter (ArrayList<Libro> libros, TIPO_BRVA tipo){
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
    public void onBindViewHolder(@NonNull ButtonRVAdapter.ButtonBookViewHolder bookViewHolder, final int i) {
        bookViewHolder.bookTitle.setText(libros.get(i).getTitulo());
        bookViewHolder.bookAuthor.setText(libros.get(i).getAutor());
        bookViewHolder.bookCover.setImageBitmap(libros.get(i).getImage());
        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            /*
            TODO Poner aki el kodigo de lanzar la viu del book
             */
                System.out.println("Click en libro: " + libros.get(i).getTitulo());
            }
        });
        bookViewHolder.boton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            /*
            TODO Poner aki el kodigo de lanzar la viu del book
             */
                System.out.println("Click en boton de libro: " + libros.get(i).getTitulo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.libros.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

package fdi.pad.buscarUI;

import fdi.pad.ucmbooks.Libro;

import java.util.ArrayList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import fdi.pad.ucmbooks.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder>{

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookTitle;
        TextView bookAuthor;
        ImageView bookCover;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.book_view);
            bookTitle = (TextView)itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView)itemView.findViewById(R.id.book_author);
            bookCover = (ImageView)itemView.findViewById(R.id.cover_photo);
        }
    }

    ArrayList<Libro> libros;

    public RVAdapter(ArrayList<Libro> libros){
        this.libros = libros;
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_card_view, viewGroup, false);
        BookViewHolder pvh = new BookViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, final int i) {

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
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

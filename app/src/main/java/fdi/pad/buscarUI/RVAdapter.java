package fdi.pad.buscarUI;

import java.util.ArrayList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import fdi.pad.libro.LibroExecutor;
import fdi.pad.ucmbooks.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder>{

    static class BookViewHolder extends RecyclerView.ViewHolder {
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

    private LibroExecutor libros;

    public RVAdapter(LibroExecutor libros){
        this.libros = libros;
    }

    @Override
    public int getItemCount() {
        if(libros != null)
            return libros.getListaLibros().size();
        return 0;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_card_view, viewGroup, false);
        BookViewHolder pvh = new BookViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, final int i) {
        bookViewHolder.bookTitle.setText(libros.getTitulo(libros.getId(i)));
        bookViewHolder.bookAuthor.setText(libros.getAutor(libros.getId(i)));
        bookViewHolder.bookCover.setImageBitmap(libros.getImage(libros.getId(i)));
        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            /*
            TODO Poner aki el kodigo de lanzar la viu del book
             */
            System.out.println("Click en libro: " + libros.getTitulo(libros.getId(i)));
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

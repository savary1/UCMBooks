package fdi.pad.buscarUI;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import fdi.pad.libro.LibroExecutor;
import fdi.pad.ucmbooks.LibroFragment;
import fdi.pad.ucmbooks.MainActivity;
import fdi.pad.ucmbooks.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder>{

    static class BookViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookTitle;
        TextView bookAuthor;
        ImageView bookCover;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.book_view);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookCover = itemView.findViewById(R.id.cover_photo);
        }
    }

    private LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);

    @Override
    public int getItemCount() {
        if(libros != null)
            return libros.getLeidosSize();
        return 0;
    }

    @Override
    @NonNull
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_card_view, viewGroup, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        final int index = i;
        bookViewHolder.bookTitle.setText(libros.getFromLeidos(i).getTitulo());
        bookViewHolder.bookAuthor.setText(libros.getFromLeidos(i).getAutor());
        bookViewHolder.bookCover.setImageBitmap(libros.getFromLeidos(i).getImage());
        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LibroFragment lF = new LibroFragment();
                lF.setList(libros.getFromLeidos(index));
                switchContent(R.id.fragment_container, lF);
            }
        });
    }
    /**
     * Cambia el fragment que se muesta en la MainActivity
     * @param id    contenedor del fragment en el que se quiere mostrat
     * @param fragment  fragment que se quiere mostrar
     */
    private void switchContent(int id, Fragment fragment) {
        if (MainActivity.mainContext == null)
            return;
        if (MainActivity.mainContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) MainActivity.mainContext;
            mainActivity.switchWebViewFragment(id, fragment);
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

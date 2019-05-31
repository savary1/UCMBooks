package fdi.pad.buscarUI;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fdi.pad.libro.Libro;
import fdi.pad.libro.LibroExecutor;
import fdi.pad.ucmbooks.LibroFragment;
import fdi.pad.ucmbooks.MainActivity;
import fdi.pad.ucmbooks.R;

public class ButtonRVAdapter extends RecyclerView.Adapter<ButtonRVAdapter.ButtonBookViewHolder> {

    static class ButtonBookViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView bookTitle;
        TextView bookAuthor;
        ImageView bookCover;
        Button boton;

        ButtonBookViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.book_view);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookCover = itemView.findViewById(R.id.cover_photo);
            boton = itemView.findViewById(R.id.seguir_button);
        }
    }

    public enum TIPO_BRVA{
        SEGUIDO, BUSCADO
    }

    private LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);
    /**
     *Indica si se trata de un adaptador que muestra libros seguidos o libros que se han buscado
     */
    private TIPO_BRVA tipo;

    public ButtonRVAdapter (TIPO_BRVA tipo){
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
        return  new ButtonRVAdapter.ButtonBookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonRVAdapter.ButtonBookViewHolder bookViewHolder, int i) {
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
                LibroFragment lF = new LibroFragment();
                lF.setList(l);
                switchContent(R.id.fragment_container, lF);
            }
        });
        bookViewHolder.boton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(tipo == TIPO_BRVA.BUSCADO) {
                    l.buttonSeguir();
                    libros.addToSeguidos(l);
                }
                else{
                    libros.deleteFromSeguidos(l.getIdLibro());
                    l.libroLeido();
                    libros.addToLeidos(l);
                }
                notifyDataSetChanged();
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
    public int getItemCount() {
        if(libros != null)
            if(tipo == TIPO_BRVA.SEGUIDO)
                return libros.getSeguidosSize();
            if(tipo == TIPO_BRVA.BUSCADO)
                return libros.getBusquedaSize();
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

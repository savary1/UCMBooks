package fdi.pad.ucmbooks;

import fdi.pad.buscarUI.ButtonRVAdapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

public class BuscarFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        RecyclerView rv = view.findViewById(R.id.books_list_view);
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        RecyclerView.Adapter rvAdapter = new ButtonRVAdapter(ButtonRVAdapter.TIPO_BRVA.BUSCADO);
        rv.setAdapter(rvAdapter);
        return view;
    }

    /**
     * Actualiza el fragmento Buscar para mostrar la última búsqueda que se ha realizado
     */
    public void refreshList(){
        RecyclerView rv = getView().findViewById(R.id.books_list_view);
        ButtonRVAdapter adapter = new ButtonRVAdapter(ButtonRVAdapter.TIPO_BRVA.BUSCADO);
        rv.setAdapter(adapter);
    }
}
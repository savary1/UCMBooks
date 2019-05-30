package fdi.pad.ucmbooks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fdi.pad.buscarUI.ButtonRVAdapter;
import fdi.pad.libro.LibroExecutor;


public class LibreriaFragment extends Fragment {
    private RecyclerView rv;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        rv = view.findViewById(R.id.books_list_view);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        rvAdapter = new ButtonRVAdapter(ButtonRVAdapter.TIPO_BRVA.SEGUIDO);
        rv.setAdapter(rvAdapter);
        return view;
    }


    public void refreshList(){
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.books_list_view);
        ButtonRVAdapter adapter = new ButtonRVAdapter(ButtonRVAdapter.TIPO_BRVA.SEGUIDO);
        rv.setAdapter(adapter);
    }

}
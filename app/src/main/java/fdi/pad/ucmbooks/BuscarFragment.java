package fdi.pad.ucmbooks;

import fdi.pad.buscarUI.ButtonRVAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

public class BuscarFragment extends Fragment {

    private RecyclerView rv;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        rv = view.findViewById(R.id.books_list_view);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        rvAdapter = new ButtonRVAdapter(new ArrayList<Libro>(), ButtonRVAdapter.TIPO_BRVA.BUSCADO);
        rv.setAdapter(rvAdapter);
        return view;
    }

    public void refreshList(ArrayList<Libro> libros){
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.books_list_view);
        ButtonRVAdapter adapter = new ButtonRVAdapter(libros, ButtonRVAdapter.TIPO_BRVA.BUSCADO);
        rv.setAdapter(adapter);
    }
}
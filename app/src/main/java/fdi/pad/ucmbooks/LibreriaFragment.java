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

import fdi.pad.buscarUI.RVAdapter;


public class LibreriaFragment extends Fragment {
    private RecyclerView rv;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    private ArrayList<Libro> guardados;

    /*Los fragment solo pueden tener la constructora por defecto, no se pueden pasar argumentos
    por la constructora*/
    public void setLista(ArrayList<Libro> guardados){
        this.guardados=guardados;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        rv = view.findViewById(R.id.searched_list_view);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        rvAdapter = new RVAdapter(guardados);
        rv.setAdapter(rvAdapter);
        return view;
    }

    public void refreshList(Libro libro){
        guardados.add(libro);
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.searched_list_view);
        RVAdapter adapter = new RVAdapter(guardados);
        rv.setAdapter(adapter);
    }
}
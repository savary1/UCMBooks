package fdi.pad.ucmbooks;

import fdi.pad.buscarUI.RVAdapter;

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
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        rv = view.findViewById(R.id.searched_list_view);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        rvAdapter = new RVAdapter(new ArrayList<Libro>());
        rv.setAdapter(rvAdapter);
        return view;
    }
    /*
    View v = inflater.inflate(R.layout.fragment_genres, container, false);
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(getActivity());
recyclerView.setLayoutManager(layoutManager);
return v;
*/

    public void refreshList(ArrayList<Libro> libros){
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.searched_list_view);
        RVAdapter adapter = new RVAdapter(libros);
        rv.setAdapter(adapter);
    }
}
package fdi.pad.ucmbooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fdi.pad.buscarUI.RVAdapter;


public class LeidosFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        RecyclerView rv = view.findViewById(R.id.books_list_view);
        RecyclerView.LayoutManager rvLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvLayoutManager);
        RecyclerView.Adapter rvAdapter = new RVAdapter();
        rv.setAdapter(rvAdapter);
        return view;
    }

}
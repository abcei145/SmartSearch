package com.example.monica.smartstudents;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class InicioFragment extends Fragment {

    Button eliminar1, programar1;
    public InicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Bundle args=getArguments();
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        eliminar1=(Button) view.findViewById(R.id.eliminarclases);
        programar1=(Button) view.findViewById(R.id.programarclases);

        eliminar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new EliminarFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });
        programar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new ProgramarFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });

        return view;
    }

}

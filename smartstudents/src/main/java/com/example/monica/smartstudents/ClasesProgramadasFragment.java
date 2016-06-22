package com.example.monica.smartstudents;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClasesProgramadasFragment extends Fragment {


    private String[] tagTitles;
    private TextView fecha;
    private ListView drawerList;
    AlertDialog.Builder dialogo1;
    String text;
    public ClasesProgramadasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_clases_programadas, container, false);
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        int i;
        tagTitles=getResources().getStringArray(R.array.horariossemana);

        items.add(new ListItem(tagTitles[0]+Integer.toString(1)));
        for(i=1;i<tagTitles.length;i++) {
            items.add(new ListItem(tagTitles[i]));
        }
        fecha=(TextView) view.findViewById(R.id.fechaprogramadas);
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);

        if(month<9){

            text=Integer.toString(day)+"/0"+Integer.toString(month)+"/"+Integer.toString(year);

        }else{

            text=Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
        }
        fecha.setText(text);
        drawerList = (ListView) view.findViewById(R.id.clasesprogramadas);
        // Relacionar el adaptador y la escucha de la lista del drawer
        drawerList.setAdapter(new ListAdapter(getActivity(), items));
        drawerList.setOnItemClickListener(new ItemClickListener());

        /**/

        return view;
    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != 0) {
                dialogo1 = new AlertDialog.Builder(getActivity());
                TextView vista = (TextView) view.findViewById(R.id.name);
                String name = vista.getText().toString();
                dialogo1.setTitle("Esta seguro que desea programar\n la clase: x");
                dialogo1.setMessage("Vas a programar la clase: \nHORA: " + name + "\nFECHA: " + text);
                dialogo1.setCancelable(true);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        aceptar();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cancelar();
                    }
                });
                dialogo1.show();
            }

        }
    }
    public void aceptar() {
        Toast t=Toast.makeText(getActivity().getApplicationContext(),"Bienvenido a probar el programa.", Toast.LENGTH_SHORT);
        t.show();
    }
    public void cancelar() {

    }

}

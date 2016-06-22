package com.example.monica.smartstudents;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EliminarFragment extends Fragment {

    private String[] tagTitles;
    private TextView fecha;
    AlertDialog.Builder dialogo1;
    private static final String FIREBASE_URL="https://smart-a967b.firebaseio.com/Estudiantes/Estudiante 123";
    private Firebase firebasedatos;
    String text;
    private ListView drawerList;
    private Bundle args;
    private int pos;

    private String[] Vector1;
    private String ClasePendiente, ClaseActual;
    public EliminarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_eliminar, container, false);
        args=getArguments();
        tagTitles = getResources().getStringArray(R.array.horariossemana);

        ClasePendiente = args.getString("clasePendiente");
        ClaseActual = args.getString("claseActual");
        Vector1=args.getStringArray("horPendientes");

        Firebase.setAndroidContext(getActivity().getApplicationContext());
        firebasedatos=new Firebase(FIREBASE_URL);
        drawerList = (ListView) view.findViewById(R.id.eliminar);


        fecha=(TextView) view.findViewById(R.id.fechaeliminar);
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
        ObtenerEliminar(view);

        /**/

        return view;
    }
    public void ObtenerEliminar(final View view) {
        String aux;
        ArrayList<ListItem> items2 = new ArrayList<ListItem>();
        int i,cnt;
        cnt=0;
        int pendiente = Integer.parseInt(ClasePendiente);
        for (i =  0; i < Vector1.length; i++) {
            aux=Vector1[i];
            if(aux.equals("2")) {

                items2.add(new ListItem("Clase " + Integer.toString(Integer.parseInt(ClaseActual) + cnt + 1) + "  Hora: " + tagTitles[i+1]));
                cnt++;
            }
        }
        drawerList.setAdapter(new ListAdapter(getActivity(), items2));
        drawerList.setOnItemClickListener(new ItemClickListener());

    }

    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position >= 0) {
                pos=position;
                dialogo1 = new AlertDialog.Builder(getActivity());
                TextView vista = (TextView) view.findViewById(R.id.name);
                String name = vista.getText().toString();
                dialogo1.setTitle("Esta seguro que desea programar\n la clase: x");
                dialogo1.setMessage("Vas a programar la clase: "+ Integer.toString(position+Integer.parseInt(ClaseActual))+"\nHORA: " + name + "\nFECHA: " + text);
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
        Firebase firebd;
        int i;
        int cnt=0;
        String aux;
        for (i =  0; i < Vector1.length; i++) {
            aux=Vector1[i];
            if(true) {

                if(pos-cnt==0){
                    firebasedatos.child("horPendientes").child(Integer.toString(i)).setValue("0");
                }

                if(cnt>=pos){
                    Vector1[i] = "0";

                    firebd = firebasedatos.child("horProgramados").child(Integer.toString(cnt+1 + Integer.parseInt(ClaseActual)));
                    firebd.removeValue();
                    firebasedatos.child("horPendientes").child(Integer.toString(i)).setValue("0");
                }
                if (aux.equals("2")) {
                    cnt++;
                }

            }

        }
        firebasedatos.child("clasePendiente").setValue(Integer.toString(Integer.parseInt(ClasePendiente)-pos-1));

    }
    public void cancelar() {

    }
}

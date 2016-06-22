package com.example.monica.smartstudents;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramarFragment extends Fragment {

    private String[] tagTitles;
    private TextView fecha;
    private int auxi;
    private String []HorariosDisponibles;
    AlertDialog.Builder dialogo1;
    private String text,ClasePendiente,name;
    private static final String FIREBASE_URL="https://smart-a967b.firebaseio.com/Estudiantes/Estudiante 123";
    private Firebase firebasedatos;
    private ListView drawerList;
    private Bundle args;
    public ProgramarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_programar, container, false);

        args=getArguments();
        ClasePendiente=args.getString("clasePendiente");
        HorariosDisponibles=args.getStringArray("horPendientes");
        Firebase.setAndroidContext(getActivity().getApplicationContext());
        firebasedatos=new Firebase(FIREBASE_URL);
        drawerList = (ListView) view.findViewById(R.id.programar);
        tagTitles=getResources().getStringArray(R.array.horariossemana);
        fecha=(TextView) view.findViewById(R.id.fechaprogramar);
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);

        if(month<9){

            text=Integer.toString(day)+"/0"+Integer.toString(month)+"/"+Integer.toString(year);

        }else{

            text=Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
        }

        Obtener();

        /**/

        return view;
    }
public void Obtener(){

    ArrayList<ListItem> items = new ArrayList<ListItem>();
    int i;
    items.add(new ListItem(tagTitles[0] + ClasePendiente));
    for(i=1;i<tagTitles.length;i++) {
        if(HorariosDisponibles[i-1].equals("0")){
            items.add(new ListItem(tagTitles[i]));
        }
    }
    // Relacionar el adaptador y la escucha de la lista del drawer
    drawerList.setAdapter(new ListAdapter(getActivity(), items));
    drawerList.setOnItemClickListener(new ItemClickListener());

}
    private class ItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != 0) {
                dialogo1 = new AlertDialog.Builder(getActivity());
                TextView vista = (TextView) view.findViewById(R.id.name);
                name = vista.getText().toString();
                dialogo1.setTitle("Esta seguro que desea programar la clase: " + ClasePendiente);
                dialogo1.setMessage("Vas a programar la clase: "+ClasePendiente+ " \nHORA: " + name + "\nFECHA: " + text);
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
        int pendiente;
        Firebase firebd=firebasedatos.child("clasePendiente");

        Map<String, Object> nuevonombre = new HashMap <String, Object>();

        pendiente=Integer.valueOf(ClasePendiente)+1;

        ClasePendiente=Integer.toString(pendiente);

        nuevonombre.put(ClasePendiente,name);
        firebd.setValue(ClasePendiente);
        args.putString("clasePendiente",ClasePendiente);
        int i;

        firebasedatos.child("horProgramados").updateChildren(nuevonombre);
        for(i=1;i<tagTitles.length;i++) {
            if(!HorariosDisponibles[i - 1].equals("2")) {
                HorariosDisponibles[i - 1] = "1";
                firebasedatos.child("horPendientes").child(Integer.toString(i - 1)).setValue("1");
                if (tagTitles[i].equals(name)) {
                    firebasedatos.child("horPendientes").child(Integer.toString(i - 1)).setValue("2");
                    HorariosDisponibles[i - 1] = "2";
                    break;
                }
            }
        }
        // Relacionar el adaptador y la escucha de la lista del drawer
        Fragment fragment;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment = new InicioFragment();
        fragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
    public void cancelar() {

    }

}

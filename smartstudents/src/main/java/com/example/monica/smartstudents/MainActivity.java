package com.example.monica.smartstudents;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] tagTitles;
    private static final String FIREBASE_URL = "https://smart-a967b.firebaseio.com/Estudiantes";
    private Firebase firebasedatos;
    private CharSequence activityTitle;
    private CharSequence itemTitle;
    private int auxi;
    private ActionBarDrawerToggle drawerToggle;
    private String[] HorariosDisponibles,HorariosPendientes;

    private Bundle args;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auxi=1;
        Firebase.setAndroidContext(this);
        firebasedatos = new Firebase(FIREBASE_URL);
        Firebase firebd = firebasedatos.child("Estudiante 123");
        Estudiantes producto = new Estudiantes("Santiago", "Salgado", "3722685", "correoarrobapirobopuntcom", "123", "0", "0");
        firebd.setValue(producto);

        setContentView(R.layout.activity_main);
        HorariosDisponibles = new String[]{"0","0","0","0","0","0","0","0","0","0"};
        HorariosPendientes=new String[10];
        itemTitle = activityTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tagTitles = getResources().getStringArray(R.array.Tags);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        args=new Bundle();
        args.putStringArray("horPendientes", HorariosDisponibles);
        args.putString("clasePendiente", "0");
        args.putStringArray("horProgramados", HorariosPendientes);
        args.putString("claseActual", "0");

        Obtener();


        Fragment fragment;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new BibliografiaFragment();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        // Setear una sombra sobre el contenido principal cuando el drawer se despliegue
        // drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //Crear elementos de la lista
        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();

        items.add(new DrawerItem("claroo", R.drawable.drawer_toggle    ));
        items.add(new DrawerItem(tagTitles[0], R.drawable.indicador_derecho ));
        items.add(new DrawerItem(tagTitles[1], R.drawable.indicador_derecho));
        items.add(new DrawerItem(tagTitles[2], R.drawable.indicador_derecho));
        items.add(new DrawerItem(tagTitles[3], R.drawable.indicador_derecho));
        items.add(new DrawerItem(tagTitles[4], R.drawable.indicador_derecho));
        items.add(new DrawerItem(tagTitles[5], R.drawable.indicador_derecho));

        // Relacionar el adaptador y la escucha de la lista del drawer
        drawerList.setAdapter(new DrawerListAdapter(this, items));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());


        // Habilitar el icono de la app por si hay algún estilo que lo deshabilitó
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_toggle);
        // Crear ActionBarDrawerToggle para la apertura y cierre
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.drawer_toggle,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(itemTitle);

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(activityTitle);

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }
        };
        //Seteamos la escucha
        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void Obtener(){


    firebasedatos.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {

                                                String[] id = {"claseActual", "clasePendiente", "horPendientes", "horProgramados"};

                                                if (snapshot.child(id[2]).exists()) {
                                                    String ClasePendiente, ClaseActual;
                                                    ClaseActual = snapshot.child(id[0]).getValue().toString();
                                                    ClasePendiente = snapshot.child(id[1]).getValue().toString();

                                                    int i;
                                                    for (i = 1; i < tagTitles.length; i++) {
                                                        HorariosDisponibles[i - 1] = snapshot.child(id[2]).child(Integer.toString(i - 1)).getValue().toString();

                                                    }
                                                    for (i = 0; i < Integer.parseInt(ClasePendiente) - Integer.parseInt(ClaseActual); i++) {
                                                        HorariosPendientes[i] = snapshot.child(id[3]).child(Integer.toString(i + 1)).getValue().toString();

                                                    }
                                                    args.putString("claseActual", ClaseActual);
                                                    args.putStringArray("horProgramados", HorariosPendientes);
                                                    args.putStringArray("horPendientes", HorariosDisponibles);
                                                    args.putString("clasePendiente", ClasePendiente);


                                                }

                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {
                                            }
                                        }


    );

}
    /* La escucha del ListView en el Drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        // Reemplazar el contenido del layout principal por un fragmento
        FragmentManager fragmentManager;
        Fragment fragment;
        fragmentManager = getSupportFragmentManager();
        switch(position)
        {
            case 1:
                fragment=new InicioFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                fragment.setArguments(args);
                drawerList.setItemChecked(position, true);
                setTitle(tagTitles[position-1]);
                drawerLayout.closeDrawer(drawerList);
                break;
            case 2:
                fragment=new CuentaFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                drawerList.setItemChecked(position, true);
                setTitle(tagTitles[position-1]);
                drawerLayout.closeDrawer(drawerList);
                break;
            case 3:
                fragment=new ClasesProgramadasFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                drawerList.setItemChecked(position, true);
                setTitle(tagTitles[position-1]);
                drawerLayout.closeDrawer(drawerList);
                break;
            case 4:
                fragment=new NotasFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                drawerList.setItemChecked(position, true);
                setTitle(tagTitles[position-1]);
                drawerLayout.closeDrawer(drawerList);
                break;
            case 5:
                fragment=new BibliografiaFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                drawerList.setItemChecked(position, true);
                setTitle(tagTitles[position-1]);
                drawerLayout.closeDrawer(drawerList);
                break;
            case 6:
                fragment=new AcercaDeFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                drawerList.setItemChecked(position, true);
                setTitle(tagTitles[position-1]);
                drawerLayout.closeDrawer(drawerList);
                break;
        }

        // Se actualiza el item seleccionado y el título, después de cerrar el drawer

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            // Toma los eventos de selección del toggle aquí
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Método auxiliar para setear el titulo de la action bar */
    @Override
    public void setTitle(CharSequence title) {
        itemTitle = title;
        getSupportActionBar().setTitle(itemTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sincronizar el estado del drawer
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cambiar las configuraciones del drawer si hubo modificaciones
        drawerToggle.onConfigurationChanged(newConfig);
    }

}



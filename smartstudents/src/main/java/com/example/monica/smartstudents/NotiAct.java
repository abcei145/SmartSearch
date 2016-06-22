package com.example.monica.smartstudents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by monica on 11/05/2016.
 */
public class NotiAct extends Fragment {

    public NotiAct (){
        InicioFragment fragment= new InicioFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}

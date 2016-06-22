package com.example.monica.smartstudents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PeluchitosDatos extends SQLiteOpenHelper{
    private Cursor c;
    public PeluchitosDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//elimina la anterior con drop
        db.execSQL("create table Peluchitos (id text not null, nombre text not null, cantidad integer not null, valor integer not null, ganancia integer not null, primary key (id))");
        //crea
       c = db.rawQuery("select * from Peluchitos where", null);
        if (c.moveToFirst() == false){
            ContentValues registro = new ContentValues();//Es para guardar los datos ingresados
            registro.put("id", "3");//tag debe aparecer igual que en la clase BaseDeDatos
            registro.put("nombre", "Capitan_America");
            registro.put("cantidad", 10);
            registro.put("valor", 15000);
            registro.put("ganancia", 0);
            db.insert("Peluchitos", null, registro);
        }


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists estudiantes");//elimina la anterior con drop
        db.execSQL("create table estudiantes(identificacion integer primary key ,nombre text , nota integer)");
    }
}
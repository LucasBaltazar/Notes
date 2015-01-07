package com.lbttecnology.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by irene on 24/12/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "Notes";
    private static int VERSION = 1;


    public static class Notes{
        public static final String TABLE = "notes";
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String CONCLUDED = "concluded";
        public static final String[] COLUMNS = new String[]{_ID, TITLE, DESCRIPTION, CONCLUDED};
    }

    

    public DatabaseHelper(Context context){ super(context, DATABASE, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes (_id integer primary key, title text, description text, concluded integer default 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

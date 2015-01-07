package com.lbttecnology.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irene on 24/12/2014.
 */
public class DAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DAO(Context context){helper = new DatabaseHelper(context);}

    public SQLiteDatabase getDb(){
        if (db==null){
            db = helper.getWritableDatabase();
        }
        return db;
    }

    private void close(){
        helper.close();
    }

    public List<Notes> dataList(boolean concludeCheck){
        Cursor cursor;
        if (concludeCheck) {
            cursor = getDb().query(DatabaseHelper.Notes.TABLE, DatabaseHelper.Notes.COLUMNS, "concluded = ?", new String[]{"1"}, null, null, null);
        }else{
            cursor = getDb().query(DatabaseHelper.Notes.TABLE, DatabaseHelper.Notes.COLUMNS, "concluded = ?", new String[]{"0"}, null, null, null);
        }
        List<Notes> notes = new ArrayList<Notes>();
        while(cursor.moveToNext()){
            Notes note = createNote(cursor);
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    private Notes createNote(Cursor cursor){
        Notes note = new Notes(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Notes._ID)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.Notes.TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Notes.DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Notes.CONCLUDED)));
        return note;
    }

    public long insert(Notes note){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Notes.TITLE, note.getTitle());
        values.put(DatabaseHelper.Notes.DESCRIPTION, note.getDescription());
        return getDb().insert(DatabaseHelper.Notes.TABLE, null, values);
    }

    public long insertConcluded(Notes note){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Notes.TITLE, note.getTitle());
        values.put(DatabaseHelper.Notes.DESCRIPTION, note.getDescription());
        values.put(DatabaseHelper.Notes.CONCLUDED, 1);
        return getDb().insert(DatabaseHelper.Notes.TABLE, null, values);
    }

    public Notes searchNoteById(String id){
        Cursor cursor = getDb().rawQuery("select _id, title, description, concluded from "+DatabaseHelper.Notes.TABLE+" where _id = ?", new String[]{id});
        cursor.moveToFirst();
        Notes note = new Notes(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Notes._ID)),  cursor.getString(cursor.getColumnIndex(DatabaseHelper.Notes.TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Notes.DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Notes.CONCLUDED)));
        return note;
    }

    public long update(Notes note){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Notes.TITLE, note.getTitle());
        values.put(DatabaseHelper.Notes.DESCRIPTION, note.getDescription());
        return getDb().update(DatabaseHelper.Notes.TABLE, values, DatabaseHelper.Notes._ID + " = ?", new String[]{note.getId().toString()});
    }

    public long updateConcluded(Notes note){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Notes.TITLE, note.getTitle());
        values.put(DatabaseHelper.Notes.DESCRIPTION, note.getDescription());
        values.put(DatabaseHelper.Notes.CONCLUDED, 1);
        return getDb().update(DatabaseHelper.Notes.TABLE, values, DatabaseHelper.Notes._ID+" = ?", new String[]{note.getId().toString()});
    }

    public boolean remove(String id){
        String whereClause = DatabaseHelper.Notes._ID+" = ?";
        String[] whereArgs = new String[]{id};
        int removed = getDb().delete(DatabaseHelper.Notes.TABLE, whereClause, whereArgs);
        return removed > 0;
    }

}

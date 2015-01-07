package com.lbttecnology.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lbttecnology.persistence.DAO;
import com.lbttecnology.persistence.DatabaseHelper;
import com.lbttecnology.persistence.Notes;

/**
 * Created by irene on 25/12/2014.
 */
public class NewNote extends Activity {

    private EditText title, description;
    private DAO dao;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        dao = new DAO(this);

        title = (EditText) findViewById(R.id.titleN);
        description = (EditText) findViewById(R.id.descriptionN);

        id = getIntent().getStringExtra(DatabaseHelper.Notes._ID);

        if(id!=null){
            startUpdate();
        }

    }

    public void menuButton(View view){
        Button rButton = (Button) view;
        String option = "Opção: "+rButton.getText().toString();

        switch (rButton.getId()){
            case R.id.button1: startActivity(new Intent(this, NotesActivity.class));
                break;
            case R.id.button2: startActivity(new Intent(this, NewNote.class));
                break;
            case R.id.button3: startActivity(new Intent(this, NotesActivity.class));
                break;
        }

        Toast.makeText(this, option, Toast.LENGTH_SHORT).show();
    }

    public void saveNote(View view){
        Notes note;

        long received;

        if (id==null) {
            note = new Notes(title.getText().toString(), description.getText().toString());
            received = dao.insert(note);
        }else {
            note = new Notes(Long.valueOf(id), title.getText().toString(), description.getText().toString(), 1);
            received = dao.update(note);
        }
        if(received != -1){
            Toast.makeText(this, getString(R.string.savedNote), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getString(R.string.errorSaved), Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(this, NotesActivity.class));
    }

    public void concludeNote(View view){
        Notes note;

        long received;

        if (id==null) {
            note = new Notes(title.getText().toString(), description.getText().toString());
            received = dao.insertConcluded(note);
        } else{
            note = new Notes(Long.valueOf(id), title.getText().toString(), description.getText().toString(), 1);
            received = dao.updateConcluded(note);
        }
        if(received != -1){
            Toast.makeText(this, getString(R.string.savedConcludedNote), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getString(R.string.errorSaved), Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(this, NotesActivity.class));
    }

    private void startUpdate(){
        Notes note = dao.searchNoteById(id);

        title.setText(note.getTitle());
        description.setText(note.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish();
        return true;
    }
}

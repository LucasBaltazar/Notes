package com.lbttecnology.notes;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbttecnology.persistence.DAO;
import com.lbttecnology.persistence.DatabaseHelper;
import com.lbttecnology.persistence.Notes;


public class NotesActivity extends ListActivity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener{

    private List<Map<String, Object>> data;
    private DAO dao;
    private Intent intent;
    private String selectedId;
    private boolean concluded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new DAO(this);


        concluded = getIntent().getBooleanExtra(DatabaseHelper.Notes.CONCLUDED, false);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout menu = (LinearLayout) inflater.inflate(R.layout.menu, null);
        String[] from = {DatabaseHelper.Notes.TITLE, DatabaseHelper.Notes.DESCRIPTION};
        int[] to = {R.id.title, R.id.description};

        SimpleAdapter adapter = new SimpleAdapter(this, dataList(), R.layout.activity_notes, from, to);
        setListAdapter(adapter);
        getListView().addHeaderView(menu);
        getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
    }

    private List<Map<String, Object>> dataList(){

        data = new ArrayList<Map<String, Object>>();

        List<Notes> notesList = dao.dataList(concluded);

        for (Notes note: notesList){
            Map<String, Object> item = new HashMap<String, Object>();

            item.put(DatabaseHelper.Notes._ID, note.getId());
            item.put(DatabaseHelper.Notes.TITLE, note.getTitle());
            item.put(DatabaseHelper.Notes.DESCRIPTION, note.getDescription());

            data.add(item);
        }
        return data;
    }


    public void menuButton(View view){
        Button rButton = (Button) view;
        String option = "Opção: "+rButton.getText().toString();

        switch (rButton.getId()){
            case R.id.button1: startActivity(new Intent(this, NotesActivity.class));
                break;
            case R.id.button2: startActivity(new Intent(this, NewNote.class));
                break;
            case R.id.button3: intent = new Intent(this, NotesActivity.class);
                intent.putExtra(DatabaseHelper.Notes.CONCLUDED, true);
                startActivity(intent);
                break;
        }

        Toast.makeText(this, option, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        intent = new Intent(this, NewNote.class);
        intent.putExtra(DatabaseHelper.Notes._ID, this.selectedId);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = position-1;
        selectedId = data.get(pos).get(DatabaseHelper.Notes._ID).toString();
        intent = new Intent(this, NewNote.class);
        intent.putExtra(DatabaseHelper.Notes._ID, selectedId);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String id;
        int index = info.position-1;
        if (R.id.remove == item.getItemId()) {
            id = data.get(index).get(DatabaseHelper.Notes._ID).toString();
            boolean check = dao.remove(id);
            data.remove(index);
            if (check) {
                Toast.makeText(this, "Opção: " + item.getTitle().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Um erro ocorreu!", Toast.LENGTH_SHORT).show();
            }
            getListView().invalidateViews();
            return true;
        }else if(R.id.edit == item.getItemId()) {
            id = data.get(index).get(DatabaseHelper.Notes._ID).toString();
            intent = new Intent(this, NewNote.class);
            Toast.makeText(this, "Opção: " + item.getTitle().toString(), Toast.LENGTH_SHORT).show();
            intent.putExtra(DatabaseHelper.Notes._ID, id);
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
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

package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.addNote);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.addNote:
                        return true;
                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(),AddTaskActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NotesDatabase db = new NotesDatabase(this);
        notes = db.getNotes();
        recyclerView = findViewById(R.id.listOfNotes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addNote){
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
            Toast.makeText(this, "Add button is Clicked", Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.sortByDate){

            Toast.makeText(this, "Sort By Date", Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.sortByDateAsc){
            NotesDatabase db = new NotesDatabase(this);
            notes = db.getNotesAsc();
            recyclerView = findViewById(R.id.listOfNotes);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, notes);

            recyclerView.setAdapter(adapter);
        }

        if(item.getItemId() == R.id.sortByDateDesc){
            NotesDatabase db = new NotesDatabase(this);
            notes = db.getNotes();
            recyclerView = findViewById(R.id.listOfNotes);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, notes);

            recyclerView.setAdapter(adapter);
        }

        if(item.getItemId() == R.id.sortByName){

            Toast.makeText(this, "Sort By Name", Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.sortByNameAsc){
            NotesDatabase db = new NotesDatabase(this);
            notes = db.getNotes();

            Collections.sort(notes, new CustomComparatorTitle());

            recyclerView = findViewById(R.id.listOfNotes);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, notes);

            recyclerView.setAdapter(adapter);
        }

        if(item.getItemId() == R.id.sortByNameDesc){
            NotesDatabase db = new NotesDatabase(this);
            notes = db.getNotes();

            Collections.sort(notes, new CustomComparatorTitle().reversed());

            recyclerView = findViewById(R.id.listOfNotes);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, notes);

            recyclerView.setAdapter(adapter);
        }

        return super.onOptionsItemSelected(item);
    }

    private class CustomComparatorTitle implements Comparator<Note>{

        @Override
        public int compare(Note o1, Note o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }
}
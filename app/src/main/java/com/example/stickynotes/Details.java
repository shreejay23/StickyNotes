package com.example.stickynotes;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class  Details extends AppCompatActivity {
    long id;
    TextView mDetails;
    NotesDatabase db;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDetails = findViewById(R.id.detailsOfNote);

        Intent i = getIntent();
        id = i.getLongExtra("ID", 0);

        db = new NotesDatabase(this);
        note = db.getNote(id);

        getSupportActionBar().setTitle(note.getTitle());

        mDetails.setText(note.getContent());
        mDetails.setMovementMethod(new ScrollingMovementMethod());


        //Toast.makeText(this, "ID -> " +id, Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                db.deleteNote(note.getID());
                startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.editNote){
            Toast.makeText(this,"Edit Note", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, Edit.class);
            i.putExtra("ID", note.getID());
            startActivity(i);
        }
        else if(item.getItemId() == R.id.back){
            goToMain();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this, AddNoteActivity.class);
        startActivity(i);
    }

}
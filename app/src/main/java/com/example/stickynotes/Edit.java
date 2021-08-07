package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Edit extends AppCompatActivity {

    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    CharSequence ttle;
    long id;
    NotesDatabase db;
    Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent i = getIntent();
        id = i.getLongExtra("ID", 0);

        db = new NotesDatabase(this);
        note = db.getNote(id);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(note.getTitle());

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

        noteTitle.setText(note.getTitle());
        noteDetails.setText(note.getContent());


        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() !=0){
                    ttle = charSequence;
                    getSupportActionBar().setTitle(charSequence);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ttle.length()==0){
                    getSupportActionBar().setTitle("Add Title");
                }

            }
        });

        // Get current date & time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));

        Log.d("calender", "Date & Time : " + todaysDate + " and " + currentTime);


    }

    private String pad(int i){
        if(i<10)
            return "0"+i;
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete){
            Toast.makeText(this,"Note Cancelled", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(item.getItemId() == R.id.save){
            if(noteTitle.getText().length()!=0) {
                note.setTitle(noteTitle.getText().toString());
                note.setContent(noteDetails.getText().toString());

                int id = db.editNote(note);
                if(id== note.getID()){
                    Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent(getApplicationContext(), Details.class);
                i.putExtra("ID", note.getID());
                startActivity(i);

            }
            else {
                noteTitle.setError("Title can't be empty");
            }
        }
        if(item.getItemId() == R.id.back){
            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this, AddNoteActivity.class);
        startActivity(i);
    }




}
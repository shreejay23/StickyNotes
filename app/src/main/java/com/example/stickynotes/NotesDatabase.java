package com.example.stickynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notesdb";
    private static final String DATABASE_TABLE = "notestable";



    // Column names of Database
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_EMAIL = "email";

    String uEmail;


    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE "+ DATABASE_TABLE + "(" + KEY_ID +" INTEGER PRIMARY KEY,"+
                KEY_TITLE + " TEXT,"+
                KEY_CONTENT + " TEXT,"+
                KEY_DATE + " TEXT,"+
                KEY_TIME + " TEXT,"+
                KEY_EMAIL + " TEXT" + ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);

    }

    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());
        c.put(KEY_EMAIL, note.getEmail());

        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "ID -> " +ID);
        return ID;

    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME, KEY_EMAIL}, KEY_ID +"=?",
                new String[]{ String.valueOf(id)}, null, null, null, null);

        if (cursor!=null)
            cursor.moveToFirst();

        Note note = new Note(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        return  note;


    }



    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();

        uEmail = ProfileActivity.getUEmail();

        String query = "SELECT * FROM " +DATABASE_TABLE+ " WHERE " + KEY_EMAIL + " = \'" + uEmail + "\' ORDER BY " +KEY_ID+ " DESC";//"WHERE " + KEY_EMAIL + " = " + uEmail +

        Log.d(uEmail,"");
        //Toast.makeText(c,"Email" + uEmail,Toast.LENGTH_SHORT).show();

        Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    //if(temp.equals(uEmail)) {
                        Note note = new Note();
                        note.setID(cursor.getLong(0));
                        note.setTitle(cursor.getString(1));
                        note.setContent(cursor.getString(2));
                        note.setDate(cursor.getString(3));
                        note.setTime(cursor.getString(4));
                        note.setEmail(cursor.getString(5));

                        allNotes.add(note);
                    //}

                } while (cursor.moveToNext());
            }

        return allNotes;

    }

    public List<Note> getNotesAsc(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();

        uEmail = ProfileActivity.getUEmail();

        String query = "SELECT * FROM " +DATABASE_TABLE+ " WHERE " + KEY_EMAIL + " = \'" + uEmail + "\' ORDER BY " +KEY_ID+ " ASC";//"WHERE " + KEY_EMAIL + " = " + uEmail +

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                //if(cursor.getString(5).equals(uEmail)) {

                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));

                allNotes.add(note);
                //}

            } while (cursor.moveToNext());
        }

        return allNotes;

    }

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "edited Title : ->"+note.getTitle()+"\n ID -> "+note.getID());
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());
        return db.update(DATABASE_TABLE, c, KEY_ID+"=?", new String[]{String.valueOf(note.getID())});
    }

    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID +"=?", new String[]{String.valueOf(id)});
        db.close();
    }


}

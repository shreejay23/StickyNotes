package com.example.stickynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "taskdb";
    private static final String DATABASE_TABLE = "tasktable";

    // Column names of Database
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STATUS = "status";

    private SQLiteDatabase db;

    String uEmail;

    public TaskDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + DATABASE_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_TIME + " TEXT," +
                KEY_EMAIL + " TEXT," +
                KEY_STATUS + " TEXT" + ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public long insertTask(Task task){
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, task.getTitle());
        c.put(KEY_DATE, task.getDate());
        c.put(KEY_TIME, task.getTime());
        c.put(KEY_EMAIL, task.getEmail());
        c.put(KEY_STATUS, 0);

        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "ID -> " +ID);
        return ID;
    }

    public Task getTask(long id){
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_TIME, KEY_EMAIL, KEY_STATUS}, KEY_ID + "=?",
                new String[]{ String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));

        return task;
    }



    public List<Task> getTasks(){
        //SQLiteDatabase db = this.getReadableDatabase();
        List<Task> allTasks = new ArrayList<>();

        uEmail = ProfileActivity.getUEmail();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + " = \'" + uEmail + "\' ORDER BY " + KEY_ID + " DESC";

            Log.d(uEmail, "");

            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));

                    allTasks.add(task);
                } while (cursor.moveToNext());
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return allTasks;
    }

    public void updateStatus(long ID, int status) {
        //SQLiteDatabase db = this.getReadableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_STATUS, status);
        db.update(DATABASE_TABLE, c, KEY_ID + "=?", new String[]{String.valueOf(ID)});
    }

    public int updateTask(long id, String title){
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, title);
        Log.d("Edited", "edited Title : ->" + title + "\n ID -> " + id);
        //c.put(KEY_TITLE, task.getTitle());
        //c.put(KEY_DATE, task.getDate());
        //c.put(KEY_TIME, task.getTime());
        //c.put(KEY_STATUS, task.getStatus());
        return db.update(DATABASE_TABLE, c, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    void deleteTask(long id){
        //SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }
}

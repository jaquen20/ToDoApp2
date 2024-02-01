package com.example.todoapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {


    private  Context context;
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
     static String TODO_TABLE="todo";

    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";


    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;


    public DataBase(Context context) {
        super(context, NAME, null, VERSION);
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
//        String s1 = sharedPreferences.getString("user_name", "todo");
//        String[] parts = s1.split("@");
//
//        if (parts.length > 1) {
//
//
//            TODO_TABLE = parts[0] + "_todo";
//           // TODO_TABLE = parts[0];
//        } else {
//            TODO_TABLE="todo";
//        }

    }




    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
//                + STATUS + " INTEGER)");
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
//        String s1 = sharedPreferences.getString("user_name", "todo");
//        String[] parts = s1.split("@");
//        TODO_TABLE = parts[0];
//
//        if (parts.length > 1) {
//
//
//            TODO_TABLE = parts[0] + "_todo";
//            // TODO_TABLE = parts[0];
//        } else {
//            TODO_TABLE="todo";
//        }
//       String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
//                + STATUS + " INTEGER)";


        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(DataModel task){

        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<DataModel> getAllTasks(){
        List<DataModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        DataModel task = new DataModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            if (cur != null) {
                // Your code when 'cur' is not
                cur.close();
            } else {
                // Handle the case when 'cur' is null
                Log.e("MyApp", "Cursor is null. Handle this case.");
            }
           // cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
//    public void createUserTable(String username) {
//        String table;
//
//        String[] parts = username.split("@");
//
//        if (parts.length > 1) {
//
//
//            table = parts[0] + "_todo";
//            // TODO_TABLE = parts[0];
//        } else {
//            table = "todo";
//        }
//        if (!tableExists(db, table)) {
//
//            String createTableQuery = "CREATE TABLE " + table + " ("
//                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "task TEXT, "
//                    + "status INTEGER)";
//            db = getWritableDatabase();
//            db.execSQL(createTableQuery);
//        }
//    }
//    public boolean tableExists(SQLiteDatabase db, String tableName) {
//        Cursor cursor = null;
//        try {
//            // Query the SQLite "sqlite_master" table for the table name
//            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] { tableName });
//
//            // If a row is returned, the table exists
//            return cursor.getCount() > 0;
//        } catch (Exception e) {
//            // Handle any exceptions or errors here
//            e.printStackTrace();
//            return false;
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }



}

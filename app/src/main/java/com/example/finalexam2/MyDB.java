package com.example.finalexam2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

public class MyDB {

    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;


    public final static String CON_TABLE = "Tasks"; // name of table
    public final static String CON_IMAGE = "image";// image set to the task
    public final static String CON_ID = "_id"; // id
    public final static String CON_NAME = "name";  // name
    public final static String CON_DATE = "date";
    public final static String CON_DONE = "done";

    /**
     * @param context
     */
    public MyDB(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.onCreate(database);//Per si volem reinciar la database
    }


    public long createRecords(byte[] image, String name, String date, String done) {
        int id = nextId();
        ContentValues values = new ContentValues();
        values.put(CON_ID, id);
        values.put(CON_IMAGE, image);
        values.put(CON_NAME, name);
        values.put(CON_DATE, date);
        values.put(CON_DONE, done);
        return database.insert(CON_TABLE, null, values);
    }

    public long updateRecords(int id, byte[] image, String name, String date, String done) {
        ContentValues cv = new ContentValues();
        cv.put("image", image);
        cv.put("name", name);
        cv.put("date", date);
        cv.put("done", done);
        return database.update(CON_TABLE, cv, "_id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteItem(int s) {
        database.delete(CON_TABLE, "_id = ?", new String[]{String.valueOf(s)});
    }

    public Cursor selectRecords(boolean b) {
        String[] cols = new String[]{CON_ID, CON_IMAGE, CON_NAME, CON_DATE, CON_DONE};
        Cursor mCursor;
        if (b) {
            mCursor = database.query(true, CON_TABLE, cols, null
                    , null, null, null, "date asc", null);
        } else {
            mCursor = database.query(true, CON_TABLE, cols, "done = ?"
                    , new String[]{"false"}, null, null, "date asc", null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    private int nextId() {
        int id;
        try {
            String[] cols = new String[]{CON_ID};
            Cursor mCursor = database.query(true, CON_TABLE, cols, null
                    , null, null, null, "_id desc", null);
            mCursor.moveToFirst();
            id = mCursor.getInt(0) + 1;
            return id;
        }catch (CursorIndexOutOfBoundsException e){
            id=0;
        }
        return id;
    }
}
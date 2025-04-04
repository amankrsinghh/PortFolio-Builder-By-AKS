package com.example.pfbuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class pfHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pfbuilder.db";
    private static final int DATABASE_VERSION = 1;

Context context;
    public pfHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE Users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, job TEXT, location TEXT, email TEXT, phone TEXT, about TEXT, skills TEXT, image TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public void insertUser(String name, String job, String location, String email, String phone, String about, String skills, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("job", job);
        values.put("location", location);
        values.put("email", email);
        values.put("phone", phone);
        values.put("about", about);
        values.put("skills", skills);
        values.put("image", image);
        long data = db.insert("Users", null, values);
        if (data == -1) {
            Toast.makeText(context, "Failed to Create PortFolio", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "PortFolio Created Successfully", Toast.LENGTH_SHORT).show();

        }
        db.close();
    }

    public ArrayList<pfmodel> getAllUsers() {
        ArrayList<pfmodel> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    userList.add(new pfmodel(
                            cursor.getInt(0),  // Fetch ID
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8)
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return userList;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Users", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean updateUser(int id, String name, String job, String location, String email, String phone, String about, String skills, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("job", job);
        values.put("location", location);
        values.put("email", email);
        values.put("phone", phone);
        values.put("about", about);
        values.put("skills", skills);
        values.put("image", image);

        int result = db.update("Users", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }



}

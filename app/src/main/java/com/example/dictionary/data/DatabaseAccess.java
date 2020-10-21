package com.example.dictionary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<Word> getWords() {
        List<Word> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet limit 20", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Word word = new Word(cursor.getString(1), cursor.getString(2));
            list.add(word);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<Word> getWords(int size, int limit) {
        int nextLimit = size + limit;
        List<Word> list = new ArrayList<>();
        Cursor cursor;
        cursor = database.rawQuery("SELECT * FROM anh_viet limit " + nextLimit, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Word word = new Word(cursor.getString(1), cursor.getString(2));
            list.add(word);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public ArrayList<String> getWords(String filter) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word like " +
                filter + "% limit 10", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getDefinition(String word) {
        String definition = "";
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word = "
        + word + "", null);
        cursor.moveToFirst();
        definition = cursor.getString(2);
        cursor.close();
        return definition;
    }

}

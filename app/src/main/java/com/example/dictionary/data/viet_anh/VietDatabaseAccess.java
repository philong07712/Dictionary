package com.example.dictionary.data.viet_anh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dictionary.model.Word;
import com.example.dictionary.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class VietDatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static VietDatabaseAccess instance;
    private final int TYPE = Constants.WORD.VIET_TYPE;
    private VietDatabaseAccess(Context context) {
        this.openHelper = new VietDatabaseOpenHelper(context);
    }

    public static VietDatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new VietDatabaseAccess(context);
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
        Cursor cursor = database.rawQuery("SELECT * FROM viet_anh limit 20", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Word word = new Word(TYPE,
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));
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
        cursor = database.rawQuery("SELECT * FROM viet_anh limit " + nextLimit, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Word word = new Word(TYPE,
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));
            list.add(word);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<Word> getWords(String filter) {
        ArrayList<Word> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM viet_anh where word like '% " +
                filter + " %' limit 20", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Word word = new Word(TYPE,
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2));
            list.add(word);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Word getWordById(int id) {
        Cursor cursor = database.rawQuery("SELECT * FROM viet_anh where id like " + id + "", null);
        cursor.moveToFirst();
        Word word = new Word(TYPE,
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2));
        cursor.close();
        return word;
    }

    public String getDefinition(String word) {
        String definition = "";
        Cursor cursor = database.rawQuery("SELECT * FROM viet_anh where word = "
        + word + "", null);
        cursor.moveToFirst();
        definition = cursor.getString(2);
        cursor.close();
        return definition;
    }

    public List<Word> getFavorite() {
        List<Word> wordList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT viet_anh.id, word, content FROM viet_anh inner join favorite on viet_anh.id = favorite.id", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            wordList.add(new Word(TYPE,
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return wordList;
    }

    public void addFavorite(int id) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        database.insert("favorite", null, values);
        database.close();
        Log.i("TAG", "addFavorite: ");
    }

}

package com.example.dictionary.data;

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

public class EngDatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static EngDatabaseAccess instance;
    private final int TYPE = Constants.WORD.ENG_TYPE;
    private EngDatabaseAccess(Context context) {
        this.openHelper = new EngDatabaseOpenHelper(context);
    }

    public static EngDatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new EngDatabaseAccess(context);
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
        cursor = database.rawQuery("SELECT * FROM anh_viet limit " + nextLimit, null);
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
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word like '% " +
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
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where id like " + id + "", null);
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
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet where word = "
        + word + "", null);
        cursor.moveToFirst();
        definition = cursor.getString(2);
        cursor.close();
        return definition;
    }

    public List<Word> getFavorite() {
        List<Word> wordList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT anh_viet.id, word, content FROM anh_viet inner join favorite on anh_viet.id = favorite.id", null);
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

    public Word getYourWord() {
        Word word;
        Cursor cursor = database.rawQuery("SELECT * FROM anh_viet limit 1 OFFSET" +
                " ABS(RANDOM()) % MAX((SELECT COUNT(*) FROM anh_viet), 1)", null);
        cursor.moveToFirst();
        word = new Word(TYPE,
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2));
        cursor.close();
        return word;
    }

}

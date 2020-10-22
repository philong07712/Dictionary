package com.example.dictionary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class EngDatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "anh_viet.db";
    private static final int DATABASE_VERSION = 1;

    public EngDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}

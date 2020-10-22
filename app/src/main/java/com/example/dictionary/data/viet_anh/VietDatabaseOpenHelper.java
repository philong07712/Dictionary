package com.example.dictionary.data.viet_anh;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class VietDatabaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "viet_anh.db";
    private static final int DATABASE_VERSION = 1;

    public VietDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}

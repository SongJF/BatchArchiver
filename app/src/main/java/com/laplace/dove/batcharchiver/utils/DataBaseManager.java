package com.laplace.dove.batcharchiver.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DataBaseManager {
    public static <T, U> Map<T,U> mapQuery(
            String dbPath, String quetyStr, Function<Cursor, Map<T,U>> cursorFunc){
        if (dbPath == null || quetyStr == null || cursorFunc ==null){
            return new HashMap<>();
        }
        if (!dbPath.endsWith(".db")){
            return new HashMap<>();
        }


        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        try (Cursor cursor = db.rawQuery(quetyStr, null)) {
            return cursorFunc.apply(cursor);
        }
    }
}

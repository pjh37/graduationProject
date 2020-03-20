package com.example.graduationproject.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WidgetListDBManager {
    static final String DB_NAME = "WidgetSurveyList.db";
    static final String TABLE_NAME = "List";
    static final int DE_VERSION = 1;

    Context context = null;
    private static WidgetListDBManager dbManager= null;
    private SQLiteDatabase database = null;

    public static WidgetListDBManager getInstance(Context context){
        if(dbManager == null){
            dbManager = new WidgetListDBManager(context);
        }

        return dbManager;
    }

    private WidgetListDBManager(Context context){
        this.context = context;
        database = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "title TEXT," + "response INTEGER," + "time STRING," + "expect INTEGER);");
    }
}

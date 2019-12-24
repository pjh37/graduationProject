package com.example.graduationproject.createform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FormSaveManager {
    private static final String DB_FORM="forms.db";
    private static final String TABLE_FORMS="forms";
    private static final int DB_VERSION=1;
    private static FormSaveManager formSaveManager=null;
    private SQLiteDatabase mDatabase=null;
    Context mContext=null;
    private FormSaveManager(Context context){
        mContext=context;
        mDatabase=mContext.openOrCreateDatabase(DB_FORM,mContext.MODE_PRIVATE,null);
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS "+TABLE_FORMS+
                        "("+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        "json TEXT, "+
                        "time TEXT);"
        );

    }
    public static FormSaveManager getInstance(Context context){
        if(formSaveManager==null){
            formSaveManager=new FormSaveManager(context);
        }
        return formSaveManager;
    }
    public long insert(ContentValues addRowValue){
        return mDatabase.insert(TABLE_FORMS,null,addRowValue);
    }
    public Cursor query(String[] colums, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
        return mDatabase.query(TABLE_FORMS,colums,selection,selectArgs,groupBy,having,orderBy);
    }
    public int update( ContentValues updateRowValue,
                       String whereClause,
                       String[] whereArgs ){
        return mDatabase.update( TABLE_FORMS, updateRowValue, whereClause, whereArgs );
    }
    public int delete( String whereClause,
                       String[] whereArgs ){
        return mDatabase.delete( TABLE_FORMS,
                whereClause, whereArgs);
    }
}

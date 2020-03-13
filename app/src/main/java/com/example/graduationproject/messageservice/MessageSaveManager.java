package com.example.graduationproject.messageservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduationproject.form.FormSaveManager;

public class MessageSaveManager {
    private static final String DB_FORM="forms.db";
    private static final String TABLE_NAME="message";
    private static final int DB_VERSION=1;
    private static MessageSaveManager messageSaveManager=null;
    private SQLiteDatabase mDatabase=null;

    Context mContext=null;
    private MessageSaveManager(Context context){
        mContext=context;
        mDatabase=mContext.openOrCreateDatabase(DB_FORM,mContext.MODE_PRIVATE,null);
        mDatabase.execSQL(
                "create TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                        "_id integer primary key autoincrement,"+
                        "chatRoomID TEXT,"+
                        "userEmail TEXT,"+
                        "message TEXT,"+
                        "messageType integer,"+
                        "date TEXT,"+
                        "fileUrl TEXT);"

        );

    }
    public static MessageSaveManager getInstance(Context context){
        if(messageSaveManager==null){
            messageSaveManager=new MessageSaveManager(context);
        }
        return messageSaveManager;
    }
    public long insert(MessageDTO messaage){
        ContentValues contentValues=new ContentValues();
        contentValues.put("chatRoomID",messaage.getChatRoomID());
        contentValues.put("messageType",messaage.getMessageType());
        contentValues.put("userEmail",messaage.getUserEmail());
        contentValues.put("message",messaage.getMessage());
        contentValues.put("fileUrl",messaage.getFileUrl());
        contentValues.put("date",messaage.getDate());
        return mDatabase.insert(TABLE_NAME,null,contentValues);
    }
}

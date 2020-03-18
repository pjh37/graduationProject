package com.example.graduationproject.messageservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduationproject.form.FormSaveManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageSaveManager {
    private static final String DB_FORM="checkmate.db";
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
                        "roomKey TEXT,"+
                        "userEmail TEXT,"+
                        "message TEXT,"+
                        "date TEXT,"+
                        "isRead integer);"

        );

    }
    public static MessageSaveManager getInstance(Context context){
        if(messageSaveManager==null){
            messageSaveManager=new MessageSaveManager(context);
        }
        return messageSaveManager;
    }
    public Cursor query(String[] colums, String selection, String[] selectArgs, String groupBy, String having, String orderBy,String limit){
        return mDatabase.query(TABLE_NAME,colums,selection,selectArgs,groupBy,having,orderBy,limit);
    }
    public long insert(MessageDTO messaage){
        ContentValues contentValues=new ContentValues();
        contentValues.put("roomKey",messaage.getRoomKey());
        contentValues.put("userEmail",messaage.getUserEmail());
        contentValues.put("message",messaage.getMessage());
        contentValues.put("date",messaage.getDate());
        return mDatabase.insert(TABLE_NAME,null,contentValues);
    }
    public HashMap<String,ArrayList<MessageDTO>> findAllMessageById(){
        //key = chatRoomID
        HashMap<String,ArrayList<MessageDTO>> datas=new HashMap<>();

        String[] columns=new String[]{"_id","roomKey","userEmail","message","date","isRead"};
        String orderBy="date desc";
        Cursor cursor=query(columns,null,null,null,null,orderBy,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                try{
                    MessageDTO msg=new MessageDTO(cursor.getInt(0)
                            ,cursor.getString(1)
                            ,cursor.getString(2)
                            ,cursor.getString(3)
                            ,cursor.getString(4)
                            ,cursor.getInt(5));
                    if(datas.containsKey(msg.getRoomKey())){
                        datas.get(msg.getRoomKey()).add(msg);
                    }else{
                        ArrayList<MessageDTO> dto=new ArrayList<>();
                        dto.add(msg);
                        datas.put(msg.getRoomKey(),dto);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    cursor.close();
                }
            }
        }
        return datas;
    }

}

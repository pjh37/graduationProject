package com.example.graduationproject.messageservice;

import android.content.Context;
import android.util.Log;

import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;
import com.example.graduationproject.login.Session;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MessageManager {
    private static MessageManager messageManager=null;
    private static Socket mSocket;
    private Context mContext;
    private MessageManager(){ }
    private MessageManager(Context context){
        this.mContext=context;
    }
    public static MessageManager getInstance(Context context){
        if(messageManager==null){
            messageManager=new MessageManager(context);
        }
        return messageManager;
    }

    public void connect(){
        try{
            //http://192.168.35.42:8001
            //mContext.getString(R.string.baseUrl)
            mSocket= IO.socket(mContext.getString(R.string.baseUrl));
            mSocket.connect();
            Log.v("NetworkManager","socket connect ");
        }catch (Exception error){
            Log.v("NetworkManager","error.printStackTrace()");
            error.printStackTrace();
        }
    }

    public Socket getSocket(){
        return mSocket;
    }
    public static void login(){
        JSONObject jObject=new JSONObject();
        try{
            jObject.put("userEmail", Session.getUserEmail());
            mSocket.emit("login",jObject);
            Log.v("login","메세지 전송함!!!");
        }catch (Exception error){
            error.printStackTrace();
        }
    }
    public static void msgSend(MessageDTO msg){
        JSONObject jObject=new JSONObject();
        try{
            jObject.put("roomKey",msg.getRoomKey());
            jObject.put("userEmail", msg.getUserEmail());
            jObject.put("message", msg.getMessage());
            jObject.put("date", msg.getDate());

            mSocket.emit("message",jObject);
            Log.v("테스트","메세지 전송함!!!");
        }catch (Exception error){
            error.printStackTrace();
        }
    }
    public static void roomJoin(){
        JSONObject jObject=new JSONObject();
        try{
            jObject.put("userEmail", Session.getUserEmail());
            mSocket.emit("roomjoin",jObject);
            Log.v("messageSend","메세지 전송함!!!");
        }catch (Exception error){
            error.printStackTrace();
        }
    }
}

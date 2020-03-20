package com.example.graduationproject.messageservice;

import android.content.Context;
import android.util.Log;

import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MessageManager {
    private static MessageManager messageManager=null;
    private Socket mSocket;
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
            mSocket= IO.socket(mContext.getString(R.string.baseUrl));
            mSocket.connect();
            Log.v("NetworkManager","socket connect ");
        }catch (Exception error){
            Log.v("NetworkManager","error.printStackTrace()");
            error.printStackTrace();
        }
    }
}

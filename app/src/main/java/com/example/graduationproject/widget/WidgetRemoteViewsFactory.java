package com.example.graduationproject.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.graduationproject.R;

import java.util.ArrayList;

/**
 * 앱에서 listview를 사용하게 될 경우 활용
 * 4x1 size 에서는 listview를 사용하기 힘들 것 같아서 일단 보류
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private ArrayList<WidgetItem> itemArrayList;

    public WidgetRemoteViewsFactory(Context context){
        this.context = context;
    }

    public void setData(){
        itemArrayList = new ArrayList<>();
        itemArrayList.add(new WidgetItem(1, "test1"));
        itemArrayList.add(new WidgetItem(2, "test2"));
        itemArrayList.add(new WidgetItem(3, "test3"));
    }

    @Override
    public void onCreate(){
        setData();
    }

    @Override
    public void onDataSetChanged(){
        setData();
    }

    @Override
    public void onDestroy(){

    }

    @Override
    public int getCount(){
        return itemArrayList.size();
    }

    @Override
    public int getViewTypeCount(){
        return 1;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public RemoteViews getViewAt(int position){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgt_list_item);
        remoteViews.setTextViewText(R.id.widget_item_title, itemArrayList.get(position).getTitle());

        Intent intent = new Intent();
        intent.putExtra("item_id", itemArrayList.get(position).getId());
        intent.putExtra("item_title",itemArrayList.get(position).getTitle());
        remoteViews.setOnClickFillInIntent(R.id.widget_item_title, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView(){
        return null;
    }
}

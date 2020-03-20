package com.example.graduationproject.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * WidgetRemoteViewsFactory 사용 시 활용
 */
public class WidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }
}

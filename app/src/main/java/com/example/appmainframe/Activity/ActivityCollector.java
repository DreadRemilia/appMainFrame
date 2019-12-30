package com.example.appmainframe.Activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    //利用集合类存放Activity
    public static List<Activity> activities = new ArrayList<>();

    //添加Activity方法
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    //移除Activity方法
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    //关闭所有Activity方法
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}

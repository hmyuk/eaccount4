package com.isu.erp.eaccount.common;

import android.app.Activity;
import android.util.Log;

import com.isu.erp.eaccount.vo.UserVo;

import java.util.ArrayList;
import java.util.HashMap;

public class Singleton {
    private static final Singleton instance = new Singleton();

    private UserVo userVo;
    private HashMap<String,Object> columnDefine;

    private ArrayList<Activity> activityStack;

    private Singleton() {
        activityStack = new ArrayList<Activity>();
    }

    public static Singleton getInstance(){
        return instance;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public HashMap<String, Object> getColumnDefine() {
        return columnDefine;
    }

    public void setColumnDefine(HashMap<String, Object> columnDefine) {
        this.columnDefine = columnDefine;
    }

    public void addActivityStack(Activity activity){
        activityStack.add(activity);
    }

    public void removeActivityStack(Activity activity){
        for(int i = 0 ; i < activityStack.size(); i ++){
            if(activityStack.get(i) != null && activity.equals(activityStack.get(i))){
                activityStack.get(i).finish();
                activityStack.remove(i);
            }
        }
    }

    public void removeAllActivityStack(){
        for(int i = activityStack.size()-1 ; 0 <= i; i --){
            Log.d("eaccountLog","delete activity stack : "+ activityStack.get(i).getClass().getSimpleName()+" ...delete.......");

            if(activityStack.get(i) != null){
                activityStack.get(i).finish();
            }
            activityStack.remove(i);
        }

    }

    public ArrayList<Activity> getActivityStack(){
        return this.activityStack;
    }

}

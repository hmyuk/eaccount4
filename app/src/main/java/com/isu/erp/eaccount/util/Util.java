package com.isu.erp.eaccount.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.vo.UserVo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Response;

public class Util {

    private Context context;
    private Activity activity;

    public Util(Context context){
        this.context = context;
    }
    public Util(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    /**
     * @auther : yukpan
     * @type : event
     * @date : 2019.06.18
     * @explain : 로그인 유저 데이터를 저장소에 저장한다.
     **/
    public void setLoginData(HashMap<String,Object> userMap){
        SharedPreferences sf = context.getSharedPreferences("userData",context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sf.edit();
        editor.putString("tenantId", userMap.get("tenantId").toString());
        editor.putString("id", userMap.get("id").toString());
        editor.putString("password", userMap.get("password").toString());
        editor.putString("tenantKey", userMap.get("tenantKey").toString());
        editor.putString("gwPrefix", userMap.get("gwPrefix").toString());
        editor.putString("bukrs", userMap.get("BUKRS").toString());

        editor.commit();
    }

    /**
     * @auther : yukpan
     * @type : event
     * @date : 2019.06.18
     * @explain : 저장소에 저장되어있는 유저 데이터를 가져온다.
     **/
    public UserVo getLoginData(){
        UserVo userVo = new UserVo();

        SharedPreferences sf = context.getSharedPreferences("userData", context.MODE_PRIVATE);

        if(sf != null){

            userVo.setTenantId(sf.getString("tenantId", ""));
            userVo.setId(sf.getString("id",""));
            userVo.setPassword(sf.getString("password",""));
            userVo.setTenantKey(sf.getString("tenantKey",""));
            userVo.setGwPrefix(sf.getString("gwPrefix",""));
            userVo.setBukrs(sf.getString("bukrs",""));
        }


        return userVo;
    }

    public UserVo getUserVo(HashMap<String, Object> map){
        UserVo userVo = new UserVo();

        userVo.setBukrs(map.get("BUKRS").toString());
        userVo.setKostl(map.get("KOSTL").toString());
        userVo.setPernr(map.get("PERNR").toString());
        userVo.setSname(map.get("SNAME").toString());
        userVo.setSmtpAddr(map.get("SMTP_ADDR").toString());
        userVo.setBupla(map.get("BUPLA").toString());
        userVo.setAuthor(map.get("AUTHOR").toString());
        userVo.setBname(map.get("BNAME").toString());
        userVo.setMctxt1(map.get("MCTXT1").toString());
        userVo.setLifnr(map.get("LIFNR").toString());
        userVo.setTenantId(map.get("tenantId").toString());
        userVo.setTenantKey(map.get("tenantKey").toString());
        userVo.setAccessToken(map.get("accessToken").toString());
        userVo.setGwPrefix(map.get("gwPrefix").toString());
        userVo.setId(map.get("id").toString());
        userVo.setPassword(map.get("password").toString());

        return userVo;
    }

    /**
     * @auther : yukpan
     * @type : event
     * @date : 2019.06.18
     * @explain : 저장소에 저장되어있는 유저 데이터를 초기화한다.
     **/
    public void clearLoginData(){

        SharedPreferences sf = context.getSharedPreferences("userData", context.MODE_PRIVATE);

        if(sf != null){

            SharedPreferences.Editor editor = sf.edit();

            editor.putString("tenantId", "");
            editor.putString("id", "");
            editor.putString("password", "");
            editor.putString("tenantKey", "");
            editor.putString("gwPrefix", "");
            editor.putString("bukrs", "");

            editor.commit();
        }

        Singleton.getInstance().setUserVo(new UserVo());
        Singleton.getInstance().setColumnDefine(new HashMap<String, Object>());

    }

    /**
     * @auther : yukpan
     * @type : event
     * @date : 2019.05.29
     * @explain :
     **/
    public HashMap<String, Object> getSelectBoxDate(){
        return new HashMap<String,Object>();
    }

    public static HashMap<String,Object> getData(Response<JsonObject> response){
        JsonObject result = response.body();
        Log.d("eaccountLog", response.toString());
        HashMap<String,Object> resultMap = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            resultMap = mapper.readValue(result.toString(), new HashMap<String,Object>().getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * @auther : yukpan
     * @type : event
     * @date : 2019.07.02
     * @explain : 시작일 데이터를 가져온다.
     **/
    public static String getStaDate(){
        Calendar cal = Calendar.getInstance();

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.getMinimum(Calendar.DAY_OF_MONTH)); //월은 -1해줘야 해당월로 인식

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");


        return sf.format(cal.getTime());
    }

    public static String getEndDate(){
        Calendar cal = Calendar.getInstance();

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //월은 -1해줘야 해당월로 인식

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");


        return sf.format(cal.getTime());
    }


    public static String getOnlyNumber(String date){
        return date.replaceAll("-","");
    }

}

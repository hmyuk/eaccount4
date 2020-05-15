package com.isu.erp.eaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.isu.erp.eaccount.activity.A001Activity;
import com.isu.erp.eaccount.activity.A003Activity;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.util.Util;
import com.isu.erp.eaccount.vo.UserVo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. SharedPreferences 에 user 정보가 있는지 확인

        // 2. if . User 정보가 있으면
        Util util = new Util(getApplicationContext());
        UserVo userVo =  util.getLoginData();

        if(userVo.getTenantId() != null && !"".equals(userVo.getTenantId())){

            HashMap<String,Object> param = new HashMap<String,Object>();
            param.put("tenantId", userVo.getTenantId());
            param.put("id", userVo.getId());
            param.put("password", userVo.getPassword());
            param.put("tenantKey", userVo.getTenantKey());
            param.put("gwPrefix", userVo.getGwPrefix());
            param.put("bukrs", userVo.getBukrs());

            // 2.1 User 정보가 있으면 해당 정보로 User 데이터를 다시 조회한다.
            Call<JsonObject> res = ERetrofit.getInstance().getService().getLogin(param);

            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("eaccountMobile", response.body().toString());
                    Util util1 = new Util(getApplicationContext());
                    HashMap<String,Object> resultMap = Util.getData(response);

                    String resultYn = resultMap.get("resultYn").toString();

                    if("Y".equals(resultYn)){
                        Log.d("eaccountMobile", "2.2 User 정보가 정상적으로 확인되면 A003인 메인화면으로 이동한다.");
                        util1.setLoginData((HashMap<String,Object>)resultMap.get("userData"));
                        // 2.2 User 정보가 정상적으로 확인되면 A003인 메인화면으로 이동한다.
                        Singleton singleton = Singleton.getInstance();
                        singleton.setUserVo(util1.getUserVo((HashMap<String, Object>) resultMap.get("userData")));
                        singleton.setColumnDefine((HashMap<String,Object>)resultMap.get("columnDefine"));

                        Intent intent = new Intent(getApplicationContext(), A003Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Log.d("eaccountMobile", "2.3 User 정보가 정상적이지 않으면 A001인 그룹 로그인 화면으로 이동한다.");
                        // 2.3 User 정보가 정상적이지 않으면 A001인 그룹 로그인 화면으로 이동한다.
                        Intent intent = new Intent(getApplicationContext(), A001Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }



                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("eaccountMobile","2.4 User 정보가 정상적이지 않으면 A001인 그룹 로그인 화면으로 이동한다.");
                    // 2.4 User 정보가 정상적이지 않으면 A001인 그룹 로그인 화면으로 이동한다.
                    Intent intent = new Intent(getApplicationContext(), A001Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

        }else {
            Log.d("eaccountMobile","3. User 정보가 정상적으로 확인되지 않으면 A001인 그룹 로그인 화면으로 이동한다.");
            // 3. User 정보가 정상적으로 확인되지 않으면 A001인 그룹 로그인 화면으로 이동한다.
            Intent intent = new Intent(getApplicationContext(), A001Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}

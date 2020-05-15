package com.isu.erp.eaccount.activity;

import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.common.BackPressCloseHandler;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.delegate.CustomCallback;
import com.isu.erp.eaccount.dialog.SelectBoxDialog;
import com.isu.erp.eaccount.util.EDialog;
import com.isu.erp.eaccount.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class A002Activity extends AppCompatActivity {

    private HashMap<String, Object> groupData;

    private HashMap<String,Object> selectedCompany;

    private BackPressCloseHandler backPressCloseHandler;

    private EditText id;
    private EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a002);

        id = (EditText)findViewById(R.id.editId);
        pw = (EditText)findViewById(R.id.editPw);


        backPressCloseHandler = new BackPressCloseHandler(A002Activity.this);

        Intent intent = getIntent();

        groupData = (HashMap<String, Object>) intent.getSerializableExtra("groupData");


        Log.d("eaccountLog", groupData.toString());

        setCompanyPickerView();

        eventHandler();
    }

    /**
     * @auther : yukpan
     * @date : 2019.06.20
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        ImageView imageView = (ImageView)findViewById(R.id.confirmBtn);
        ImageView grpBtn = (ImageView)findViewById(R.id.grpBtn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBtnClick();
            }
        });


        //Keyboard input area out Event
        ConstraintLayout con = (ConstraintLayout)findViewById(R.id.a002_out_area);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(id.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pw.getWindowToken(), 0);

                id.clearFocus();
                pw.clearFocus();
            }
        });


        //Grp Btn Click Event
        grpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), A001Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    /**
     * @auther : yukpan
     * @date : 2019.06.24
     * @explain : 회사코드 picker를 세팅하는 부분
     **/
    public void setCompanyPickerView(){

        HashMap<String, Object> param = new HashMap<String,Object>();

        param.put("mobileGroupId", groupData.get("mobileGroupId").toString());

        Call<JsonObject> res = ERetrofit.getInstance().getService().getCompanyList(param);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HashMap<String,Object> resultMap = Util.getData(response);

                Log.d("eaccountLog", resultMap.toString());

                String resultYn = resultMap.get("resultYn").toString();

                if("Y".equals(resultYn)){
                    final ArrayList<HashMap<String,Object>> companyData =  (ArrayList<HashMap<String,Object>>)resultMap.get("companyData");

                    if(!companyData.isEmpty()){
                        HashMap<String,Object> firstCompanyData = (HashMap<String, Object>) companyData.get(0);

                        String companyName = firstCompanyData.get("companyName").toString();
                        TextView textView = (TextView)findViewById(R.id.editCompany);
                        textView.setText(companyName);
                        selectedCompany = firstCompanyData;
                    }

                    RelativeLayout companyBox = (RelativeLayout) findViewById(R.id.company_box);

                    companyBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SelectBoxDialog selectBoxDialog = new SelectBoxDialog(A002Activity.this, companyData, "companyName", new CustomCallback() {
                                @Override
                                public void callback(Object map) {
                                    HashMap<String,Object> currentCompany = (HashMap<String,Object>)map;
                                    String companyName = currentCompany.get("companyName").toString();
                                    TextView textView = (TextView)findViewById(R.id.editCompany);
                                    textView.setText(companyName);
                                    selectedCompany = currentCompany;
                                }
                            });
                            selectBoxDialog.show();
                        }
                    });

                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    /**
     @auther : yukpan
     @type : event
     @date : 2019.06.25
     @explain : 인증요청 Event
     **/
    public void confirmBtnClick(){
        if(id.length() < 1){
            EDialog eDialog = new EDialog(getApplicationContext(), A002Activity.this);
            eDialog.setMessage(getString(R.string.a002_id_msg));
            eDialog.showAlertDialog(new EDialog.DialogCallback() {
                @Override
                public void positiveBtnClick() {
                }
                @Override
                public void negativeBtnClick() {
                }
            });
            return;
        }



        if(pw.length() < 1){
            EDialog eDialog = new EDialog(getApplicationContext(), A002Activity.this);
            eDialog.setMessage(getString(R.string.a002_pw_msg));
            eDialog.showAlertDialog(new EDialog.DialogCallback() {
                @Override
                public void positiveBtnClick() {
                }
                @Override
                public void negativeBtnClick() {
                }
            });
            return;
        }


        HashMap<String, Object> param = new HashMap<String,Object>();

        param.put("tenantId", selectedCompany.get("tenantId").toString());
        param.put("id", id.getText().toString());
        param.put("password", pw.getText().toString());
        param.put("tenantKey", selectedCompany.get("tenantKey").toString());
        param.put("gwPrefix", selectedCompany.get("gwPrefix").toString());
        param.put("bukrs", selectedCompany.get("bukrs").toString());

        Call<JsonObject> res = ERetrofit.getInstance().getService().getLogin(param);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HashMap<String,Object> resultMap = Util.getData(response);

                String resultYn = (String)resultMap.get("resultYn");

                if("Y".equals(resultYn)){
                    Log.d("eaccountLog", resultMap.toString());
                    Util util = new Util(getApplicationContext());
                    util.setLoginData((HashMap<String,Object>)resultMap.get("userData"));

                    Singleton singleton = Singleton.getInstance();
                    singleton.setUserVo(util.getUserVo((HashMap<String, Object>) resultMap.get("userData")));
                    singleton.setColumnDefine((HashMap<String,Object>)resultMap.get("columnDefine"));

                    Intent intent = new Intent(getApplicationContext(), A003Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else{
                    EDialog eDialog = new EDialog(getApplicationContext(), A002Activity.this);
                    eDialog.setMessage(getString(R.string.a002_login_fail_msg));
                    eDialog.showAlertDialog(new EDialog.DialogCallback() {
                        @Override
                        public void positiveBtnClick() {
                        }
                        @Override
                        public void negativeBtnClick() {
                        }
                    });
                    return;
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                EDialog eDialog = new EDialog(getApplicationContext(), A002Activity.this);
                eDialog.setMessage(getString(R.string.a002_login_fail_msg));
                eDialog.showAlertDialog(new EDialog.DialogCallback() {
                    @Override
                    public void positiveBtnClick() {
                    }
                    @Override
                    public void negativeBtnClick() {
                    }
                });
                return;
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);

        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}

package com.isu.erp.eaccount.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.util.EDialog;
import com.isu.erp.eaccount.util.Util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class A001Activity extends AppCompatActivity {

    private ImageView confirmBtn;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a001);
        editText = findViewById(R.id.editText);

        this.eventHandler();
    }

    /**
     * @auther : yukpan
     * @date : 2019.06.20
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        // 확인 버튼 클릭시 Event
        confirmBtn = (ImageView) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccount", "click");
                confirmBtnClick();
            }
        });

        ConstraintLayout con = (ConstraintLayout)findViewById(R.id.a001_out_area);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.clearFocus();
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("eaccountLog","editText Touch!!");
                return false;
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("eaccountLog","key!! : " + +keyCode);
                return false;
            }
        });

    }

    /**
     * @auther : yukpan
     * @date : 2019.06.20
     * @explain : confirm btn이 클릭되면 서버에서 해당정보를 조회한다.
     **/
    public void confirmBtnClick(){

        HashMap<String,Object> param = new HashMap<String,Object>();


        String groupCode = editText.getText().toString();

        param.put("groupCode", groupCode);

        Call<JsonObject> res = ERetrofit.getInstance().getService().getAuthGrpCode(param);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                HashMap<String,Object> resultMap = Util.getData(response);

                String resultYn = resultMap.get("resultYn").toString();

                HashMap<String, Object> groupData = (HashMap<String,Object>)resultMap.get("groupData");
                if("Y".equals(resultYn)){
                    Log.d("eaccountLog", groupData.toString());
                    Intent intent = new Intent(getApplicationContext(), A002Activity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("groupData", groupData);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                    finish();
//                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_and_hide);
//                    animation.setStartOffset(1000);
//                    animation.setInterpolator(getApplicationContext(), android.R.anim.accelerate_decelerate_interpolator);

                }else{

                    EDialog eDialog = new EDialog(getApplicationContext(), A001Activity.this);
                    Log.d("eaccountLog", getString(R.string.a001_dialog_msg));

                    eDialog.setMessage(getString(R.string.a001_dialog_msg));
                    eDialog.showAlertDialog(new EDialog.DialogCallback() {
                        @Override
                        public void positiveBtnClick() {
                        }
                        @Override
                        public void negativeBtnClick() {
                        }
                    });

                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                EDialog eDialog = new EDialog(getApplicationContext(), A001Activity.this);
                Log.d("eaccount", getString(R.string.a001_dialog_msg));
                eDialog.setMessage(getString(R.string.a001_dialog_msg));
                eDialog.showAlertDialog(new EDialog.DialogCallback() {
                    @Override
                    public void positiveBtnClick() {
                    }
                    @Override
                    public void negativeBtnClick() {
                    }
                });
            }
        });

    }

}

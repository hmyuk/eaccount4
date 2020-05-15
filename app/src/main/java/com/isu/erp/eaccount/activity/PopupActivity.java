package com.isu.erp.eaccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.adapter.ListviewMainAdapter;
import com.isu.erp.eaccount.adapter.PopupAdapter;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.util.Util;
import com.isu.erp.eaccount.vo.UserVo;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopupActivity extends AppCompatActivity {

    private int resultCode = 0;

    private TextView keyCol;
    private TextView valuecol;

    private String rfcFunctionNm = "";
    private EditText editText;

    private HashMap<String,Object> data;
    private String zprog;

    private ListView listView;

    private PopupAdapter adapter;

    private ArrayList<HashMap<String,Object>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Singleton.getInstance().addActivityStack(PopupActivity.this);
        TextView toolbarText = (TextView)findViewById(R.id.toolbar_text);

        keyCol = (TextView)findViewById(R.id.popup_key_col);
        valuecol = (TextView)findViewById(R.id.popup_value_col);
        editText = (EditText)findViewById(R.id.popup_search);

//        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        listData = new ArrayList<HashMap<String, Object>>();

        Intent intent = getIntent();
        data = (HashMap<String,Object>)intent.getSerializableExtra("data");

        zprog = intent.getStringExtra("zprog");


        String title = (String)data.get("title");
        String columnName = (String)data.get("columnName");
        String type = (String)data.get("type");
        String description = (String)data.get("description");
        String masterVisible = (String)data.get("masterVisible");
        String detailVisible = (String)data.get("detailVisible"); // 여기에서는 모두 true 이미 필터링을 거쳤음.
        String mandatory = (String)data.get("mandatory");
        String value = (String)data.get("value");
        

        toolbarText.setText(title);
        keyCol.setText(title);
        valuecol.setText(title+"명");

        if("HKONT".equals(columnName)){
            rfcFunctionNm = "ZFI_ED_M_F4_004";
            resultCode = 100;
        }else if("AUFNR".equals(columnName)){
            rfcFunctionNm = "ZFI_ED_M_F4_006";
            resultCode = 200;
        }else if("KOSTL".equals(columnName)){
            rfcFunctionNm = "ZFI_ED_M_F4_003";
            resultCode = 300;
        }else if("LIFNR".equals(columnName)){
            rfcFunctionNm = "ZFI_ED_M_F4_012";
            resultCode = 400;
        }



        listView = (ListView)findViewById(R.id.popup_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.clearFocus();

                Intent intent = new Intent();

                intent.putExtra("selectedData", listData.get(position));
                setResult(RESULT_OK, intent);
                Singleton.getInstance().removeActivityStack(PopupActivity.this);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);


            }
        });

        adapter = new PopupAdapter(getApplicationContext(), listData, columnName);

        eventHandler();
        selectData();
    }

    /**
     * @auther : yukpan
     * @date : 2019.07.05
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        View backBox = (View)findViewById(R.id.back_box);
        Button retrieveBtn = (Button)findViewById(R.id.popup_retrieve_btn);
        ConstraintLayout outArea = (ConstraintLayout)findViewById(R.id.popup_out_area);

        //back btn
        backBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                setResult(RESULT_OK, intent);
                Singleton.getInstance().removeActivityStack(PopupActivity.this);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData();
            }
        });

        outArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccountLog","conClick!");
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    editText.clearFocus();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("eaccountLog","key!!!! : " + keyCode);
                if(keyCode == 66){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    editText.clearFocus();
                }
                return false;
            }
        });

    }

    /**
     @auther : yukpan
     @type : select
     @date : 2019.07.05
     @explain : 데이터를 조회하여 가져온다.
     **/
    public void selectData(){

        final String columnName = (String)data.get("columnName");
        String searchKey = editText.getText().toString();

        UserVo userVo = Singleton.getInstance().getUserVo();

        HashMap<String,Object> param = new HashMap<String,Object>();

        param.put("I_ZPROG", zprog);

        param.put("tenantId", userVo.getTenantId());
        param.put("tenantKey", userVo.getTenantKey());
        param.put("BUKRS", userVo.getBukrs());
        param.put("PERNR", userVo.getPernr());
        param.put("searchKey", searchKey);


        Call<JsonObject> res = null;
        if("HKONT".equals(columnName)){
            res = ERetrofit.getInstance().getService().getZFI_ED_M_F4_004(param);
        }else if("AUFNR".equals(columnName)){
            res = ERetrofit.getInstance().getService().getZFI_ED_M_F4_006(param);
        }else if("KOSTL".equals(columnName)){
            res = ERetrofit.getInstance().getService().getZFI_ED_M_F4_003(param);
        }else if("LIFNR".equals(columnName)){
            res = ERetrofit.getInstance().getService().getZFI_ED_M_F4_012(param);
        }

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HashMap<String,Object> resultMap = Util.getData(response);

                String resultYn = (String)resultMap.get("resultYn");
                ArrayList<HashMap<String,Object>> resultList = (ArrayList<HashMap<String, Object>>)resultMap.get("data");

                if("Y".equals(resultYn)){
                    Log.d("eaccountLog",data.toString());

                    listData = resultList;
                    adapter = new PopupAdapter(getApplicationContext(), listData, columnName);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }



}

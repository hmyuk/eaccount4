package com.isu.erp.eaccount.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.adapter.ListviewMainAdapter;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.util.EDialog;
import com.isu.erp.eaccount.util.Util;
import com.isu.erp.eaccount.vo.UserVo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class B001Activity extends AppCompatActivity {

    private ArrayList<HashMap<String,Object>> data;
    private HashMap<String,Object> codeData;

    private ListView listView;
    private ListviewMainAdapter adapter;

    private TextView staDate;
    private TextView endDate;

    private Button retrieveBtn;

    private int CAL_STA_CODE = 100;
    private int CAL_END_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b001);

        Singleton.getInstance().addActivityStack(B001Activity.this);
        TextView toolbarText = (TextView)findViewById(R.id.toolbar_text);
        toolbarText.setText(getString(R.string.toolbar_b001_title));

        data = new ArrayList<HashMap<String, Object>>();
        staDate = (TextView)findViewById(R.id.sta_date);
        endDate = (TextView)findViewById(R.id.end_date);

        retrieveBtn = (Button)findViewById(R.id.b001_retrieve_btn);

        staDate.setText(Util.getStaDate());
        endDate.setText(Util.getEndDate());

        eventHandler();


        listView = (ListView) findViewById(R.id.b001_listview);
//        adapter = new ListviewMainAdapter(getApplicationContext(), data);
//        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("eaccountLog",data.get(position).toString());
                showDetailActivity(data.get(position));
            }
        });

        selectData();

    }


    public void showDetailActivity(HashMap<String,Object> rowData){
        HashMap<String, Object> columnDefines = Singleton.getInstance().getColumnDefine();
        ArrayList<HashMap<String, Object>> columnDefine = (ArrayList<HashMap<String,Object>>)columnDefines.get("ZFI_ED_M_002");

        ArrayList<HashMap<String,Object>> detailData = new ArrayList<HashMap<String,Object>>();

        for(HashMap<String,Object> rowDefine : columnDefine){

            String columnName = (String)rowDefine.get("columnName");

            if("true".equals(rowDefine.get("detailVisible"))){
                rowDefine.put("value", rowData.get(columnName));
                detailData.add(rowDefine);
            }
        }

        Intent intent = new Intent(getApplicationContext(), B001DetailActivity.class);

        intent.putExtra("data", detailData);
        intent.putExtra("codeData", codeData);

        startActivityForResult(intent, 300);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }


    /**
     * @auther : yukpan
     * @date : 2019.07.01
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        View backBox = (View)findViewById(R.id.back_box);
        LinearLayout staDateBox = (LinearLayout)findViewById(R.id.sta_date_box);
        LinearLayout endDateBox = (LinearLayout)findViewById(R.id.end_date_box);

        //back btn
        backBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), A003Activity.class);
                setResult(RESULT_OK, intent);
                Singleton.getInstance().removeAllActivityStack();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        //시작일 btn
        staDateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccountLog","STD!");
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivityForResult(intent, CAL_STA_CODE);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        //종료일 btn
        endDateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccountLog","END!");
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivityForResult(intent, CAL_END_CODE);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });


        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData();
            }
        });
    }

    /**
     * @auther : yukpan
     * @date : 2019.07.02
     * @explain : 데이터를 가져온다.
     **/
    public void selectData(){

        int tmpStaDate = Integer.parseInt(Util.getOnlyNumber(staDate.getText().toString()));
        int tmpEndDate = Integer.parseInt(Util.getOnlyNumber(endDate.getText().toString()));

        if(tmpEndDate - tmpStaDate < 0){
            EDialog eDialog = new EDialog(getApplicationContext(), B001Activity.this);
            eDialog.setMessage(getString(R.string.moreThanDateMsg));
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

        UserVo userVo = Singleton.getInstance().getUserVo();


        HashMap<String,Object> param = new HashMap<String,Object>();

        param.put("tenantId", userVo.getTenantId());
        param.put("tenantKey", userVo.getTenantKey());
        param.put("BUKRS", userVo.getBukrs());
        param.put("PERNR", userVo.getPernr());
        param.put("I_BUKRS",userVo.getBukrs());
        param.put("I_CARDNO","");
        param.put("I_KOSTL", userVo.getKostl());
        param.put("I_APPDT_F", Util.getOnlyNumber(staDate.getText().toString()));
        param.put("I_APPDT_T", Util.getOnlyNumber(endDate.getText().toString()));
        param.put("I_LPERNR",  userVo.getPernr());
        param.put("I_PERNR", userVo.getPernr());

        final Call<JsonObject> res = ERetrofit.getInstance().getService().getZFI_ED_M_002(param);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HashMap<String,Object> resultMap = Util.getData(response);

                Log.d("eaccountLog",resultMap.toString());

                String resultYn = (String)resultMap.get("resultYn");


                if("Y".equals(resultYn)){

                    //CARD DATA
                    ArrayList<HashMap<String,Object>> resultData = (ArrayList<HashMap<String, Object>>)resultMap.get("cardData");
                    data = resultData;
                    adapter = new ListviewMainAdapter(getApplicationContext(), data);
                    adapter.setTextViewColumnNm1("APPRDATE");
                    adapter.setTextViewColumnNm2("MERCHNAME");
                    adapter.setTextViewColumnNm3("ACQUTOT");
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                    //CODE DATA
                    codeData = (HashMap<String, Object>)resultMap.get("codeData");

                    if(resultData.size() < 1){
                        EDialog eDialog = new EDialog(getApplicationContext(), B001Activity.this);
                        eDialog.setMessage(getString(R.string.not_found_data));
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

                }else{
                    EDialog eDialog = new EDialog(getApplicationContext(), B001Activity.this);
                    eDialog.setMessage(getString(R.string.not_found_data));
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

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("eaccountLog","result Method");
        if(resultCode == RESULT_OK) switch (requestCode) {
            case 100 :
                if(data != null) {
                    Log.d("eaacountLog", "STA CALLBACK : " + data.getStringExtra("selectedDate"));
                    if (data.getStringExtra("selectedDate") != null && data.getStringExtra("selectedDate").length() > 0) {
                        staDate.setText(data.getStringExtra("selectedDate"));
                    }

                }

                break;
            case 200:

                if(data != null){
                    Log.d("eaccountLog", "END CALLBACK"+data.getStringExtra("selectedDate"));
                    if(data.getStringExtra("selectedDate") != null && data.getStringExtra("selectedDate").length() > 0){
                        endDate.setText(data.getStringExtra("selectedDate"));
                    }


                }
                break;
            case 300 :
                Log.d("eaccountLog","Code : 300 ");
                break;
        }
    }

}

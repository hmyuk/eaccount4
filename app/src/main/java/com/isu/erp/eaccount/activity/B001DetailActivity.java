package com.isu.erp.eaccount.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.adapter.ListViewDetailAdapter;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.delegate.CustomCallback;
import com.isu.erp.eaccount.delegate.EditTextSaveCallback;
import com.isu.erp.eaccount.dialog.SelectBoxDialog;
import com.isu.erp.eaccount.util.EDialog;
import com.isu.erp.eaccount.util.Util;
import com.isu.erp.eaccount.vo.UserVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class B001DetailActivity extends AppCompatActivity {


    private ArrayList<HashMap<String,Object>> data;
    private HashMap<String, Object> codeData;
    private ListView listView;
    private ListViewDetailAdapter adapter;

    private EditText lastSelectTextField;

    private HashMap<Integer, TextView> editTextMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b001_detail);
        Singleton.getInstance().addActivityStack(B001DetailActivity.this);
        TextView toolbarText = (TextView)findViewById(R.id.toolbar_text);
        toolbarText.setText(getString(R.string.toolbar_detail_title));


        editTextMap = new HashMap<Integer, TextView>();
        data = (ArrayList<HashMap<String,Object>>)getIntent().getSerializableExtra("data");
        codeData = (HashMap<String,Object>)getIntent().getSerializableExtra("codeData");



        listView = (ListView)findViewById(R.id.b001_detail_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("eaccountLog","onItemClick!");

                if(lastSelectTextField != null){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lastSelectTextField.getWindowToken(), 0);

                    lastSelectTextField.clearFocus();
                }


                HashMap<String,Object> rowData = data.get(position);

                String title = (String)data.get(position).get("title");
                final String columnName = (String)data.get(position).get("columnName");
                String type = (String)data.get(position).get("type");
                String description = (String)data.get(position).get("description");
                String masterVisible = (String)data.get(position).get("masterVisible");
                String detailVisible = (String)data.get(position).get("detailVisible"); // 여기에서는 모두 true 이미 필터링을 거쳤음.
                String mandatory = (String)data.get(position).get("mandatory");
                String value = (String)data.get(position).get("value");

                ArrayList<HashMap<String,Object>> inputCodeData = (ArrayList<HashMap<String,Object>>)codeData.get(columnName);

                final TextView textView2 = (TextView)view.findViewById(R.id.listview_detail_tv2);

                if("label".equals(type)){

                }else if("textField".equals(type)){

                }else if("search".equals(type)){

                    Intent intent = new Intent(getApplicationContext(), PopupActivity.class);

                    int returnCode = 0;
                    if("HKONT".equals(columnName)){
                        returnCode = 100;
                    }else if("AUFNR".equals(columnName)){
                        returnCode = 200;
                    }else if("KOSTL".equals(columnName)){
                        returnCode = 300;
                    }else if("LIFNR".equals(columnName)){
                        returnCode = 400;
                    }

                    intent.putExtra("data", data.get(position));
                    intent.putExtra("zprog","ED1.1.1");
                    startActivityForResult(intent, returnCode);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

                }else if("select".equals(type)){
                    SelectBoxDialog selectBoxDialog = new SelectBoxDialog(B001DetailActivity.this, inputCodeData, "text", new CustomCallback() {
                        @Override
                        public void callback(Object map) {
                            HashMap<String,Object> currentData = (HashMap<String,Object>)map;

                            String currentValue = currentData.get("value").toString();
                            String currentText = currentData.get("text").toString();
                            textView2.setText(currentText);
                            data.get(position).put("value", currentValue);

                            Log.d("eaccountLog","### select ### ");
                            Log.d("eaccountLog",data.get(position).toString());

                            //구분 값 변경 시 추가 이벤트 발생
                            if("ZGUBUN1".equals(columnName)){
                                ZGUBUN1ChangeEvent(currentValue);
                            }
                        }
                    });
                    selectBoxDialog.show();
                }
            }
        });

        adapter = new ListViewDetailAdapter(getApplicationContext(), data, codeData, new CustomCallback() {
            @Override
            public void callback(Object obj) {
                Log.d("eaccountLog", "callback!!");

                lastSelectTextField = (EditText) obj;
            }
        }, new CustomCallback() {
            @Override
            public void callback(Object obj) {
                //enter Key Event Callback
                if (lastSelectTextField != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lastSelectTextField.getWindowToken(), 0);

                    lastSelectTextField.clearFocus();
                }
            }
        }, new EditTextSaveCallback() {
            @Override
            public void callback(Integer idx, EditText editText) {
                editTextMap.put(idx, editText);
            }
        });


        listView.setAdapter(adapter);

        Log.d("eaccountLog","#####################");
        Log.d("eaccountLog", data.toString());
        Log.d("eaccountLog", codeData.toString());

        eventHandler();
    }

    /**
     * @auther : yukpan
     * @date : 2019.07.03
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        View backBox = (View)findViewById(R.id.back_box);
        Button createBtn = (Button)findViewById(R.id.b001_create_btn);
        ConstraintLayout outArea = (ConstraintLayout)findViewById(R.id.b001_detail_out_area);


        //back btn
        backBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                Singleton.getInstance().removeActivityStack(B001DetailActivity.this);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBtnClick();
            }
        });

        outArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccountLog","conClick!");
                if(lastSelectTextField != null){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lastSelectTextField.getWindowToken(), 0);

                    lastSelectTextField.clearFocus();
                }
            }
        });

//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("eaccountLog","list onClick!");
//            }
//        });

    }

    /**
     @auther : yukpan
     @type : event
     @date : 2019.07.04
     @explain : 전표생성버튼 클릭시 발생하는 event
     **/
    public void createBtnClick(){
        if(!validationCheck()){
            return;
        }

        Log.d("eaccountLog", editTextMap.toString());

        Iterator iter =  editTextMap.keySet().iterator();
        while(iter.hasNext()){
            Integer idx  = (Integer) iter.next();
            EditText editText = (EditText)editTextMap.get(idx.intValue());

            data.get(idx).put("value",editText.getText().toString());

        }

        Log.d("eaccountLog", "##########");
        Log.d("eaccountLog", data.toString());

    }

    /**
     @auther : yukpan
     @type : event
     @date : 2019.07.04
     @explain : validation을 체크한다.
     **/
    public boolean validationCheck(){
        boolean checkBool = true;

        for(HashMap<String,Object> map : data){
            String title = (String)map.get("title");
            String columnName = (String)map.get("columnName");
            String type = (String)map.get("type");
            String description = (String)map.get("description");
            String masterVisible = (String)map.get("masterVisible");
            String detailVisible = (String)map.get("detailVisible"); // 여기에서는 모두 true 이미 필터링을 거쳤음.
            String mandatory = (String)map.get("mandatory");
            String value = (String)map.get("value");

            if("true".equals(mandatory) && "".equals(value)){
                checkBool = false;

                EDialog eDialog = new EDialog(getApplicationContext(), B001DetailActivity.this);
                eDialog.setMessage("["+title+"]"+getString(R.string.mandatory_field_msg));
                eDialog.showAlertDialog(new EDialog.DialogCallback() {
                    @Override
                    public void positiveBtnClick() {
                    }
                    @Override
                    public void negativeBtnClick() {
                    }
                });
                return checkBool;
            }

        }
        return checkBool;
    }

    /**
     @auther : yukpan
     @type : event
     @date : 2019.07.08
     @explain : ZGUBUN 변경시 이벤트
                구분변경시 > 대계정 코드 변경 , g/l계정 , g/l계정명 , 세금코드 변경
     **/
    public void ZGUBUN1ChangeEvent(String ZGUBUN1){

        String VAT1 = "";

        for(HashMap<String,Object> map : data){
            String columnName = (String)map.get("columnName");
            if("VAT1".equals(columnName)){
                VAT1 = (String)map.get("value");
            }

        }


        HashMap<String,Object> param = new HashMap<String,Object>();

        UserVo userVo = Singleton.getInstance().getUserVo();
        param.put("tenantId", userVo.getTenantId());
        param.put("tenantKey", userVo.getTenantKey());
        param.put("BUKRS", userVo.getBukrs());
        param.put("PERNR", userVo.getPernr());
        param.put("I_ZGUBUN1", ZGUBUN1);
        param.put("I_ZPROG", "ED1.1.1");
        param.put("VAT1", VAT1);

        Call<JsonObject> res = ERetrofit.getInstance().getService().getChangeGubun(param);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HashMap<String,Object> resultMap = Util.getData(response);

                String resultYn  = (String)resultMap.get("resultYn");
                HashMap<String,Object> inData = (HashMap<String,Object>)resultMap.get("data");

                if("Y".equals(resultYn)){
                    ArrayList<HashMap<String,Object>> ZFI_ED_M_F4_009 = (ArrayList<HashMap<String,Object>>)inData.get("ZFI_ED_M_F4_009");
                    ArrayList<HashMap<String,Object>> ZGUBUN2_Dic = (ArrayList<HashMap<String,Object>>)inData.get("ZGUBUN2");

                    codeData.put("ZGUBUN2", ZGUBUN2_Dic);

                    if(ZFI_ED_M_F4_009 != null && ZFI_ED_M_F4_009.size() > 0){
                        String HKONT = ZFI_ED_M_F4_009.get(0).get("HKONT").toString();
                        String TXT20 = ZFI_ED_M_F4_009.get(0).get("TXT20").toString();
                        String MWSKZ = ZFI_ED_M_F4_009.get(0).get("MWSKZ").toString();


                        //여기 코딩중이였음
                        for(HashMap<String,Object> map : data){
                            String columnName = (String)map.get("columnName");

                            if("HKONT".equals(columnName)){
                                map.put("value", HKONT);
                            }else if("TXT20".equals(columnName)){
                                map.put("value", TXT20);
                            }else if("MWSKZ".equals(columnName)){
                                map.put("value", MWSKZ);
                            }
                        }

                    }

                    adapter.updateList();
                }




            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK){

            HashMap<String,Object> selectedData = (HashMap<String,Object>)intent.getSerializableExtra("selectedData");

            if(selectedData == null){
                return;
            }

            String keyCol = "";
            String valueCol = "";
            String keyVal = "";
            String valueVal = "";

            switch (requestCode){
                case 100 :

                    keyCol = "HKONT";
                    valueCol = "TXT20";
                    keyVal = (String)selectedData.get(keyCol);
                    valueVal = (String)selectedData.get(valueCol);
                    break;
                case 200 :
                    keyCol = "AUFNR";
                    valueCol = "KTEXT";
                    keyVal = (String)selectedData.get(keyCol);
                    valueVal = (String)selectedData.get(valueCol);
                    break;
                case 300 :
                    keyCol = "KOSTL";
                    valueCol = "MCTXT1";
                    keyVal = (String)selectedData.get(keyCol);
                    valueVal = (String)selectedData.get(valueCol);
                    break;
                case 400:
                    break;
            }

            for(HashMap<String,Object> map : this.data){
                String columnName = (String)map.get("columnName");

                if(keyCol.equals(columnName)){
                    map.put("value", keyVal);
                }else if(valueCol.equals(columnName)){
                    map.put("value", valueVal);
                }
            }
            adapter.updateList();

        }
    }

}

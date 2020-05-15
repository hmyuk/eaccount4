package com.isu.erp.eaccount.adapter;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.delegate.CustomCallback;
import com.isu.erp.eaccount.delegate.EditTextSaveCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewDetailAdapter extends BaseAdapter {

    private ArrayList<HashMap<String,Object>> list;

    private Context context;
    private HashMap<Integer, Object> positionMapper;
    private HashMap<String, Object> codeData;
    private CustomCallback callback;
    private CustomCallback enterCallback;
    private EditTextSaveCallback editTextSaveCallback;

    public ListViewDetailAdapter(Context context, ArrayList<HashMap<String, Object>> list, HashMap<String,Object> codeData, CustomCallback callback,
                                 CustomCallback enterCallback, EditTextSaveCallback editTextSaveCallback) {
        this.context = context;
        this.list = list;
        this.positionMapper = new HashMap<Integer, Object>();
        this.codeData = codeData;

        this.callback = callback;
        this.enterCallback = enterCallback;
        this.editTextSaveCallback = editTextSaveCallback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LinearLayout linearLayout;

//        Log.d("eaccountLog", "position : " + position);
//        Log.d("eaccountLog", "view : " +( view == null));

        if(this.positionMapper.get(position) == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //type : label , EditText, selectbox, search

            View inflaterView = layoutInflater.inflate(R.layout.listview_detail_adapter_linear, null);

            TextView textView1 = (TextView) inflaterView.findViewById(R.id.listview_detail_tv1);
            TextView textView2 = (TextView) inflaterView.findViewById(R.id.listview_detail_tv2);
            final EditText editText = (EditText) inflaterView.findViewById(R.id.listview_detail_et1);
            ImageView imageView = (ImageView) inflaterView.findViewById(R.id.listview_img1);

            String title = (String)list.get(position).get("title");
            String columnName = (String)list.get(position).get("columnName");
            String type = (String)list.get(position).get("type");
            String description = (String)list.get(position).get("description");
            String masterVisible = (String)list.get(position).get("masterVisible");
            String detailVisible = (String)list.get(position).get("detailVisible"); // 여기에서는 모두 true 이미 필터링을 거쳤음.
            String mandatory = (String)list.get(position).get("mandatory");
            String value = (String)list.get(position).get("value");


            textView1.setText(title);

            //타입별 row 세팅
            if("label".equals(type) || "date".equals(type)){
                textView2.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.INVISIBLE);

                textView2.setText(value);
            }else if("textField".equals(type)){
                textView2.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);

                editText.setText(value);

                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        Log.d("eaccountLog","Focuse Out!");

                        //callback 함수를 사용하여 lastSelectTextField 에 해당 TextView를 binding 하자.
                        callback.callback(editText);

                    }
                });
                editText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        Log.d("eaccountLog","Key insert! : " + keyCode);
                        if (keyCode == 66){
                            enterCallback.callback(editText);
                        }
                        return false;
                    }
                });


                editTextSaveCallback.callback(position, editText);

            }else if("search".equals(type)){
                textView2.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                imageView.setImageResource(R.drawable.search_icon);
                textView2.setText(value);

            }else if("select".equals(type)){
                textView2.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                imageView.setImageResource(R.drawable.selectbox_down);

                ArrayList<HashMap<String,Object>> codeList = (ArrayList<HashMap<String,Object>>)codeData.get(columnName);

                for(HashMap<String,Object> map : codeList){
                    String val = (String)map.get("value");
                    String txt = (String)map.get("text");

                    if(val.equals(value)){
                        value = txt;
                    }

                }

                textView2.setText(value);
            }

            //필수항목
            if("true".equals(mandatory)){

            }


            linearLayout = (LinearLayout) inflaterView.findViewById(R.id.listview_detail_dapter_linear);

            this.positionMapper.put(position, linearLayout);

        }else{
            linearLayout = (LinearLayout)this.positionMapper.get(position);
        }


        return linearLayout;
    }

    public void updateList() {
//        this.list.clear();
//        this.list.addAll(list);
        this.positionMapper = new HashMap<Integer, Object>();
        this.notifyDataSetChanged();
//        this.notifyDataSetInvalidated();
    }


}

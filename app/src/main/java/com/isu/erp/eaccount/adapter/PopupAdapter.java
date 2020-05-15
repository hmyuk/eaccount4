package com.isu.erp.eaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isu.erp.eaccount.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PopupAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String,Object>> list;
    private HashMap<Integer, Object> positionMapper;

    private String textFieldNm;

    private String columnName;

    public PopupAdapter(Context context, ArrayList<HashMap<String, Object>> list, String columnName) {
        this.context = context;
        this.list = list;
        this.positionMapper = new HashMap<Integer, Object>();
        this.columnName = columnName;
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

        if(this.positionMapper.get(position) == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View inflaterView = layoutInflater.inflate(R.layout.popup_adapter_linear, null);

            TextView textView1 = (TextView) inflaterView.findViewById(R.id.listview_popup_tv1);
            TextView textView2 = (TextView) inflaterView.findViewById(R.id.listview_popup_tv2);

            if("HKONT".equals(columnName)){
                textView1.setText(list.get(position).get("HKONT").toString());
                textView2.setText(list.get(position).get("TXT20").toString());
            }else if("AUFNR".equals(columnName)){
                textView1.setText(list.get(position).get("AUFNR").toString());
                textView2.setText(list.get(position).get("KTEXT").toString());
            }else if("KOSTL".equals(columnName)){
                textView1.setText(list.get(position).get("KOSTL").toString());
                textView2.setText(list.get(position).get("MCTXT1").toString());
            }else if("LIFNR".equals(columnName)){
                textView1.setText(list.get(position).get("LIFNR").toString());
                textView2.setText(list.get(position).get("NAME1").toString());
            }


            linearLayout = (LinearLayout) inflaterView.findViewById(R.id.listview_popup_adapter_linear);

            this.positionMapper.put(position, linearLayout);

        }else{
            linearLayout = (LinearLayout)this.positionMapper.get(position);
        }


        return linearLayout;
    }
}

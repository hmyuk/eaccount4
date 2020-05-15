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

public class ListviewMainAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String,Object>> list;
    private HashMap<Integer, Object> positionMapper;
    private String textViewColumnNm1 = "";
    private String textViewColumnNm2 = "";
    private String textViewColumnNm3 = "";

    public ListviewMainAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
        this.positionMapper = new HashMap<Integer, Object>();
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

            View inflaterView = layoutInflater.inflate(R.layout.listview_main_adapter_linear, null);

            TextView textView1 = (TextView) inflaterView.findViewById(R.id.listview_main_tv1);
            TextView textView2 = (TextView) inflaterView.findViewById(R.id.listview_main_tv2);
            TextView textView3 = (TextView) inflaterView.findViewById(R.id.listview_main_tv3);

            textView1.setText(list.get(position).get(this.textViewColumnNm1).toString());
            textView2.setText(list.get(position).get(this.textViewColumnNm2).toString());
            textView3.setText(list.get(position).get(this.textViewColumnNm3).toString());

            linearLayout = (LinearLayout) inflaterView.findViewById(R.id.listview_main_dapter_linear);

            this.positionMapper.put(position, linearLayout);

        }else{
            linearLayout = (LinearLayout)this.positionMapper.get(position);
        }


        return linearLayout;
    }

    public String getTextViewColumnNm1() {
        return textViewColumnNm1;
    }

    public void setTextViewColumnNm1(String textViewColumnNm1) {
        this.textViewColumnNm1 = textViewColumnNm1;
    }

    public String getTextViewColumnNm2() {
        return textViewColumnNm2;
    }

    public void setTextViewColumnNm2(String textViewColumnNm2) {
        this.textViewColumnNm2 = textViewColumnNm2;
    }

    public String getTextViewColumnNm3() {
        return textViewColumnNm3;
    }

    public void setTextViewColumnNm3(String textViewColumnNm3) {
        this.textViewColumnNm3 = textViewColumnNm3;
    }
}

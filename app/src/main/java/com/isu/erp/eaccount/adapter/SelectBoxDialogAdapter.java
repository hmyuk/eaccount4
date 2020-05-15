package com.isu.erp.eaccount.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.isu.erp.eaccount.R;

public class SelectBoxDialogAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String,Object>> list;
    private HashMap<Integer, Object> positionMapper;

    private String textFieldNm;

    public SelectBoxDialogAdapter(Context context, ArrayList<HashMap<String, Object>> list, String textFieldNm) {
        this.context = context;
        this.list = list;
        this.positionMapper = new HashMap<Integer, Object>();

        this.textFieldNm = textFieldNm;
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

            View inflaterView = layoutInflater.inflate(R.layout.adapter_select_dialog_linear, null);

            TextView textView = (TextView) inflaterView.findViewById(R.id.select_text);

            textView.setText(list.get(position).get(textFieldNm).toString());

            linearLayout = (LinearLayout) inflaterView.findViewById(R.id.adapter_select_dialog_linear);

            this.positionMapper.put(position, linearLayout);

        }else{
            linearLayout = (LinearLayout)this.positionMapper.get(position);
        }


        return linearLayout;
    }
}


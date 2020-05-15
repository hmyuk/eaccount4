package com.isu.erp.eaccount.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isu.erp.eaccount.adapter.SelectBoxDialogAdapter;
import com.isu.erp.eaccount.delegate.CustomCallback;

import java.util.ArrayList;
import java.util.HashMap;

import com.isu.erp.eaccount.R;

import org.w3c.dom.Text;

public class SelectBoxDialog extends Dialog {

    private CustomCallback customCallback;
    private ArrayList<HashMap<String,Object>> data;
    private Context context;

    private String textFieldNm;

    public SelectBoxDialog(Context context, ArrayList<HashMap<String,Object>> data, String textFieldNm, CustomCallback customCallback){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.context = context;
        this.customCallback = customCallback;
        this.data = data;
        this.textFieldNm = textFieldNm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.adapter_select_dialog);

        ListView listView = (ListView)findViewById(R.id.listview);

        SelectBoxDialogAdapter adapter = new SelectBoxDialogAdapter(context, data, textFieldNm);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customCallback.callback(data.get(position));
                customHide();
            }
        });


        eventHandler();

    }

    /**
     * @auther : yukpan
     * @date : 2019.06.21
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        TextView cancel = (TextView) findViewById(R.id.cancel);
        TextView done = (TextView) findViewById(R.id.done);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccountLog","cancel btn Click");
                customHide();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("eaccountLog","done btn Click");
            }
        });

    }

    @Override
    public void show() {
        super.show();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.up_show);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.outerRelativeLayout);
        relativeLayout.startAnimation(animation);
    }

    public void customHide(){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_hide);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.outerRelativeLayout);
        relativeLayout.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("eaccountLog", "hide!!");
                hide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

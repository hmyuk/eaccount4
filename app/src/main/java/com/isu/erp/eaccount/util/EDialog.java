package com.isu.erp.eaccount.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.isu.erp.eaccount.R;

import java.util.HashMap;

public class EDialog {

    private Context context;
    private Activity activity;

    private String title;
    private String message;
    private Boolean positiveBtnUse = false;
    private Boolean negativeBtnUse = true;
    private String positiveText;
    private String negativeText;

    public EDialog(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        this.title = context.getString(R.string.dialog_default_title);
        this.message = context.getString(R.string.dialog_default_message);
        this.positiveText = context.getString(R.string.dialog_default_pText);
        this.negativeText = context.getString(R.string.dialog_default_nText);
    }

    public interface DialogCallback{
        void positiveBtnClick();
        void negativeBtnClick();
    }

    private DialogCallback dialogCallback;


    /**
     * @auther : yukpan
     * @type : event
     * @date : 2019.06.20
     * @explain :
     * @param : 1. (*)title : 타이틀 (default : 메시지)
     *          2. (*)message : 메시지
     *          3. positiveBtnYn : 확인버튼 유무 (default : N)
     *          4. negativeBtnYn : 취소버튼 유무 (default : Y)
     *          5. positiveText : 확인버튼 Text (default : 확인)
     *          6. negativeText : 취소버튼 Text (default : 취소)
     **/
    public void showAlertDialog(DialogCallback callback){

        this.dialogCallback = callback;

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // 제목셋팅
        alertDialogBuilder.setTitle(this.title);


        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage(this.message)
                .setCancelable(false);

        if(this.positiveBtnUse){
            alertDialogBuilder.setPositiveButton(this.positiveText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            // 프로그램을 종료한다.
                            dialogCallback.positiveBtnClick();
                        }
                    });
        }

        if(this.negativeBtnUse){
            alertDialogBuilder.setNegativeButton(this.negativeText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            // 다이얼로그를 취소한다
                            dialog.cancel();
                            dialogCallback.negativeBtnClick();
                        }
                    });
        }

        // 다이얼로그 생성

        AlertDialog alertDialog = alertDialogBuilder.create();

        // 다이얼로그 보여주기
        alertDialog.show();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getPositiveBtnUse() {
        return positiveBtnUse;
    }

    public void setPositiveBtnUse(Boolean positiveBtnUse) {
        this.positiveBtnUse = positiveBtnUse;
    }

    public Boolean getNegativeBtnUse() {
        return negativeBtnUse;
    }

    public void setNegativeBtnUse(Boolean negativeBtnUse) {
        this.negativeBtnUse = negativeBtnUse;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }
}

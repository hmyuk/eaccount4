package com.isu.erp.eaccount.common;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    private String message;


    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            //activity.finish();
            activity.moveTaskToBack(true);



//            Singleton.getInstance().removeAllActivityStack();

            activity.finish();
            Singleton.getInstance().removeAllActivityStack();


            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "  \'뒤로\'버튼을 한번 더 누르시면 종료됩니다.  ", Toast.LENGTH_SHORT);
        toast.show();
    }
}

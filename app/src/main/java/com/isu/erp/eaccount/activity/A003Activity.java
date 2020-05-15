package com.isu.erp.eaccount.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.common.ERetrofit;
import com.isu.erp.eaccount.common.Singleton;
import com.isu.erp.eaccount.util.EDialog;
import com.isu.erp.eaccount.util.Util;
import com.isu.erp.eaccount.vo.UserVo;

import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class A003Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a003);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);

//        toggle.setHomeAsUpIndicator(R.drawable.a003_top_left_icon);
        toggle.syncState();



        ImageView img = (ImageView) findViewById(R.id.navi_btn);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        eventHandler();
        setStatusCntData();
    }

    /**
     * @auther : yukpan
     * @date : 2019.06.28
     * @explain : 이벤트를 정의한다.
     **/
    public void eventHandler(){
        ImageView menu1 = (ImageView)findViewById(R.id.a003_menu1);
        ImageView menu2 = (ImageView)findViewById(R.id.a003_menu2);
        ImageView menu3 = (ImageView)findViewById(R.id.a003_menu3);
        ImageView menu4 = (ImageView)findViewById(R.id.a003_menu4);
        ImageView menu5 = (ImageView)findViewById(R.id.a003_menu5);
        ImageView menu6 = (ImageView)findViewById(R.id.a003_menu6);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.menu1) {
                    // Handle the camera action
                    Log.d("eaccountLog","logout!");

                    Util util = new Util(getApplicationContext());
                    util.clearLoginData();

                    Intent intent = new Intent(getApplicationContext(), A001Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Singleton.getInstance().removeAllActivityStack();

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navigationView.showContextMenu();


        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), B001Activity.class);
//                startActivity(intent);
//
//                Singleton.getInstance().addActivityStack(A003Activity.this);
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), B001Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), B002Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), B003Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), B004Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

        menu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), B005Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });


    }

    /**
     * @auther : yukpan
     * @date : 2019.06.28
     * @explain : 상태값을 세팅한다.
     **/
    public void setStatusCntData(){

        final TextView O_GUNSU1 = (TextView) findViewById(R.id.a003_o_gunsu1);
        final TextView O_GUNSU2 = (TextView) findViewById(R.id.a003_o_gunsu2);
        final TextView O_GUNSU3 = (TextView) findViewById(R.id.a003_o_gunsu3);
        final TextView O_GUNSU4 = (TextView) findViewById(R.id.a003_o_gunsu4);

        UserVo userVo =  Singleton.getInstance().getUserVo();

        HashMap<String, Object> param = new HashMap<String,Object>();

        param.put("tenantId", userVo.getTenantId());
        param.put("I_BUKRS", userVo.getBukrs());
        param.put("I_PERNR", userVo.getPernr());

        param.put("I_SDATE", "");
        param.put("I_EDATE", "");

        Log.d("eaccountLog", param.toString());

        Call<JsonObject> res = ERetrofit.getInstance().getService().getZFI_ED_M_001(param);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                HashMap<String,Object> resultMap = Util.getData(response);
                Log.d("eaccountLog",resultMap.toString());
                String resultYn = (String)resultMap.get("resultYn");
                HashMap<String,Object> statusCntData = (HashMap<String,Object>)resultMap.get("statusCntData");

                if("Y".equals(resultYn)){
                    O_GUNSU1.setText(statusCntData.get("O_GUNSU1").toString());
                    O_GUNSU2.setText(statusCntData.get("O_GUNSU2").toString());
                    O_GUNSU3.setText(statusCntData.get("O_GUNSU3").toString());
                    O_GUNSU4.setText(statusCntData.get("O_GUNSU4").toString());

                }else{
//                    EDialog eDialog = new EDialog(getApplicationContext(), A002Activity.this);
//                    eDialog.setMessage(getString(R.string.a002_login_fail_msg));
//                    eDialog.showAlertDialog(new EDialog.DialogCallback() {
//                        @Override
//                        public void positiveBtnClick() {
//                        }
//                        @Override
//                        public void negativeBtnClick() {
//                        }
//                    });
//                    return;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){

        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.menu1) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

}

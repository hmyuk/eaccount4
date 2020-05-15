package com.isu.erp.eaccount.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isu.erp.eaccount.R;
import com.isu.erp.eaccount.common.Singleton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Singleton.getInstance().addActivityStack(CalendarActivity.this);
        TextView toolbarText = (TextView)findViewById(R.id.toolbar_text);
        toolbarText.setText(getString(R.string.toolbar_calendar_title));
        calendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        eventHandler();
    }

    public void eventHandler(){
        View backBox = (View)findViewById(R.id.back_box);

        //back btn
        backBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity를 string으로 만들어야하는데.
                Intent intent = new Intent();
//                ComponentName name = new ComponentName("com.isu.erp.eaccount.activity","com.isu.erp.eaccount.activity.B001Activity");
//                intent.setComponent(name);
                setResult(Activity.RESULT_OK);
                Singleton.getInstance().removeActivityStack(CalendarActivity.this);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                String month =  (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : (date.getMonth()+1)+"";
                String day = date.getDay() < 10 ? "0"+date.getDay() : date.getDay()+"";

                Intent intent = new Intent();
                intent.putExtra("selectedDate", date.getYear() +"-"+ month +"-"+ day );
                setResult(Activity.RESULT_OK, intent);
                Singleton.getInstance().removeActivityStack(CalendarActivity.this);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

    }


    private class SundayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }


    private class SaturdayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    private class OneDayDecorator implements DayViewDecorator {
        private CalendarDay date;

        public OneDayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
            view.addSpan(new ForegroundColorSpan(Color.GREEN));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }
}

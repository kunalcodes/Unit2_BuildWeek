package com.example.taskmanager_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity {
private EditText mEtCreateTaskDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mEtCreateTaskDate=findViewById(R.id.etCreateTaskDate);
        mEtCreateTaskDate.setInputType(InputType.TYPE_NULL);
        mEtCreateTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoeDateTimeDialog(mEtCreateTaskDate);
            }
        });

    }

    private void shoeDateTimeDialog(EditText mEtCreateTaskDate) {
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                       calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yy :: HH:mm");
                        mEtCreateTaskDate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                setAlarm(calendar.getTimeInMillis());
                new TimePickerDialog(CreateTaskActivity.this,R.style.TimePickerTheme,onTimeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();

            }
        };
        new DatePickerDialog(CreateTaskActivity.this,R.style.TimePickerTheme,dateSetListener,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setAlarm(long timeInMillis){
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent AlarmIntent=new Intent(this,AlarmBroadCastReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,AlarmIntent,0);
        alarmManager.setRepeating(AlarmManager.RTC,timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this,"New Task Created ",Toast.LENGTH_SHORT).show();
    }
}
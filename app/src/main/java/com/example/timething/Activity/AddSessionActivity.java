package com.example.timething.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timething.DbUtil;
import com.example.timething.R;
import com.example.timething.model.Session;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddSessionActivity extends AppCompatActivity {

    CalendarView calSessionDate;

    TextView txtStartHour;
    Spinner spnStartMin;

    RadioGroup radgpStartAmPm;
    RadioButton radStartAm;
    RadioButton radStartPm;

    RadioGroup radgpFinishAmPm;
    RadioButton radFinishAm;
    RadioButton radFinishPm;

    TextView txtFinishHour;
    Spinner spnFinishMin;

    Button btnAdd;
    Button btnBack;

    SimpleDateFormat sdfMMMDddYyy = new SimpleDateFormat("MMM/dd/yyy");
    private final DateTimeFormatter dtfHrsMins = DateTimeFormatter.ofPattern("hh:mm a");


    int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);

        calSessionDate = findViewById(R.id.cal_dateSelect);

        txtStartHour = findViewById(R.id.txt_start_time);
        spnStartMin = findViewById(R.id.spn_start_time_mins);

        radgpStartAmPm = findViewById(R.id.radgp_start_amPm);
        radStartAm = findViewById(R.id.rad_start_am);
        radStartPm = findViewById(R.id.rad_start_pm);

        txtFinishHour = findViewById(R.id.txt_finish_time);
        spnFinishMin = findViewById(R.id.spn_finish_time_mins);

        radgpFinishAmPm = findViewById(R.id.radgp_finish_amPm);
        radFinishAm = findViewById(R.id.rad_finish_am);
        radFinishPm = findViewById(R.id.rad_finish_pm);

        btnAdd = findViewById(R.id.btn_addSession);
        btnBack = findViewById(R.id.btn_back);

        DbUtil db = new DbUtil(AddSessionActivity.this);

        Intent intent = getIntent();
        jobId = intent.getIntExtra("job_id", 0);

        //TODO: Input validation
        btnAdd.setOnClickListener(view -> {
            if (txtStartHour.getText().toString().equals("") && txtFinishHour.getText().toString().equals("")) {
                Toast.makeText(AddSessionActivity.this, "Please specify a start time and a finish time", Toast.LENGTH_LONG).show();
            }
            else if (txtStartHour.getText().toString().equals("")) {
                Toast.makeText(AddSessionActivity.this, "Please specify a start time", Toast.LENGTH_LONG).show();
            }
            else if (txtFinishHour.getText().toString().equals("")) {
                Toast.makeText(AddSessionActivity.this, "Please specify a finish time", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    int startHour = Integer.parseInt(txtStartHour.getText().toString());
                    int finishHour = Integer.parseInt(txtFinishHour.getText().toString());

                    String startAmPm = "";
                    String finishAmPm = "";

                    if (radStartAm.isChecked())
                        startAmPm = " a.m.";
                    else if (radStartPm.isChecked())
                        startAmPm = " a.m.";

                    if (radFinishAm.isChecked())
                        finishAmPm = " a.m.";
                    else if (radFinishPm.isChecked())
                        finishAmPm = " a.m.";

                    String startTimeString = startHour + spnStartMin.getSelectedItem().toString() + startAmPm;
                    String finishTimeString = finishHour + spnFinishMin.getSelectedItem().toString() + finishAmPm;

                    LocalDateTime startTime = LocalDateTime.parse(startTimeString, dtfHrsMins);
                    LocalDateTime finishTime = LocalDateTime.parse(finishTimeString, dtfHrsMins);

                    Session sesh = new Session(LocalDateTime.ofInstant(Instant.ofEpochMilli(calSessionDate.getDate()), TimeZone.getDefault().toZoneId()), startTime, finishTime, jobId);
                    db.AddSession(sesh);

                    Toast.makeText(AddSessionActivity.this, "Session added successfully", Toast.LENGTH_LONG).show();

                }
                catch (NumberFormatException e) {
                    Toast.makeText(AddSessionActivity.this, "Start time and finish time must be whole numbers between 1 and 12", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBack.setOnClickListener(view -> {
            Intent i = new Intent (AddSessionActivity.this, ViewJobActivity.class);
            i.putExtra("job_id", jobId);

            startActivity(i);
        });
    }
}
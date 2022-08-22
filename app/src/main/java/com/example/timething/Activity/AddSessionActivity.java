package com.example.timething.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import com.example.timething.model.Job;
import com.example.timething.model.Session;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    final DateTimeFormatter dtfMMMDddYyy = DateTimeFormatter.ofPattern("MMM/dd/yyy");
    private final DateTimeFormatter dtfHrsMins = DateTimeFormatter.ofPattern("hh:mm a");

    String selectedDate = LocalDate.now().toString();
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
            else if (txtStartHour.getText().toString().equals("") ) {
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

                    if (radStartPm.isChecked())
                        startHour += 12;


                    if (radFinishPm.isChecked())
                        finishHour += 12;

                    String startTimeString = startHour + spnStartMin.getSelectedItem().toString();
                    String finishTimeString = finishHour + spnFinishMin.getSelectedItem().toString();

                    LocalTime startTime = LocalTime.parse(startTimeString);
                    LocalTime finishTime = LocalTime.parse(finishTimeString);

                    Session sesh = new Session(LocalDate.parse(selectedDate), startTime, finishTime, jobId);
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

        calSessionDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                selectedDate = month + "-" + dayOfMonth + "-" + year;
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        DbUtil db = new DbUtil(AddSessionActivity.this);

        Session sesh;
        // Check which radio button was clicked
        if (checked) {
            if (view.getId() == radStartAm.getId()) {

            } else if (view.getId() == radStartPm.getId()) {

            } else if (view.getId() == radFinishAm.getId()) {

            } else if (view.getId() == radFinishPm.getId()) {

            }
        }
    }
}
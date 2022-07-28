package com.example.timething;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timething.model.Session;

import java.text.SimpleDateFormat;

public class AddSessionActivity extends AppCompatActivity {

    CalendarView calSessionDate;

    TextView txtHours;
    Spinner spnHourDecimal;

    Button btnAdd;
    Button btnBack;

    SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyy");

    int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);

        calSessionDate = findViewById(R.id.cal_dateSelect);

        txtHours = findViewById(R.id.txt_duration);
        spnHourDecimal = findViewById(R.id.spn_durationDecimal);

        btnAdd = findViewById(R.id.btn_addSession);
        btnBack = findViewById(R.id.btn_back);

        DbUtil db = new DbUtil(AddSessionActivity.this);

        Intent intent = getIntent();
        jobId = intent.getIntExtra("job_id", 0);


        btnAdd.setOnClickListener(view -> {
            if (txtHours.getText().toString().equals("")) {
                Toast.makeText(AddSessionActivity.this, "Please specify a duration", Toast.LENGTH_LONG).show();
            }
            else {
                String durationString = txtHours.getText().toString() + spnHourDecimal.getSelectedItem().toString();
                double duration = Double.parseDouble(durationString);

                Session sesh = new Session(duration, sdf.format(calSessionDate.getDate()), jobId);
                db.AddSession(sesh);

                Toast.makeText(AddSessionActivity.this, "Session added successfully", Toast.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(view -> {
            Intent i = new Intent (AddSessionActivity.this, ViewJobActivity.class);
            i.putExtra("job_id", jobId);

            startActivity(i);
        });
    }
}
package com.example.timething;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timething.Activity.ClientsActivity;
import com.example.timething.Activity.JobsActivity;
import com.example.timething.model.Client;
import com.example.timething.model.Job;
import com.example.timething.model.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private Button btnStart;
    private Button btnPause;

    private Spinner spnSavedJobs;
    private Spinner spnSavedClients;

    private TextView txtOnTheClock;
    private TextView txtStartTime;

    private boolean timerRunning = false;

    private final DateTimeFormatter dtfHrsMins = DateTimeFormatter.ofPattern("hh:mm a");
    private final DateTimeFormatter dtfMmmDdYyy = DateTimeFormatter.ofPattern("MMM/dd/yyy");

    private final double ROUND_FACTOR = 4.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        Button btnLog = findViewById(R.id.btn_log);

        spnSavedJobs = findViewById(R.id.spn_savedJobs);
        spnSavedClients = findViewById(R.id.spn_savedClients);

        Button btnClients = findViewById(R.id.btn_clients);
        Button btnJobs = findViewById(R.id.btn_jobs);

        Button btnReset = findViewById(R.id.btn_reset);

        txtOnTheClock = findViewById(R.id.txt_onTheClock);
        txtStartTime = findViewById(R.id.txt_startTime);

        RefreshClientSpinner();

        if (spnSavedClients.getSelectedItem() != null) RefreshJobSpinner();

        //TODO: Keep clock ticking when on another activity or screen is off or in another app
        //Starts the clock. Records a new startTime if starting a new session.
        btnStart.setOnClickListener(view -> {
            if (!timerRunning) {
                if (ClockBackground.startTime == null) {
                    ClockBackground.startTime = LocalDateTime.now();
                    txtStartTime.setText(getString(R.string.start_time, LocalDateTime.now().format(dtfHrsMins)));
                    txtStartTime.setVisibility(View.VISIBLE);
                }
                else
                    ClockBackground.pauseDuration += Duration.between(ClockBackground.pauseTime, LocalDateTime.now()).toMillis();

                StartTimer();
            }
        });

        //Stops the clock
        btnPause.setOnClickListener(view -> {
            if (timerRunning) StopTimer();
        });

        //Resets the clock
        btnReset.setOnClickListener(view -> ResetTimer());

        //Resets clock and records session to job table
        btnLog.setOnClickListener(view -> {
            if (spnSavedJobs.getSelectedItem() == null)
                Toast.makeText(MainActivity.this, "Please select or add a job", Toast.LENGTH_LONG).show();

            else if (ClockBackground.startTime == null)
                Toast.makeText(MainActivity.this, "No time to log", Toast.LENGTH_LONG).show();

            else {
                DbUtil db = new DbUtil(MainActivity.this);

                Job job = (Job) spnSavedJobs.getSelectedItem();
                LocalDateTime now = LocalDateTime.now();

//                //Determine timeWorkedInMillis
//                long timeWorkedInMillis = Duration.between(ClockBackground.startTime, now).toMillis();//TODO: get java dividing properly on ln112 and get pause time working
//                timeWorkedInMillis -= ClockBackground.pauseDuration;    //Subtract pauseDuration, default 0
//                double timeWorkedInHours = (long)((float) timeWorkedInMillis / 3600000);       //Millis / 3600000 = Hours
//                timeWorkedInHours = Math.round(timeWorkedInHours * ROUND_FACTOR) / ROUND_FACTOR;    //Round hours to nearest quarter hour

                Session sesh = new Session(LocalDateTime.now(), ClockBackground.startTime, LocalDateTime.now(), job.getId());

                db.AddSession(sesh);
                Toast.makeText(MainActivity.this, sesh.getDuration() + "hrs. logged to " + job.getName(), Toast.LENGTH_LONG).show();
                ResetTimer();
            }
        });

        btnClients.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, ClientsActivity.class);
            startActivity(i);
        });

        btnJobs.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, JobsActivity.class);
            startActivity(i);
        });

        spnSavedClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RefreshJobSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        ClockBackground.clockRunning = timerRunning;

        if (timerRunning)
            ClockBackground.pauseTime = LocalDateTime.now();
    }

    @Override
    public void onResume(){
        super.onResume();

        timerRunning = ClockBackground.clockRunning;

        if (ClockBackground.pauseTime != null && timerRunning)
            ClockBackground.timeAway = Duration.between(ClockBackground.pauseTime, LocalDateTime.now()).toMillis();

        if (ClockBackground.startTime != null) {
            txtStartTime.setText(getString(R.string.start_time, ClockBackground.startTime.format(dtfHrsMins)));
            txtStartTime.setVisibility(View.VISIBLE);
        }

        if (timerRunning)
            StartTimer();
        else
            StopTimer();

        RefreshClientSpinner();

        if (spnSavedClients.getSelectedItem() != null)
            RefreshJobSpinner();
    }

    private void RefreshJobSpinner() {
        DbUtil db = new DbUtil(getApplicationContext());
        Client client = (Client) spnSavedClients.getSelectedItem();

        List<Job> jobs = db.GetIncompleteJobsFromClientId(client.getId());

        ArrayAdapter<Job> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, jobs);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnSavedJobs.setAdapter(dataAdapter);
    }

    private void RefreshClientSpinner() {
        DbUtil db = new DbUtil(getApplicationContext());
        List<Client> clients = db.GetAllClients();

        ArrayAdapter<Client> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, clients);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnSavedClients.setAdapter(dataAdapter);
    }

    private void StopTimer() {
        ClockBackground.pauseTime = LocalDateTime.now();

        btnPause.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.VISIBLE);
        txtOnTheClock.setVisibility(View.INVISIBLE);

        timerRunning = false;
        ClockBackground.clockRunning = false;
    }

    private void StartTimer() {
        btnPause.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtOnTheClock.setVisibility(View.VISIBLE);

        timerRunning = true;
        ClockBackground.clockRunning = true;
    }

    private void ResetTimer() {
        StopTimer();
        txtStartTime.setVisibility(View.INVISIBLE);

        ClockBackground.pauseTime = null;
        ClockBackground.startTime = null;
        ClockBackground.timeAway = 0;
        ClockBackground.pauseDuration = 0;
        ClockBackground.clockRunning = false;
    }
}
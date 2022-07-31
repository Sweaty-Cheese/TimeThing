package com.example.timething.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timething.DbUtil;
import com.example.timething.MainActivity;
import com.example.timething.R;
import com.example.timething.model.Client;
import com.example.timething.model.Job;

import java.util.List;

public class JobsActivity extends AppCompatActivity {
    ListView lstJobs;
    Spinner spnClients;
    TextView txtJobName;
    Button btnAddJob;

    Button btnTimer;

    private final int FALSE = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        lstJobs = findViewById(R.id.lstJobs);
        spnClients = findViewById(R.id.spn_clients);
        txtJobName = findViewById(R.id.txt_jobName);
        btnAddJob = findViewById(R.id.btn_addJob);

        btnTimer = findViewById(R.id.btn_mainFromJobs);

        RefreshClientSpinner();

        spnClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RefreshJobList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btnAddJob.setOnClickListener(view -> {
            if (txtJobName.getText().toString().equals("")) {
                Toast.makeText(JobsActivity.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
            }
            else {
                DbUtil db = new DbUtil(JobsActivity.this);
                Client client = (Client) spnClients.getSelectedItem();
                Job job = new Job(txtJobName.getText().toString(), client.getId(), FALSE);

                db.AddJob(job);
                RefreshJobList();
                Toast.makeText(JobsActivity.this, "Job created under " + client.getName(), Toast.LENGTH_LONG).show();
            }
        });

        lstJobs.setOnItemClickListener((adapterView, view, i, l) -> {
            Job job = (Job) adapterView.getItemAtPosition(i);

            Intent intent = new Intent(JobsActivity.this, ViewJobActivity.class);
            intent.putExtra("job_id", job.getId());
            startActivity(intent);
        });

        lstJobs.setOnItemLongClickListener((adapterView, view, i, l) -> {
            DbUtil db = new DbUtil(JobsActivity.this);
            Job job = (Job) adapterView.getItemAtPosition(i);

            db.DeleteJob(job.getId());
            RefreshJobList();
            return false;
        });

        btnTimer.setOnClickListener(view -> {
            Intent intent = new Intent(JobsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RefreshClientSpinner();
        RefreshJobList();
    }

    public void RefreshClientSpinner() {
        DbUtil db = new DbUtil(getApplicationContext());
        List<Client> clients = db.GetAllClients();

        ArrayAdapter<Client> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, clients);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnClients.setAdapter(dataAdapter);
    }

    public void RefreshJobList() {
        DbUtil db = new DbUtil(JobsActivity.this);
        Client client = (Client) spnClients.getSelectedItem();
        List<Job> jobs = db.GetJobsFromClientId(client.getId());

        ArrayAdapter<Job> adapter = new ArrayAdapter<>(JobsActivity.this, android.R.layout.simple_list_item_1, jobs);

        lstJobs.setAdapter(adapter);
    }
}
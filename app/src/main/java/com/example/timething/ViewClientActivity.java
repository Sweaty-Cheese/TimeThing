package com.example.timething;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timething.model.Client;
import com.example.timething.model.Job;

import java.util.List;

public class ViewClientActivity extends AppCompatActivity {

    TextView tvClientName;
    TextView txtEditClientName;

    TextView txtAddJob;
    Button btnAddJob;

    Button btnEditClient;
    Button btnSaveClient;

    Button btnTimer;

    ListView lstJobs;

    int clientId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        DbUtil db = new DbUtil(ViewClientActivity.this);

        tvClientName = findViewById(R.id.tv_clientName2);
        txtEditClientName = findViewById(R.id.txt_editName);

        txtAddJob = findViewById(R.id.txt_addJob);
        btnAddJob = findViewById(R.id.btn_addJob2);

        btnEditClient = findViewById(R.id.btn_editClient);
        btnSaveClient = findViewById(R.id.btn_saveClient);

        btnTimer = findViewById(R.id.btn_mainFromClient);

        lstJobs = findViewById(R.id.lst_jobs);

        Intent intent = getIntent();
        clientId = intent.getIntExtra("client_id", 0);
        Client client = db.GetClientFromId(clientId);

        RefreshJobList(clientId);

        tvClientName.setText(client.getName());
        txtEditClientName.setText(client.getName());

        btnEditClient.setOnClickListener(view -> {
            tvClientName.setVisibility(View.INVISIBLE);
            btnEditClient.setVisibility(View.INVISIBLE);

            txtEditClientName.setVisibility(View.VISIBLE);
            btnSaveClient.setVisibility(View.VISIBLE);
        });

        btnSaveClient.setOnClickListener(view -> {
            if (txtEditClientName.getText().toString().equals("")) {
                Toast.makeText(ViewClientActivity.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
            }
            else {
                tvClientName.setVisibility(View.VISIBLE);
                btnEditClient.setVisibility(View.VISIBLE);

                txtEditClientName.setVisibility(View.INVISIBLE);
                btnSaveClient.setVisibility(View.INVISIBLE);

                client.setName(txtEditClientName.getText().toString());
                db.UpdateClient(client);
                tvClientName.setText(client.getName());
                txtEditClientName.setText(client.getName());
                Toast.makeText(ViewClientActivity.this, "Client successfully updated", Toast.LENGTH_LONG).show();
            }
        });

        btnAddJob.setOnClickListener(view -> {
            if (txtAddJob.getText().toString().equals("")) {
                Toast.makeText(ViewClientActivity.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
            }
            else {
                Job job = new Job(txtAddJob.getText().toString(), clientId, db.FALSE);

                db.AddJob(job);

                Toast.makeText(ViewClientActivity.this, "Job created under " + client.getName(), Toast.LENGTH_LONG).show();
                txtAddJob.setText("");
                RefreshJobList(clientId);
            }
        });

        lstJobs.setOnItemClickListener((adapterView, view, i, l) -> {
            Job job = (Job) adapterView.getItemAtPosition(i);

            Intent intent1 = new Intent(ViewClientActivity.this, ViewJobActivity.class);
            intent1.putExtra("job_id", job.getId());
            startActivity(intent1);
        });

        lstJobs.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Job job = (Job) adapterView.getItemAtPosition(i);

            db.DeleteJob(job.getId());
            RefreshJobList(clientId);
            return false;
        });

        btnTimer.setOnClickListener(view -> {
            Intent intent12 = new Intent(ViewClientActivity.this, MainActivity.class);
            startActivity(intent12);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RefreshJobList(clientId);
    }

    public void RefreshJobList(int id) {
        DbUtil db = new DbUtil(ViewClientActivity.this);
        List<Job> jobs = db.GetJobsFromClientId(id);

        ArrayAdapter<Job> adapter = new ArrayAdapter<>(ViewClientActivity.this, android.R.layout.simple_list_item_1, jobs);

        lstJobs.setAdapter(adapter);
    }
}
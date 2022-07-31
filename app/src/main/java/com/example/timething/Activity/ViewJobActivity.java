package com.example.timething.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timething.DbUtil;
import com.example.timething.MainActivity;
import com.example.timething.R;
import com.example.timething.model.Client;
import com.example.timething.model.Job;
import com.example.timething.model.Session;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewJobActivity extends AppCompatActivity {

    TextView txtJobName;
    TextView txtEditJobName;

    TextView txtClientName;
    Spinner spnClientName;

    RadioGroup radgpTrueFalse;
    RadioButton radTrue;
    RadioButton radFalse;

    ListView lstSessions;

    Button btnEditJob;
    Button btnSave;

    Button btnAddSession;

    Button btnTimer;

    Boolean inEditMode = false;

    int jobId;

    private final int TRUE = 1;
    private final int FALSE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);

        DbUtil db = new DbUtil(ViewJobActivity.this);

        txtJobName = findViewById(R.id.tv_JobName);
        txtEditJobName = findViewById(R.id.txt_editJobName);

        txtClientName = findViewById(R.id.tv_ClientName);
        spnClientName = findViewById(R.id.spn_changeClient);

        radgpTrueFalse = findViewById(R.id.radgp_trueFalse);
        radTrue = findViewById(R.id.rad_True);
        radFalse = findViewById(R.id.rad_False);

        lstSessions = findViewById(R.id.lstSessions);
        lstSessions.setLongClickable(true);

        btnEditJob = findViewById(R.id.btn_EditJob);
        btnSave = findViewById(R.id.btn_SaveJob);

        btnAddSession = findViewById(R.id.btn_AddSession);

        btnTimer = findViewById(R.id.btn_mainFromJob);

        Intent intent = getIntent();
        jobId = intent.getIntExtra("job_id", 0);

        Job job = db.GetJobFromId(jobId);

        txtJobName.setText(job.getName());
        txtEditJobName.setText(job.getName());

        Client client = db.GetClientFromId(job.getClient_id());
        txtClientName.setText(client.getName());
        RefreshClientSpinner(client);

        if (job.isJob_completed() == TRUE) {
            radTrue.setChecked(true);
            radFalse.setChecked(false);
        }
        else {
            radFalse.setChecked(true);
            radTrue.setChecked(false);
        }

        RefreshSessionList(jobId);

        btnEditJob.setOnClickListener(view -> {
            txtJobName.setVisibility(View.INVISIBLE);
            txtClientName.setVisibility(View.INVISIBLE);
            btnEditJob.setVisibility(View.INVISIBLE);

            txtEditJobName.setVisibility(View.VISIBLE);
            spnClientName.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);

            inEditMode = true;
        });

        btnSave.setOnClickListener(view -> {
            if (txtEditJobName.getText().toString().equals("")) {
                Toast.makeText(ViewJobActivity.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
            }
            else {
                Client newClient = (Client) spnClientName.getSelectedItem();
                job.setName(txtEditJobName.getText().toString());
                job.setClient_id(newClient.getId());

                if (radTrue.isChecked())
                    job.setJob_completed(TRUE);
                else
                    job.setJob_completed(FALSE);

                db.UpdateJob(job);

                txtJobName.setText(job.getName());
                txtEditJobName.setText(job.getName());

                txtClientName.setText(newClient.getName());
                //spnClientName.setSelection(((ArrayAdapter)spnClientName.getAdapter()).getPosition(newClient));

                txtJobName.setVisibility(View.VISIBLE);
                txtClientName.setVisibility(View.VISIBLE);
                btnEditJob.setVisibility(View.VISIBLE);

                txtEditJobName.setVisibility(View.INVISIBLE);
                spnClientName.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.INVISIBLE);

                inEditMode = false;

                Toast.makeText(ViewJobActivity.this, "Job information successfully updated", Toast.LENGTH_LONG).show();
            }
        });

        btnAddSession.setOnClickListener(view -> {
            Intent intent1 = new Intent(ViewJobActivity.this, AddSessionActivity.class);
            intent1.putExtra("job_id", job.getId());
            startActivity(intent1);
        });

        btnTimer.setOnClickListener(view -> {
            Intent intent12 = new Intent(ViewJobActivity.this, MainActivity.class);
            startActivity(intent12);
        });

        lstSessions.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Session sesh = (Session) adapterView.getItemAtPosition(i);

            db.DeleteSession(sesh.getId());
            RefreshSessionList(job.getId());
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshSessionList(jobId);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        DbUtil db = new DbUtil(ViewJobActivity.this);

        Job job = db.GetJobFromId(jobId);
        // Check which radio button was clicked
        if (!inEditMode && checked) {
            if (view.getId() == radTrue.getId()) {
                job.setJob_completed(TRUE);
                db.UpdateJob(job);

                Toast.makeText(ViewJobActivity.this, "Job saved as completed", Toast.LENGTH_LONG).show();
            } else if (view.getId() == radFalse.getId()) {
                job.setJob_completed(FALSE);
                db.UpdateJob(job);

                Toast.makeText(ViewJobActivity.this, "Job saved as incomplete", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void RefreshClientSpinner(Client client) {
        DbUtil db = new DbUtil(getApplicationContext());
        List<Client> clients = db.GetAllClients();
        List<String> names = db.GetAllClientNames();

        ArrayAdapter<Client> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, clients);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);

        nameAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnClientName.setAdapter(dataAdapter);

        int position = nameAdapter.getPosition(client.getName());

        spnClientName.setSelection(position);
    }

    public void RefreshSessionList(int id) {
        DbUtil db = new DbUtil(ViewJobActivity.this);
        List<Session> sessions = db.GetSessionsFromJobId(id);

        ArrayAdapter<Session> adapter = new ArrayAdapter<>(ViewJobActivity.this, android.R.layout.simple_list_item_1, sessions);

        lstSessions.setAdapter(adapter);
    }
}
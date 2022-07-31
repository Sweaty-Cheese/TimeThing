package com.example.timething.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timething.DbUtil;
import com.example.timething.MainActivity;
import com.example.timething.R;
import com.example.timething.model.Client;

import java.util.List;

public class ClientsActivity extends AppCompatActivity {
    private ListView lstClients;
    Button btnAddClient;
    TextView txtClientName;

    Button btnBackToTimer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        lstClients = findViewById(R.id.lstClients);
        btnAddClient = findViewById(R.id.btn_addClient);
        txtClientName = findViewById(R.id.txt_clientName);

        btnBackToTimer = findViewById(R.id.btn_mainFromClients);

        UpdateList();
        //Pass the id of the selected client to the Jobs activity
        lstClients.setOnItemClickListener((parent, view, position, id) -> {
            Client client = (Client) parent.getItemAtPosition(position);

            Intent intent = new Intent(ClientsActivity.this, ViewClientActivity.class);
            intent.putExtra("client_id", client.getId());
            startActivity(intent);
        });

        lstClients.setOnItemLongClickListener((adapterView, view, i, l) -> {
            DbUtil db = new DbUtil(ClientsActivity.this);
            Client client = (Client) adapterView.getItemAtPosition(i);

            db.DeleteClient(client.getId());
            UpdateList();
            return false;
        });


        btnAddClient.setOnClickListener(view -> {
            if (txtClientName.getText().toString().equals("")) {
                Toast.makeText(ClientsActivity.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
            }
            else {
                Client client;
                DbUtil db = new DbUtil(ClientsActivity.this);

                client = new Client(txtClientName.getText().toString());
                db.AddClient(client);
                Toast.makeText(ClientsActivity.this, "Client added", Toast.LENGTH_LONG).show();
                UpdateList();
            }
        });

        btnBackToTimer.setOnClickListener(view -> {
            Intent intent = new Intent(ClientsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void UpdateList() {
        DbUtil db = new DbUtil(ClientsActivity.this);
        List<Client> clients = db.GetAllClients();

        ArrayAdapter<Client> adapter = new ArrayAdapter<>(ClientsActivity.this, android.R.layout.simple_list_item_1, clients);

        lstClients.setAdapter(adapter);
    }
}
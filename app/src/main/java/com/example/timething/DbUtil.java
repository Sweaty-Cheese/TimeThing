package com.example.timething;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.timething.model.Client;
import com.example.timething.model.Job;
import com.example.timething.model.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DbUtil extends SQLiteOpenHelper {
    public static final String client_table = "client_table";
    public static final String client_id = "client_id";
    public static final String client_name = "client_name";

    public static final String job_table = "job_table";
    public static final String job_id = "job_id";
    public static final String job_name = "job_name";
    //client_id
    public static final String job_completed = "job_completed";

    public static final String session_table = "session_table";
    public static final String session_id = "session_id";
    public static final String session_duration = "session_duration";
    public static final String session_date = "session_date";
    public static final String session_start = "session_start";
    public static final String session_end = "session_end";
    //job_id

    final int TRUE = 1;
    final int FALSE = 0;

    private final DateTimeFormatter dtfHrsMins = DateTimeFormatter.ofPattern("hh:mm A");
    private final DateTimeFormatter dtfMmmDdYyy = DateTimeFormatter.ofPattern("MMM/dd/yyy");

    public DbUtil(@Nullable Context context) {
        super(context,  "jobs.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createClientTable = "CREATE TABLE " + client_table + " (" +
                client_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                client_name + " TEXT)";
        db.execSQL(createClientTable);

        String createJobTable = "CREATE TABLE " + job_table + " (" +
                job_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                job_name + " TEXT, " +
                client_id + " INTEGER, " +
                job_completed + " INTEGER, " +
                "FOREIGN KEY (" + client_id + ") " +
                    "REFERENCES " + client_table + " (" + client_id + "))";
        db.execSQL(createJobTable);

        String createSessionTable = "CREATE TABLE " + session_table + " (" +
                session_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                session_duration + " REAL," +
                session_date + " TEXT," +
                session_start + " TEXT," +
                session_end + " TEXT," +
                job_id + " INTEGER," +
                "FOREIGN KEY (" + job_id + ")" +
                    "REFERENCES " + job_table + "(" + job_id + "))";
        db.execSQL(createSessionTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + client_table);
        db.execSQL("DROP TABLE IF EXISTS " + job_table);
        db.execSQL("DROP TABLE IF EXISTS " + session_table);
        onCreate(db);
    }

    public void AddClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(client_name, client.getName());

        db.insert(client_table, null, cv);
        db.close();
    }

    public Client GetClientFromId(int id) {
        Client client = new Client();

        String selectQuery = "SELECT * FROM " + client_table + " WHERE " + client_id + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);

            client = new Client (id, name);
        }
        cursor.close();
        db.close();

        return client;
    }

    public List<Client> GetAllClients(){
        List<Client> lstClient = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + client_table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Client client;
            do {            //                 client_id              client_name
                client = new Client(cursor.getInt(0), cursor.getString(1));
                lstClient.add(client);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lstClient;
    }

    public void UpdateClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(client_name, String.valueOf(client.getName()));

        String where = client_id +"= ?";
        String[] whereArgs = {String.valueOf(client.getId())};

        db.update(client_table, cv,where,whereArgs);
        db.close();
    }

    public List<String> GetAllClientNames(){
        List<String> lstNames = new ArrayList<>();

        String selectQuery = "SELECT " + client_name + " FROM " + client_table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            String name;
            do {            //       client_name
                name = cursor.getString(0);
                lstNames.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lstNames;
    }

    public void DeleteClient(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + client_table + " WHERE " + client_id + " = " + id;

        db.rawQuery(queryString, null).close();

        db.delete(client_table, client_id + " =?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void AddJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(job_name, job.getName());
        cv.put(client_id, String.valueOf(job.getClient_id()));
        cv.put(job_completed, FALSE);

        db.insert(job_table, null, cv);
        db.close();
    }

//    public List<Job> GetAllJobs(){
//        List<Job> lstJob = new ArrayList<Job>();
//
//        String selectQuery = "SELECT * FROM " + job_table;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            Job job;
//            do {            //              job_id                job_name            client_id         job_completed
//                job = new Job(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
//                lstJob.add(job);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        return lstJob;
//    }

    public void UpdateJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(job_name, String.valueOf(job.getName()));
        cv.put(client_id, String.valueOf(job.getClient_id()));
        cv.put(job_completed, String.valueOf(job.isJob_completed()));

        String where = job_id +"= ?";
        String[] whereArgs = {String.valueOf(job.getId())};

        db.update(job_table, cv,where,whereArgs);
        db.close();
    }

    public Job GetJobFromId(int id){

        Job job = new Job();

        String queryString = "SELECT * FROM " + job_table + " WHERE " + job_id + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst())
        {
            String name = cursor.getString(1);
            int client_id = cursor.getInt(2);
            int job_completed = cursor.getInt(3);

            job = new Job (id, name, client_id, job_completed);

        }
        cursor.close();
        db.close();

        return job;
    }

    public List<Job> GetJobsFromClientId(int id) {
        List<Job> lstJobs = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + job_table + " WHERE " + client_id + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Job job;
            do {            //              job_id                job_name            client_id         job_completed
                job = new Job(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
                lstJobs.add(job);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lstJobs;
    }

    public List<Job> GetIncompleteJobsFromClientId(int id) {
        List<Job> lstJobs = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + job_table + " WHERE " + client_id + " = " + id + " AND " + job_completed + " = " + FALSE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Job job;
            do {            //              job_id                job_name            client_id         job_completed
                job = new Job(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
                lstJobs.add(job);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lstJobs;
    }

    public void DeleteJob(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + job_table + " WHERE " + job_id + " = " + id;

        db.rawQuery(queryString, null).close();

        db.delete(job_table, job_id + " =?", new String[]{String.valueOf(id)});

        db.close();
    }

    public void AddSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        long timeWorkedInMillis = Duration.between(ClockBackground.startTime, LocalDateTime.now()).toMillis();//TODO: get java dividing properly on ln112 and get pause time working
        timeWorkedInMillis -= ClockBackground.pauseDuration;    //Subtract pauseDuration, default 0
        double timeWorkedInHours = timeWorkedInMillis / (double) 3600000;       //Millis / 3600000 = Hours
        timeWorkedInHours = Math.round(timeWorkedInHours * 4.0) / 4.0;    //Round hours to nearest quarter hour

        session.setDuration(timeWorkedInHours);

        ContentValues cv = new ContentValues();

        cv.put(session_duration, session.getDuration());
        cv.put(session_date, session.getDate().toString());
        cv.put(session_start, session.getStartTime().toString());
        cv.put(session_end, session.getFinishTime().toString());
        cv.put(job_id, String.valueOf(session.getJob_id()));

        db.insert(session_table, null, cv);
        db.close();
    }

    public List<Session> GetSessionsFromJobId(int id) {
        List<Session> lstSeshs = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + session_table + " WHERE " + job_id + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Session sesh;
            do {            //              session_id                duration                    date                job_id
                int id1 = cursor.getInt(0);
                double duration = cursor.getDouble(1);
                String date = cursor.getString(2);
                String startTime = cursor.getString(3);
                String finishTime = cursor.getString(4);
                int jobId = cursor.getInt(5);
                sesh = new Session (id1, duration, LocalDateTime.parse(date, dtfMmmDdYyy), LocalDateTime.parse(startTime, dtfHrsMins), LocalDateTime.parse(finishTime, dtfHrsMins), jobId);
                lstSeshs.add(sesh);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return lstSeshs;
    }

    public void DeleteSession(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + session_table + " WHERE " + session_id + " = " + id;

        db.rawQuery(queryString, null).close();

        db.delete(session_table, session_id + " =?", new String[]{String.valueOf(id)});

        db.close();
    }
}

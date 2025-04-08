package com.example.pfbuilder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
//    https://67ec3c59aa794fb3222d4939.mockapi.io/api/jobs/jobs
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Jobs extends AppCompatActivity {


    RecyclerView jobrecyclerview;
    jobAdapter jobAdapter;
    ArrayList<jobmodel> joblist;
    ProgressBar progressBar;

    String jobTitle;
    String url = "https://67ec3c59aa794fb3222d4939.mockapi.io/api/jobs/jobs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jobs);

        jobrecyclerview = findViewById(R.id.jobsrecyclerview);
        progressBar = findViewById(R.id.jobsProgressBar);
        jobrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        joblist = new ArrayList<>();

        jobTitle = getIntent().getStringExtra("jobTitle");

        fetchJobs();











        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchJobs() {
        progressBar.setVisibility(View.VISIBLE);
        joblist.clear();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jobObj = response.getJSONObject(i);

                            // Filter inside the app
                            if (jobObj.getString("title").equalsIgnoreCase(jobTitle)) {
                                String jobicon = jobObj.getString("jobicon");
                                String title = jobObj.getString("title");
                                String company = jobObj.getString("company");
                                String location = jobObj.getString("location");
                                String description = jobObj.getString("description");
                                String salary = jobObj.getString("salary");
                                Log.d("JOB_TITLE_RECEIVED", jobTitle);
                                Log.d("JOB_TITLE_FROM_JSON", jobObj.getString("title"));
                                jobmodel job = new jobmodel(jobicon, title, company, location, description, salary);
                                joblist.add(job);
                            }
                        }

                        if (joblist.isEmpty()) {
                            Toast.makeText(Jobs.this, "No jobs found for this title", Toast.LENGTH_SHORT).show();
                        }

                        jobAdapter = new jobAdapter(joblist, Jobs.this);
                        jobrecyclerview.setAdapter(jobAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Jobs.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    Toast.makeText(Jobs.this, "Failed to load jobs", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });

        queue.add(jsonArrayRequest);
    }

}
package com.example.pfbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class portfolio extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<pfmodel> pflist;
    pfHelper helper;
    pfAdapter adapter;
    Button add_portfolio;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_portfolio);

        recyclerView = findViewById(R.id.pfrecyclerview);
        add_portfolio = findViewById(R.id.add_portfolio);
        helper = new pfHelper(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadData(false); // Initial load

        add_portfolio.setOnClickListener(v -> {
            Intent intent = new Intent(portfolio.this, MainActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Exit app on back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity(); // Exit the app completely
            }
        });
    }

    public void loadData(boolean shouldScrollToLast) {
        try {
            pflist = helper.getAllUsers();
            if (pflist == null) pflist = new ArrayList<>();

            adapter = new pfAdapter(pflist, this);
            recyclerView.setAdapter(adapter);

            if (shouldScrollToLast && pflist.size() > 0) {
                recyclerView.post(() -> layoutManager.scrollToPosition(pflist.size() - 1));
            }

            Log.d("portfolio", "Users loaded: " + pflist.size());
        } catch (Exception e) {
            Log.e("portfolio", "Error fetching users", e);
            Toast.makeText(this, "Database Error! Please try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true); // Scroll to last after add/update
    }
}

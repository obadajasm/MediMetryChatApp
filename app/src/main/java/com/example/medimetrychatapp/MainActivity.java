package com.example.medimetrychatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.medimetrychatapp.adapter.UsersAdapter;
import com.example.medimetrychatapp.model.User;
import com.example.medimetrychatapp.model.UserRresponse;
import com.example.medimetrychatapp.sevices.Api;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements UsersAdapter.OnUserListener {

    private static final String TAG = "MainActivity";
    private UsersAdapter adapter;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        users = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe);
        initRecyclerView();
        getusers();
        //  trigger swipe to refresh and attach the listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // re-call  fetch data
            getusers();
            swipeRefreshLayout.setRefreshing(false);
        });


    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //     init adapter whith recycler view
        adapter = new UsersAdapter(this, users, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onUserClick(int position) {
        Intent intent = new Intent(this, ChatActivity.class);
        User currentUser = users.get(position);

        intent.putExtra("id", currentUser.getId());
        intent.putExtra("name", currentUser.getName());
        intent.putExtra("image", currentUser.getImage());
        startActivity(intent);
    }

    private void getusers() {
        //fetch data by counry
        users.clear();
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call call = api.getUseres();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    UserRresponse userRresponse = (UserRresponse) response.body();

                    if (userRresponse != null) {
                        Log.d(TAG, "onResponse: " + userRresponse.getMessage());

                        users.addAll(userRresponse.getUsers());
                        Log.d(TAG, "onResponse: " + users.size());

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
                progressBar.setVisibility(View.GONE);

            }
        });
    }


}
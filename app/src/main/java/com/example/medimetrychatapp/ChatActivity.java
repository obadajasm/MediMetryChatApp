package com.example.medimetrychatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.medimetrychatapp.adapter.ChatsAdapter;
import com.example.medimetrychatapp.adapter.UsersAdapter;
import com.example.medimetrychatapp.model.Chat;
import com.example.medimetrychatapp.model.ChatResponse;
import com.example.medimetrychatapp.model.PostMessageResponse;
import com.example.medimetrychatapp.model.User;
import com.example.medimetrychatapp.sevices.Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity implements ChatsAdapter.OnchatListener {
    private static final String TAG = "ChatActivity";

    private int id;
    private ProgressBar progressBar;
    private ArrayList<Chat> chats;
    private ChatsAdapter adapter;
    private RecyclerView recyclerView;
    private EditText et;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        chats = new ArrayList<>();
        iv = findViewById(R.id.sendbtn);
        et = findViewById(R.id.ac_et);
        initAppbarAndProgressbar();
        initRecyclerView();
        fetchChats();

        iv.setOnClickListener(view -> {
            String message = et.getText().toString();

            if (!message.equals("")) {
                sendMessage(message);
                et.setText("");
            } else {
                Toast.makeText(ChatActivity.this, "type something !", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void sendMessage(String message) {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    Chat hollerChat =new Chat(message, timeStamp, false);
        chats.add(hollerChat);
        Collections.reverse(chats);
        adapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call call = api.sendMessage(id, message);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    PostMessageResponse postMessageResponse = (PostMessageResponse) response.body();
                    if (postMessageResponse != null) {
                        Chat newChat = postMessageResponse.getChat();

                        chats.remove(hollerChat);
                        adapter.notifyItemChanged(0);

                        chats.add(newChat);
                        Collections.reverse(chats);
                        Log.d(TAG, "onResponse: " + newChat.getMessage());
                        adapter.notifyItemChanged(0);

                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void fetchChats() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call call = api.getChats(id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: dsadsad " + response.raw().body());

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ChatResponse chatResponse = (ChatResponse) response.body();
                    if (chatResponse != null) {
                        Log.d(TAG, "onResponse: " + chatResponse.getChats().size());
                        chats.addAll(chatResponse.getChats());
                        Collections.reverse(chats);
                        Log.d(TAG, "onResponse: " + chats.size());
                        adapter.notifyDataSetChanged();




                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();

            }
        });
    }

    private void initAppbarAndProgressbar() {
        Bundle data = getIntent().getExtras();
        id = data.getInt("id");
        TextView tv = findViewById(R.id.appbar_title);
        tv.setText(data.getString("name"));
        ImageView iv = findViewById(R.id.appbar_iv);
        try {
            Glide.with(this).load(data.get("image")).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar = findViewById(R.id.ac_progressbar);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.ac_rv);
        //     init adapter whith recycler view

        adapter = new ChatsAdapter(chats, ChatActivity.this);
        LinearLayoutManager lm = new LinearLayoutManager(this);

        lm.setReverseLayout(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onChatClick(int position) {
        Toast.makeText(this,  String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}

package com.example.medimetrychatapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medimetrychatapp.R;
import com.example.medimetrychatapp.model.Chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {

    private ArrayList<Chat> chatList;
    private OnchatListener mOnChatListener;

    public ChatsAdapter(ArrayList<Chat> chatList, OnchatListener OnchatListener) {
        this.chatList = chatList;

        this.mOnChatListener = OnchatListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // define what is a card and your click
        private TextView title, date;
        private ImageView check;
        OnchatListener mOnChatListener;

        public MyViewHolder(final View view, final OnchatListener OnchatListener) {
            super(view);
            date = view.findViewById(R.id.mi_date);
            title = view.findViewById(R.id.mi_msg);
            check = view.findViewById(R.id.mi_check);
            this.mOnChatListener = OnchatListener;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            // pass onClick to the activity that implemnts it
            mOnChatListener.onChatClick(getAdapterPosition());

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        // pass the required data to myViewHolder throw the adapter
        return new MyViewHolder(itemView, mOnChatListener);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // show borederd or filled heart as icon
        //depends on if the staion is favourite or not
        Chat currentChat = chatList.get(position);
        //pass the name state to the card
        holder.title.setText(currentChat.getMessage());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat  timeStampformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date nowDate=timeStampformat.parse(timeStampformat.format(Calendar.getInstance().getTime()));
            Date date2 = timeStampformat.parse(currentChat.getCreatedAt());
            long diff = date2.getTime() - nowDate.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            Log.d("d", "onBindViewHolder: "+String.valueOf(diffSeconds)+currentChat.getMessage());
            Log.d("d", "onBindViewHolder: "+String.valueOf(diffMinutes));
            Log.d("d", "onBindViewHolder: "+String.valueOf(diffHours));

            Log.d("d", "onBindViewHolder: "+currentChat.getCreatedAt());
            Log.d("d", "onBindViewHolder: "+currentChat.getUpdatedAt());
           // Log.d("d", "onBindViewHolder: "+currentChat.getCreatedAt());
            if (diffHours >=1){
                holder.date.setText(String.valueOf(diffHours) +" H");
            }else {
                holder.date.setText(String.valueOf(diffMinutes)+" M");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ddd", "onBindViewHolder: "+e.getLocalizedMessage());
        }


        if (currentChat.getIsDeleiverd() ==null){
            holder.check.setImageResource(R.drawable.ic_check);

        }else{
            holder.check.setImageResource(R.drawable.ic_access);

        }
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // create interface to implement in the targeted activity
    public interface OnchatListener {
        void onChatClick(int position);
    }

}

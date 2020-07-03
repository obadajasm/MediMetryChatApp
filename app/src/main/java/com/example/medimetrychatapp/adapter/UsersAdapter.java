package com.example.medimetrychatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.medimetrychatapp.R;
import com.example.medimetrychatapp.model.User;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<User> userList;
    private   OnUserListener mOnUserListener;


    public UsersAdapter(Context mContext, ArrayList<User> userList,  OnUserListener OnUserListener) {
        this.mContext = mContext;
        this.userList = userList;

        this.mOnUserListener=OnUserListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // define what is a card and your click
        private TextView title, count;
        private ImageView thumbnail;
        OnUserListener mOnUserListener;

        public MyViewHolder(final View view, final OnUserListener OnUserListener) {
            super(view);

            title =  view.findViewById(R.id.title);
            thumbnail =  view.findViewById(R.id.thumbnail);
            this.mOnUserListener=OnUserListener;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            // pass onClick to the activity that implemnts it
            mOnUserListener.onUserClick(getAdapterPosition());

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);

        // pass the required data to myViewHolder throw the adapter
        return new MyViewHolder(itemView,mOnUserListener);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // show borederd or filled heart as icon
        //depends on if the staion is favourite or not
        User currentUser = userList.get(position);
                //pass the name state to the card
        holder.title.setText(currentUser.getName());


        //init the placeholder
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.mipmap.placeholder);

        // loading album cover using Glide library
        Glide.with(mContext)
            //    .setDefaultRequestOptions(requestOptions)
                .load(currentUser.getImage())
                .into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
    // create interface to implement in the targeted activity
    public interface OnUserListener{
        void onUserClick(int position);

    }

}

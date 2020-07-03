package com.example.medimetrychatapp.sevices;

import com.example.medimetrychatapp.model.ChatResponse;
import com.example.medimetrychatapp.model.PostMessageResponse;
import com.example.medimetrychatapp.model.UserRresponse;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
     static final String  BASE_URL = "https://assignment.medimetry.in/api/v1/users/";
    @GET("get")
    Call<UserRresponse> getUseres();

    @GET("{userId}/chats")
    Call<ChatResponse> getChats(@Path("userId") int userId);


    @POST ("chat")
    Call<PostMessageResponse> sendMessage(@Query("id") int userid, @Query("message") String message);
}

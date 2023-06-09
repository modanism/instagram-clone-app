package com.example.instagramclone.network

import com.example.instagramclone.LoginResponse
import com.example.instagramclone.StoryResponse
import com.example.instagramclone.network.responses.FileUploadResponse
import com.example.instagramclone.network.responses.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int? = 1
    ) : Call<StoryResponse>

    @GET("stories")
    suspend fun getStoriesPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>
}
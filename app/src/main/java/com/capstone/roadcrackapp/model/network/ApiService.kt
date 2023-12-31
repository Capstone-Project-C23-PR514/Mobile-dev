package com.capstone.roadcrackapp.model.network

import com.capstone.roadcrackapp.model.ResponseRegister
import com.capstone.roadcrackapp.model.response.ResponseReport
import com.capstone.roadcrackapp.model.response.ResponseUpload
import com.capstone.roadcrackapp.model.response.ResponsesLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("nama_lengkap") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ):Response<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(

        @Field("email") email: String,
        @Field("password") password: String
    ): Response<ResponsesLogin>

    @GET("/")
    suspend fun getAllReports(
        @Header("Authorization") token: String
    ): Response<ResponseReport>

    @Multipart
    @POST("predict")
    suspend fun getUpload(
        @Header("Authorization") token: String,
        @Part("judul") judul: RequestBody,
        @Part filePart: MultipartBody.Part,
        @Part("lokasi") lokasi: RequestBody,
    ): Response<ResponseUpload>
}
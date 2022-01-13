package com.devclass.apiheartbeat

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var Resolver :ContentResolver ;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Resolver = contentResolver;





        fun addServer(){

            val values = ContentValues()

            values.put(SQLite.NAME, "Varla");
            values.put(SQLite.URL, "rimawi.me");
            values.put(SQLite.SCEHMA, "http");
            values.put(SQLite.PORT, "5050");
            values.put(SQLite.ENDPOINT, "/api/get/pinned_todos");
            values.put(SQLite.METHOD, "post");

            val uri = contentResolver.insert(
                SQLite.CONTENT_URL, values
            )
        }



        Server.pingAll()

    }
    interface Api{
        @GET("/{endpoint}")
        fun get(@Path("endpoint") endpoint : String ): Call<ResponseBody>;

        @POST("/{endpoint}")
        fun post(@Path("endpoint") endpoint : String ): Call<ResponseBody>;
    }

}
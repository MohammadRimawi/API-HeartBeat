package com.devclass.apiheartbeat

import android.annotation.SuppressLint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        fun retrieveServers(): Array<Server>{
            val URL = "content://com.devclass.apiheartbeat.SQLite";
            val servers = Uri.parse(URL)
            var SQLiteResolver = contentResolver.query(servers, null, null, null,null)
            var servers_list = arrayOf<Server>();

            if(SQLiteResolver!=null){
                if(SQLiteResolver?.moveToFirst()){
                    do{
                        servers_list += arrayOf<Server>(Server(
                            name = SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite.NAME) as Int),
                            schema = SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite.SCEHMA) as Int),
                            url = SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite.URL) as Int),
                            method = SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite.METHOD) as Int),
                            endpoint = SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite.ENDPOINT) as Int),
                            port=SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite.PORT) as Int)
                        ));
                    }while (SQLiteResolver?.moveToNext());
                }
            }
            return servers_list;
        }

       var servers = retrieveServers()

        for (server in servers) {
            try {
                val retro: Retrofit = Retrofit.Builder().baseUrl(server.BaseUrl()).build();
                val api: Api = retro.create();
                when(server.method){
                    "GET" -> api.get(server.endpoint).enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                println(server.route() + " Was not sent ")
                                t.printStackTrace()
                            }
                            override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                                println(response.code().toString() + " - " + server.route() )
                            }
                        });
                    "POST" -> api.post(server.endpoint).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            println(server.route() + " Was not sent ")
                            t.printStackTrace()
                        }
                        override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                            println(response.code().toString() + " - " + server.route() )
                        }
                    });
                }
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }

    }
    interface Api{
        @GET("/{endpoint}")
        fun get(@Path("endpoint") endpoint : String ): Call<ResponseBody>;

        @POST("/{endpoint}")
        fun post(@Path("endpoint") endpoint : String ): Call<ResponseBody>;
    }

}
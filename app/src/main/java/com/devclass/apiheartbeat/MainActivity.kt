package com.devclass.apiheartbeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
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

        // create a list of server objects, specifying all required attributes to every server.

        var servers = arrayOf<Server>(
                Server(name = "My CV", schema = "http",url = "rimawi.me",method = "GET"),
                Server(name = "My CV secure", schema = "https",url = "rimawi.me",method = "GET"),
                Server(name = "Varla", schema = "http",url = "rimawi.me",port = "5050",endpoint = "/api/get/pinned_todos",method = "POST")
            );

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
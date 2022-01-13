package com.devclass.apiheartbeat

import android.app.Application
import android.content.Context
import android.net.Uri
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.Exception

import android.content.ContentResolver;

class Server(name:String,schema : String, url : String,port:String = "",endpoint:String = "",method:String = "GET") {

    companion object{
            fun retrieveServers(): Array<Server>{
                val URL = "content://com.devclass.apiheartbeat.SQLite";
                val servers = Uri.parse(URL)
                var SQLiteResolver = MainActivity.Resolver.query(servers, null, null, null,null)
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

        fun pingAll(){
            val servers = retrieveServers();
            for (server in servers) {
                try {
                    val retro: Retrofit = Retrofit.Builder().baseUrl(server.BaseUrl()).build();
                    val api: MainActivity.Api = retro.create();
                    when(server.method){
                        "GET" -> api.get(server.endpoint).enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                println(server.route() + " Was not sent ")
                                t.printStackTrace()
                            }
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                println(response.code().toString() + " - " + server.route() )
                            }
                        });
                        "POST" -> api.post(server.endpoint).enqueue(object :
                            Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                println(server.route() + " Was not sent ")
                                t.printStackTrace()
                            }
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                println(response.code().toString() + " - " + server.route() )
                            }
                        });
                    }
                }
                catch(e: Exception){
                    e.printStackTrace()
                }
            }

        }
    }
    public val name = name;
    public val schema = schema;
    public val url = url
    public val port = port;
    public val endpoint = endpoint;
    public val method = method.uppercase();


    public fun BaseUrl():String{
        return "${schema}://${url}${if(port!=""){":${port}"}else{""}}"
    }

    public fun route():String{
        return "[ ${method} ] - ${BaseUrl()}${endpoint}"
    }


}
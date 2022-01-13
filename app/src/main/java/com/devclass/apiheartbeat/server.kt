package com.devclass.apiheartbeat

import android.app.Application
import android.content.Context
import android.net.Uri
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.Exception

import android.content.ContentResolver;
import android.content.ContentValues
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class Server(name:String,schema : String, url : String,port:String = "",endpoint:String = "",method:String = "GET") {

    companion object{

            fun retrieveServers(id:Int = 0): Array<Server>{


                var URL = "content://com.devclass.apiheartbeat.SQLite/${if(id==0){"servers"}else{"server_id/${id}"}}";
                val servers = Uri.parse(URL)
                var SQLiteResolver = MainActivity.Resolver.query(servers, null,null , null ,null)
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
                    pingOne(server);
                }
                catch(e: Exception){
                    println(server.route() + " Was not sent ")
                }
            }

        }
        private fun pingOne(server:Server){
            val retro: Retrofit = Retrofit.Builder().baseUrl(server.BaseUrl()).build();
            val api: Api = retro.create();
            when(server.method){
                "GET" -> api.get(server.endpoint).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

//                        t.printStackTrace()
                        throw(t);
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

interface Api{
    @GET("/{endpoint}")
    fun get(@Path("endpoint") endpoint : String ): Call<ResponseBody>;

    @POST("/{endpoint}")
    fun post(@Path("endpoint") endpoint : String ): Call<ResponseBody>;
}
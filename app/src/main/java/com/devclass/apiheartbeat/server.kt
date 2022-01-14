package com.devclass.apiheartbeat

import android.content.Context
import android.net.Uri
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.Exception

import android.content.ContentValues
import android.database.Cursor
import android.opengl.Visibility
import android.view.View
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
//import android.R
import com.devclass.apiheartbeat.R

import android.widget.TextView

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.devclass.apiheartbeat.MainActivity.Companion.displayServers
import com.devclass.apiheartbeat.MainActivity.Companion.main_context


class Server(name:String,schema : String, url : String,port:String = "",endpoint:String = "",method:String = "GET",_id:Int=-1) {

    companion object{
        var servers = arrayOf<Server>();

        fun reload(){
            servers = retrieveServers();
        }
        fun insert(server:Server){

            val values = ContentValues()

            values.put(SQLite.NAME, server.name);
            values.put(SQLite.URL, server.url);
            values.put(SQLite.SCEHMA, server.schema);
            values.put(SQLite.PORT, server.port);
            values.put(SQLite.ENDPOINT, server.endpoint);
            values.put(SQLite.METHOD, server.method);

            val uri = MainActivity.Resolver.insert(
                SQLite.CONTENT_URL, values
            )
        }

        fun update(id :Int = 0,vals :ContentValues) : Int{
            var URL = "content://com.devclass.apiheartbeat.SQLite/${if (id == 0) {"servers"} else {"server_id/${id}"}}";
            val servers = Uri.parse(URL)
            var SQLiteResolver = MainActivity.Resolver.update(servers, vals, null,null);

            return SQLiteResolver;
        }
            fun delete(id :Int = 0) {
                var URL = "content://com.devclass.apiheartbeat.SQLite/${if (id == 0) {"servers"} else {"server_id/${id}"}}";
                val servers = Uri.parse(URL)
                var SQLiteResolver = MainActivity.Resolver.delete(servers, null, null);
            }


            fun getServersCursor(id: Int = 0): Cursor? {
                var URL = "content://com.devclass.apiheartbeat.SQLite/${if(id==0){"servers"}else{"server_id/${id}"}}";
                val servers = Uri.parse(URL)
                var SQLiteResolver = MainActivity.Resolver.query(servers, null,null , null ,null)
                return SQLiteResolver;
            }
            fun retrieveServers(id:Int = 0): Array<Server>{

                var SQLiteResolver = getServersCursor(id)

                var servers_list = arrayOf<Server>();

                if(SQLiteResolver!=null){
                    if(SQLiteResolver?.moveToFirst()){
                        do{
                            servers_list += arrayOf<Server>(Server(
                                _id = SQLiteResolver.getString(SQLiteResolver.getColumnIndex(SQLite._ID) as Int).toInt(),
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
            if(Server.servers.size==0){
                Server.servers = Server.retrieveServers();
            }

            for (server in servers){
                try {
                     pingOne(server);

                }
                catch(e: Exception){
                    println(server.route() + " Was not sent ")
                }
            }

        }
        fun pingOne(server:Server,toast :Boolean = false) {

            try{
                val retro: Retrofit = Retrofit.Builder().baseUrl(server.BaseUrl()).build();
                val api: Api = retro.create();
                 when(server.method){
                    "GET" -> {
                        api.get(server.endpoint).enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            server.status = "REST in peace"
                            println(server.route() + " Was not sent ")
                            t.printStackTrace()
                            if(toast){
                                Toast.makeText(main_context,"REST in peace!",Toast.LENGTH_LONG).show();
                            }
                            displayServers()
                        }
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            println(response.code().toString() + " - " + server.route() )
                            server.status =  response.code().toString();
                            if(toast){
                                Toast.makeText(main_context,response.code().toString(),Toast.LENGTH_LONG).show();
                            }
                            displayServers()
                        }
                    });
                    }
                    "POST" -> api.post(server.endpoint).enqueue(object :
                        Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            server.status = "REST in peace"
                            println(server.route() + " Was not sent ")
                            t.printStackTrace()
                            if(toast){
                                Toast.makeText(main_context,"REST in peace",Toast.LENGTH_LONG).show();
                            }
                            displayServers()
                        }
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            println(response.code().toString() + " - " + server.route() )
                            server.status =  response.code().toString();
                            if(toast){
                                Toast.makeText(main_context,response.code().toString(),Toast.LENGTH_LONG).show();
                            }
                            displayServers()
                        }
                    });
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
    public val _id = _id;
    public val name = name;
    public val schema = schema;
    public val url = url
    public val port = port;
    public val endpoint = endpoint;
    public val method = method.uppercase();
    public var status = "";


    public fun BaseUrl():String{
        return "${schema}://${url}${if(port!=""){":${port}"}else{""}}"
    }

    public fun route():String{
        return "[ ${method} ] - ${BaseUrl()}${endpoint}"
    }



}

class ServerAdapter(context: Context?, servers: Array<Server>)  : ArrayAdapter<Server>(main_context, 0, servers) {



   override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val server: Server = Server.servers[position];
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_with_status, parent, false)
        }


        val schema: TextView = convertView!!.findViewById(R.id.schema)
        val url: TextView = convertView!!.findViewById(R.id.url)
        val server_name: TextView = convertView!!.findViewById(R.id.server)
        val endpoint: TextView = convertView!!.findViewById(R.id.endpoint)
       val endpoint_title: TextView = convertView!!.findViewById(R.id.endpoint_title)
       val port: TextView = convertView!!.findViewById(R.id.port)
       val port_title: TextView = convertView!!.findViewById(R.id.port_title)
        val status : TextView = convertView!!.findViewById(R.id.status);
        val method : TextView = convertView!!.findViewById(R.id.method);


       if (server.endpoint == ""){
           endpoint_title.setVisibility(View.GONE);
           endpoint.setVisibility(View.GONE);
       }
       else{
           endpoint.setText(server.endpoint);
       }


       if (server.port == ""){
           port_title.setVisibility(View.GONE);
           port.setVisibility(View.GONE);
       }
       else{
           port.setText(server.port);
       }

        schema.setText(server.schema);
        url.setText(server.url);
        server_name.setText(server.name);

        status.setText(server.status);
        method.setText(server.method);

        return convertView
    }
}

interface Api{
    @GET("/{endpoint}")
    fun get(@Path("endpoint") endpoint : String ): Call<ResponseBody>;

    @POST("/{endpoint}")
    fun post(@Path("endpoint") endpoint : String ): Call<ResponseBody>;
}
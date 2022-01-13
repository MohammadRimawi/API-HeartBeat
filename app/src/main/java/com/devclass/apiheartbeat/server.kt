package com.devclass.apiheartbeat

import android.content.Context
import android.net.Uri

class Server(context: Context,name:String,schema : String, url : String,port:String = "",endpoint:String = "",method:String = "GET") {



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
package com.devclass.apiheartbeat

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.HttpURLConnection
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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
        displayServers()
        var serversLV: ListView = findViewById(R.id.ServersLV)
        val adapter = serversLV.adapter
        serversLV.setOnItemClickListener { parent, view, position, id ->
//            val intent = Intent(this,)
        }
        Server.pingAll()
    }

    private fun displayServers() {
        var serverCursor = Server.getServersCursor()
        var from = listOf<String>("url","endpoint").toTypedArray()
        var to = intArrayOf(android.R.id.text1,android.R.id.text2)
        var adapter = SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,
                                        serverCursor, from, to, 0 )
        var serversLV: ListView = findViewById(R.id.ServersLV)
        serversLV.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sync_all){
            startService(Intent(this,PingService::class.java));
        }
        return super.onOptionsItemSelected(item)
    }

}
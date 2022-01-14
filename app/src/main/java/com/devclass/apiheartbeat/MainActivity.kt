package com.devclass.apiheartbeat

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var Resolver :ContentResolver
        lateinit var main_context : Context;
        lateinit var serversLV : ListView;
        lateinit var adapter : ServerAdapter;

        fun displayServers() {
            adapter = ServerAdapter(main_context, Server.servers);
            serversLV.setAdapter(adapter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Resolver = contentResolver;
        main_context = baseContext
        serversLV = findViewById(R.id.ServersLV)

        Server.reload();
        displayServers()

        serversLV.setOnItemClickListener { parent, view, position, id ->
            println("${position}   ${id}")
            var intent = Intent(this, ServerUpdateActivity::class.java)
            intent.putExtra("position", position);
            intent.putExtra("id", Server.servers[position]._id);
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sync_all){
            startService(Intent(this,PingService::class.java));
          displayServers()
        }
        if(item.itemId == R.id.add_server){
            var dialog = AddServerFragment()
            dialog.show(supportFragmentManager, "addDialog")

        }
        return super.onOptionsItemSelected(item)
    }

}
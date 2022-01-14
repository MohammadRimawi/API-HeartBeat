package com.devclass.apiheartbeat

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ServerUpdateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.server_update_activity)


         val id = intent.getIntExtra("id", 0);

        val server = Server.retrieveServers(id)[0];
        println(server.route())

        val activity_header : TextView = findViewById(R.id.activity_header);

        val name_text_box : EditText = findViewById(R.id.server_name_text_input);
        val url_text_box : EditText = findViewById(R.id.server_url_text_input);
        val schema_text_box : EditText = findViewById(R.id.server_schema_text_input);
        val method_text_box : EditText = findViewById(R.id.server_method_text_input);
        val port_text_box : EditText = findViewById(R.id.server_port_text_input);
        val endpoint_text_box : EditText = findViewById(R.id.server_endpoint_text_input);

        val upload_button : Button = findViewById(R.id.update_button);

        activity_header.text = "Server of ID: ${id}"

        name_text_box.setText(server.name);
        url_text_box.setText(server.url);
        schema_text_box.setText(server.schema);
        method_text_box.setText(server.method);
        port_text_box.setText(server.port);
        endpoint_text_box.setText(server.endpoint);

        upload_button.setOnClickListener{
            var values = ContentValues();
            values.put(SQLite.NAME,name_text_box.text.toString());
            values.put(SQLite.URL,url_text_box.text.toString());
            values.put(SQLite.SCEHMA,schema_text_box.text.toString());
            values.put(SQLite.METHOD,method_text_box.text.toString());
            values.put(SQLite.PORT,port_text_box.text.toString());
            values.put(SQLite.ENDPOINT,endpoint_text_box.text.toString());

            if(Server.update(id,values)> 0){
                Toast.makeText(applicationContext,"Server was updated!",Toast.LENGTH_LONG).show();
                finish();

            }
            else{
                Toast.makeText(applicationContext,"Server was not updated!",Toast.LENGTH_LONG).show();
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.server_update_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = intent.getIntExtra("id",0);

        when(item.itemId){
            R.id.ping_server -> {

            }
            R.id.delete_server ->{
                val builder = AlertDialog.Builder(this@ServerUpdateActivity)
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, dialog_id ->
                        Server.delete(id)
                        Toast.makeText(applicationContext,"Server of ID: ${id} was Deleted!",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    .setNegativeButton("No") { dialog, dialog_id ->
                        dialog.dismiss()
                        Toast.makeText(applicationContext,"Server of ID: ${id} was not Deleted!",Toast.LENGTH_LONG).show();

                    }
                val alert = builder.create()
                alert.show()
//
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
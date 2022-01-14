package com.devclass.apiheartbeat

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AddServerFragment : DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_add_server, container, false)
        var add_btn = rootView.findViewById<Button>(R.id.add_button)
        add_btn.setOnClickListener{
            val name_value : String = rootView.findViewById<EditText>(R.id.server_name_text_input).text.toString()
            val url_value  : String = rootView.findViewById<EditText>(R.id.server_url_text_input).text.toString()
            val schema_value : String = rootView.findViewById<EditText>(R.id.server_schema_text_input).text.toString()
            val method_value : String = rootView.findViewById<EditText>(R.id.server_method_text_input).text.toString()
            val port_value : String = rootView.findViewById<EditText>(R.id.server_port_text_input).text.toString()
            val endpoint_value : String = rootView.findViewById<EditText>(R.id.server_endpoint_text_input).text.toString()
            val server : Server = Server(name_value,schema_value,url_value,port_value,endpoint_value,method_value)
            Server.insert(server)
            Toast.makeText(activity,"Added $name_value server successfully",Toast.LENGTH_SHORT).show()

            dismiss()
        }
        return rootView
    }

    override fun onDismiss(dialog: DialogInterface) {
        (activity as MainActivity).displayServers()
        super.onDismiss(dialog)
    }
}
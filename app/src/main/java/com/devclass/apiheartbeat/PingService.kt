package com.devclass.apiheartbeat

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.widget.Toast

class PingService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this,"Starting Ping Service",Toast.LENGTH_SHORT).show()
        Server.pingAll()
        stopSelf();
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}
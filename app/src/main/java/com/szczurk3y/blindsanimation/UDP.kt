package com.szczurk3y.blindsanimation

import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Toast
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDP(val activity: Activity): Runnable {

    private val PORT = 10107
    private val HOST = "192.168.1.255"

    override fun run() {
        try {
            val messageBuffer = ByteArray(1024)
            val datagramSocket =
                DatagramSocket(PORT, InetAddress.getByName(HOST))
            val datagramPacket = DatagramPacket(messageBuffer, messageBuffer.size)
            while (true) {
                datagramSocket.receive(datagramPacket)
                val text = String(messageBuffer, 0, datagramPacket.length)
                if (text == "rola;") {
                    activity.runOnUiThread {
                        MainActivity.blindsCounter += 1
                        MainActivity.progressBar?.visibility = View.GONE
                        MainActivity.recyclerView?.visibility = View.VISIBLE
                        MainActivity.blindsList.add(Blind())
                        MainActivity.recyclerView?.adapter = BlindsAdapter(MainActivity.blindsList)
                    }
                    Log.i("UDP", text + " " + MainActivity.blindsCounter)
                }
            }
        } catch (e: Exception) {
            Log.i("UDP_ERROR", e.printStackTrace().toString())
        }
    }
}
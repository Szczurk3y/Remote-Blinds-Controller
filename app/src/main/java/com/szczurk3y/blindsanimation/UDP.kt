package com.szczurk3y.blindsanimation

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Toast
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDP(val activity: Activity): Runnable {
    companion object {
        const val PORT = 10107
        val HOST = "192.168.1.255"
    }

    override fun run() {
        val wifiManager: WifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val lock: WifiManager.MulticastLock = wifiManager.createMulticastLock("Log_Tag")
        lock.acquire()

        try {
            val messageBuffer = ByteArray(1024)
            val datagramSocket = DatagramSocket(PORT, InetAddress.getByName(HOST))
            val datagramPacket = DatagramPacket(messageBuffer, messageBuffer.size)
            while (true) {
                Log.i("UDP_LOG", "elo przed")
                datagramSocket.receive(datagramPacket)
                Log.i("UDP_LOG", "elo po")
                val text: String? = String(messageBuffer, 0, datagramPacket.length)
                val ip: String? = datagramPacket.address.toString().removeRange(0..0)
                val blind = Blind(
                    id = Handler.blindsList.size,
                    name = ip,
                    itemProgression = Handler.blindsList.size,
                    ip = ip.toString()
                )
                activity.runOnUiThread {
                    Handler.checkAndAdd(blind, text, false)
                }
            }
        } catch (e: Exception) {
            Log.i("UDP_ERROR", e.printStackTrace().toString())
        }
    }
}
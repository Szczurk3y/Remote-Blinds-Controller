package com.szczurk3y.blindsanimation

import android.app.Activity
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.IOException
import java.lang.Exception
import java.net.*

class UDP(val activity: Activity): Runnable {
    companion object {
        const val PORT = 10107
        var HOST = ""
    }

    override fun run() {
        val wifiManager: WifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val lock: WifiManager.MulticastLock = wifiManager.createMulticastLock("Log_Tag")
        lock.acquire()

        HOST = getBroadcast()
        Log.i("Broadcast:", HOST)
        if (HOST != "Disconnected") {
            try {
                while (true) {
                    val messageBuffer = ByteArray(1024)
                    val datagramSocket = DatagramSocket(PORT, InetAddress.getByName(HOST))
                    val datagramPacket = DatagramPacket(messageBuffer, messageBuffer.size)
                    while (true) {
                        datagramSocket.receive(datagramPacket)
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
                }
            } catch (e: Exception) {
                Log.i("UDP_ERROR", e.printStackTrace().toString())
            }
        }
    }

    @Throws(Exception::class)
    fun getBroadcast(): String {
        val wm = activity.baseContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        return if (ip != "0.0.0.0") {
            var broadcast = ""
            var counterOfDots = 0
            for (letter in ip) {
                if (counterOfDots < 3) broadcast += letter
                if (letter == '.') counterOfDots += 1
            }
            broadcast+"255" // x.x.x.255 is a broadcast address
        } else {
            "Disconnected"
        }
    }

}
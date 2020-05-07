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
                val text: String? = String(messageBuffer, 0, datagramPacket.length)
                val ip: InetAddress? = datagramPacket.address
                val blind = Blind(udpPacketText = text, ip = ip)
                activity.runOnUiThread {
                    BlindsHandler.checkAndAdd(blind)
                }
            }
        } catch (e: Exception) {
            Log.i("UDP_ERROR", e.printStackTrace().toString())
        }
    }
}
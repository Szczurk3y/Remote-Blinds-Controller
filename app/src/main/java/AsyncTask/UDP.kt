package AsyncTask

import android.app.Activity
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.text.format.Formatter
import android.util.Log
import com.szczurk3y.blindsanimation.Blind
import com.szczurk3y.blindsanimation.Handler
import java.lang.Exception
import java.net.*

class UDP(val activity: Activity): AsyncTask<Unit, Unit, Unit>() {
    companion object {
        const val PORT = 10107
        private val messageBuffer = ByteArray(1024)
        private var datagramPacket: DatagramPacket = DatagramPacket(messageBuffer, messageBuffer.size)
        private var datagramSocket: DatagramSocket = DatagramSocket(PORT)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        val wifiManager: WifiManager = activity.getSystemService(WIFI_SERVICE) as WifiManager
        val lock: WifiManager.MulticastLock = wifiManager.createMulticastLock("Log_Tag")
        lock.acquire()
        Log.i("isDeviceConnected:", isDeviceConnected().toString())
    }

    override fun doInBackground(vararg params: Unit?) {
        if (isDeviceConnected()) {
            try {
                while (true) {
                    datagramSocket.receive(
                        datagramPacket
                    )
                    val text: String? = String(messageBuffer, 0, datagramPacket.length)
                    val ip: String? = datagramPacket.address.toString().removeRange(0..0)
                    val blind = Blind(
                        id = Handler.blindsList.size + 1,
                        name = ip,
                        itemProgression = Handler.blindsList.size,
                        ip = ip.toString()
                    )
                    activity.runOnUiThread {
                      Handler.checkAndAdd(
                          blind,
                          text,
                          false
                      )
                    }
                }
            } catch (e: Exception) {
                Log.i("UDP_ERROR", e.printStackTrace().toString())
            }
        }
    }

    @Throws(Exception::class)
    fun isDeviceConnected(): Boolean {
        val wm = activity.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        return ip != "0.0.0.0"
    }
}
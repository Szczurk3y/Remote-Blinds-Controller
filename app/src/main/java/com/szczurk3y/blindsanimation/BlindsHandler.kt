package com.szczurk3y.blindsanimation

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import java.net.InetAddress
import java.util.*
import kotlin.properties.Delegates
import kotlin.properties.ObservableProperty

data class Blind(
    var blind: ImageView? = null,
    var blindCoverPercentage: Int? =  null,
    var udpPacketText: String? = null,
    var ip: InetAddress? = null,
    var blindRelativeLayout: RelativeLayout? = null
)

object BlindsHandler {
    var activeBlind = 0
    val blindsList = mutableListOf<Blind>()

    fun checkAndAdd(blind: Blind): Boolean {
        Log.i("Check", "Check!" + blind)
        var isMatching = true
        if (blind.udpPacketText != "rola;") isMatching = false
        blindsList.forEach {
            if (it.ip == blind.ip) isMatching = false
        }
        if (isMatching) {
            blindsList.add(blind)
            refresh()
        }
        return isMatching
    }

    fun remove(blind: Blind) {
        blindsList.remove(blind)
        refresh()
    }

    private fun refresh() {
        Log.i("Refresh", "Refresh!" + blindsList.size)
        if (blindsList.size > 0) {
            MainActivity.progressBar?.visibility = View.GONE
            MainActivity.recyclerView?.visibility = View.VISIBLE
//            MainActivity.recyclerView?.adapter = BlindsAdapter(blindsList)
            MainActivity.recyclerView?.adapter?.notifyItemChanged(blindsList.size, blindsList)

        } else if (blindsList.size < 1) {
            MainActivity.progressBar?.visibility = View.VISIBLE
            MainActivity.recyclerView?.visibility = View.GONE
        }
    }

}
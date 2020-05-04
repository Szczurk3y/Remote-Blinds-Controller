package com.szczurk3y.blindsanimation

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.net.InetAddress

data class Blind(
    var blind: ImageView? = null,
    var blindCoverPercentage: Int = 0,
    var text: String? = null,
    var ip: InetAddress? = null
)

@SuppressLint("StaticFieldLeak")
object BlindsHandler {
    var activeBlind = 0
    val blindsList = mutableListOf<Blind>()

    fun checkAndAdd(blind: Blind): Boolean {
        Log.i("Check", "Check!" + blind)

        var isMatching = true
        if (blind.text != "rola;") isMatching = false
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
            MainActivity.recyclerView?.adapter = BlindsAdapter(blindsList)
        } else if (blindsList.size < 1) {
            MainActivity.progressBar?.visibility = View.VISIBLE
            MainActivity.recyclerView?.visibility = View.GONE
        }
    }

}
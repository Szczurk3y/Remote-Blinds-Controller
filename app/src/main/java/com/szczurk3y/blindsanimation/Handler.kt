package com.szczurk3y.blindsanimation

import Activities.MainActivity
import Activities.OptionsActivity
import Adapters.BlindsAdapter
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import java.util.*

data class Blind(
    var id: Int?,
    var name: String?,
    var itemProgression: Int?,
    var ip: String?,
    var blindCoverPercentage: Int = 100,
    var blind: ImageView? = null,
    var blindRelativeLayout: RelativeLayout? = null
)

object Handler {
    var activeBlind: Int = 0
    val blindsList = mutableListOf<Blind>()
    var databaseHelper: DatabaseHelper? = null

    fun checkAndAdd(blind: Blind, udpPacketText: String?, isAlreadyStored: Boolean): Boolean {
        var isMatching = true
        if (udpPacketText != "rola;") isMatching = false
        blindsList.forEach {
            if (it.ip == blind.ip) isMatching = false
        }
        if (isMatching) {
            blindsList.add(blind)
            if (!isAlreadyStored) databaseHelper?.insertBlind(blind)
            MainActivity.recyclerView?.adapter?.notifyItemChanged(blindsList.size, blindsList)
            OptionsActivity.optionsRecyclerView?.adapter?.notifyItemInserted(blindsList.size)
            refresh()
        }
        return isMatching
    }

    fun onOptionMoved(positionDragged: Int, positionTarget: Int) {
        Collections.swap(blindsList, positionDragged, positionTarget)
        OptionsActivity.optionsRecyclerView?.adapter?.notifyItemMoved(positionDragged, positionTarget)
        MainActivity.recyclerView?.adapter?.notifyItemMoved(positionDragged, positionTarget)
    }

    fun onOptionSwiped(position: Int, id: Int) {
        blindsList.removeAt(position)
        databaseHelper?.deleteBlind(id)
        MainActivity.recyclerView?.adapter?.notifyItemRemoved(position)
        OptionsActivity.optionsRecyclerView?.adapter?.notifyItemRemoved(position)
        refresh()
    }

    fun renameBlind(position: Int, newName: String) {
        blindsList[position].name = newName
        databaseHelper?.renameBlind(blindsList[position], newName)
        MainActivity.recyclerView?.adapter?.notifyItemChanged(position)
    }

    fun replaceBlinds(positionDragged: Int, positionTarget: Int) {
        databaseHelper?.replaceBlinds(blindsList[positionDragged], blindsList[positionTarget])
    }

    private fun refresh() {
        Log.i("Refresh", "Refresh!" + blindsList.size)
        if (blindsList.size > 0) {
            MainActivity.progressBar?.visibility = View.GONE
            MainActivity.recyclerView?.visibility = View.VISIBLE

        } else if (blindsList.size < 1) {
            MainActivity.progressBar?.visibility = View.VISIBLE
            MainActivity.recyclerView?.visibility = View.GONE
        }
    }

}
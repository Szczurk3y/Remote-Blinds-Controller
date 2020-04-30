package com.szczurk3y.blindsanimation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_blind.view.*

class BlindsAdapter(var blindsList: MutableList<Blind>) : RecyclerView.Adapter<BlindsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blind, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return blindsList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        blindsList[position].blind = holder.blind
        holder.tactileLayout.setOnTouchListener {view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (motionEvent.y < holder.blindRelativeLayout!!.height - 10 && motionEvent.y > 0)
                        holder.blind!!.y = motionEvent.y - holder.blind.height
                }
                MotionEvent.ACTION_DOWN -> {
                    MainActivity.recyclerView?.suppressLayout(true)
                }
                MotionEvent.ACTION_UP -> {
                    MainActivity.recyclerView?.suppressLayout(false)
                }
            }
            true
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        MainActivity.activeBlind = holder.layoutPosition
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val blind = itemView.blind
        val tactileLayout = itemView.tactileLayout
        val blindRelativeLayout = itemView.blindRelativeLayout
    }
}
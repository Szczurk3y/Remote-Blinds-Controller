package com.szczurk3y.blindsanimation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_option.view.*
import java.util.*

class OptionsAdapter(var optionsList: MutableList<Blind>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.blindName.text = optionsList[position].name
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blindName = view.option_blind_name
    }
}
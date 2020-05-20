package com.szczurk3y.blindsanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_options.*
import java.util.*

class OptionsActivity : AppCompatActivity() {

    private var backButton: Button? = null
    private var optionsRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        initItems()
        initItemsListener()
    }

    private fun initItems(): Unit {
        backButton = findViewById(R.id.backButton)
        optionsRecyclerView = findViewById(R.id.optionsRecyclerView)
        optionsRecyclerView?.adapter = OptionsAdapter(BlindsHandler.blindsList)
        val touchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                dragged: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val positionDragged = dragged.adapterPosition
                val positionTarget = target.adapterPosition

                Collections.swap(BlindsHandler.blindsList, positionDragged, positionTarget)
                optionsRecyclerView?.adapter?.notifyItemMoved(positionDragged, positionTarget)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                BlindsHandler.blindsList.removeAt(viewHolder.adapterPosition)
            }
        })
        touchHelper.attachToRecyclerView(optionsRecyclerView)
    }

    private fun initItemsListener(): Unit {
        backButton?.let {
            backButton!!.setOnClickListener {
                finish()
            }
        }
    }
}

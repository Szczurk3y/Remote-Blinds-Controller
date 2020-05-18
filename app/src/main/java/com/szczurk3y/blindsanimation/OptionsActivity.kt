package com.szczurk3y.blindsanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_options.*

class OptionsActivity : AppCompatActivity() {

    private var backButton: Button? = null
    private var optionsRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        initItems()
        initItemsListener()
    }

    @Synchronized private fun initItems(): Unit {
        backButton = findViewById(R.id.backButton)
        optionsRecyclerView = findViewById(R.id.optionsRecyclerView)
        optionsRecyclerView?.adapter = OptionsAdapter(BlindsHandler.blindsList)
    }

    private fun initItemsListener(): Unit {
        backButton?.let {
            backButton!!.setOnClickListener {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        optionsRecyclerView?.let {
            optionsRecyclerView!!.adapter = OptionsAdapter(BlindsHandler.blindsList)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

package com.szczurk3y.blindsanimation

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.floor
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var arrowDropDown: CircleImageView? = null // Arrow view to push blind in down direction
    private var arrowDropUp: CircleImageView? = null // Arrow view to push blind in up direction
    private var setButton: Button? = null // Button view to send GET request to all detected blinds
    private val actionDownDownFlag = AtomicBoolean(true) // Variable to start/stop pushing blind in down direction, used in while() loop in SlidingDownThread
    private val actionDownUpFlag = AtomicBoolean(true) // variable to start/stop pushing blind in up direction, used in while() loop in SlidingUpThread

    companion object {
        @SuppressLint("StaticFieldLeak") // Who cares ( ͡°﻿ ͜ʖ ͡°)
        var progressBar: ProgressBar? = null // The progressBar (loading animation) used when program starts and it's detecting UDP packets
        var recyclerView: RecyclerView? = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initItems()
        Thread(UDP(this)).start()

        arrowDropDown?.let {
            arrowDropDown!!.setOnTouchListener { view, motionEvent ->
                val slidingDownThread = Thread(object: Runnable {
                    override fun run() {
                        actionDownDownFlag.set(true)
                        while (actionDownDownFlag.get()) {
                            if (BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y + BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.height < BlindsHandler.blindsList[BlindsHandler.activeBlind].blindRelativeLayout!!.height) {
                                BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y = BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y + 1
                                BlindsHandler.blindsList[BlindsHandler.activeBlind].blindCoverPercentage = floor(((BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y + BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.height) / BlindsHandler.blindsList[BlindsHandler.activeBlind].blindRelativeLayout!!.height * 100).toDouble()).toInt()
                                Thread.sleep(3)
                            }
                        }
                    }
                })
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        slidingDownThread.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        actionDownDownFlag.set(false)
                    }
                }
                true
            }
        }

        arrowDropUp?.let {
            arrowDropUp!!.setOnTouchListener { view, motionEvent ->
                val slidingUpthread = Thread(object: Runnable {
                    override fun run() {
                        actionDownUpFlag.set(true)
                        while (actionDownUpFlag.get()) {
                            if (BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y + BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.height > 1) {
                                BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y = BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y - 1
                                BlindsHandler.blindsList[BlindsHandler.activeBlind].blindCoverPercentage = floor(((BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.y + BlindsHandler.blindsList[BlindsHandler.activeBlind].blind!!.height) / BlindsHandler.blindsList[BlindsHandler.activeBlind].blindRelativeLayout!!.height * 100).toDouble()).toInt()
                                Thread.sleep(3)
                            }
                        }
                    }
                })
                when(motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        slidingUpthread.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        actionDownUpFlag.set(false)
                    }
                }
                true
            }
        }

        setButton?.let {
            setButton!!.setOnClickListener {
                SendShouldBe(this).execute()
            }
        }

    }

    private fun initItems(): Unit {
        progressBar = findViewById(R.id.progressBar)
        arrowDropDown = findViewById(R.id.arrowDropDown)
        arrowDropUp = findViewById(R.id.arrowDropUp)
        setButton = findViewById(R.id.setButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.adapter = BlindsAdapter(BlindsHandler.blindsList)
    }
}

class SendShouldBe(val context: Context): AsyncTask<String, Unit, Unit>() {
    private var progressDialog = ProgressDialog(context)

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.setTitle("Setting blinds...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun doInBackground(vararg p0: String?) {
        Thread.sleep(1000) // Loading... (｡◕‿‿◕｡)
        for (blind in BlindsHandler.blindsList) {
            val call = BlindsServiceBuilder(blind.ip!!).getService().shouldBe(blind.blindCoverPercentage.toString())
            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Toast.makeText(context, response.body()?.string(), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}



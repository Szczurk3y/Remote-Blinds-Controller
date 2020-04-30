package com.szczurk3y.blindsanimation

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.Delegates

data class Blind(
    var blind: ImageView? = null,
    var blindCoverPercentage: Int = 0,
    var ip: Int? = null
)

class MainActivity : AppCompatActivity() {
    private var pointingHand: ImageView? = null
    private var arrowDropDown: CircleImageView? = null
    private var arrowDropUp: CircleImageView? = null
    private var setButton: Button? = null
    private var blindRelativeLayout: RelativeLayout? = null
    private val actionDownDownFlag = AtomicBoolean(true)
    private val actionDownUpFlag = AtomicBoolean(true)

    companion object {
        @SuppressLint("StaticFieldLeak")
        var progressBar: ProgressBar? = null
        var recyclerView: RecyclerView? = null
        var activeBlind = 0
        var blindsCounter by Delegates.observable(0) {property, oldValue, newValue ->
            if (newValue > 0) {
                progressBar?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE
                if (newValue > oldValue) {
                    blindsList.add(Blind())
                    recyclerView?.adapter = BlindsAdapter(blindsList)
                }
            } else if (newValue < 1) {
                progressBar?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
            }

        }
        val blindsList = mutableListOf<Blind>()
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
                            if (blindsList[activeBlind].blind!!.y + blindsList[activeBlind].blind!!.height < blindsList[activeBlind].blind!!.height - 15)
                                blindsList[activeBlind].blind!!.y = blindsList[activeBlind].blind!!.y + 1
                            Thread.sleep(3)
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
                            if (blindsList[activeBlind].blind!!.y + blindsList[activeBlind].blind!!.height > 0) {
                                blindsList[activeBlind].blind!!.y = blindsList[activeBlind].blind!!.y - 1
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


    }

    private fun initItems(): Unit {
        progressBar = findViewById(R.id.progressBar)
        pointingHand = findViewById(R.id.pointing_hand)
        arrowDropDown = findViewById(R.id.arrowDropDown)
        arrowDropUp = findViewById(R.id.arrowDropUp)
        setButton = findViewById(R.id.setButton)
        blindRelativeLayout = findViewById(R.id.blindRelativeLayout)
        recyclerView = findViewById(R.id.recyclerView)
    }
}

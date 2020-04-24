package com.szczurk3y.blindsanimation

import android.annotation.SuppressLint
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var pointingHand: ImageView? = null
    private var blind: ImageView? = null
    private var blindRelativeLayout: RelativeLayout? = null
    private var arrowDropDown: CircleImageView? = null
    private var arrowDropUp: CircleImageView? = null
    private var setButton: Button? = null
    private val actionDownDownFlag = AtomicBoolean(true)
    private val actionDownUpFlag = AtomicBoolean(true)
    private var topRotarySomething: ImageView? = null
    private var tactileLayout: LinearLayout? = null

    private var testTextView: TextView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initItems()

        blind!!.post {
            val animation = TranslateAnimation(
                blind!!.x,
                blind!!.x,
                blind!!.y,
                blind!!.y + blind!!.height/1.3f
            )
            animation.duration = 1500
            animation.fillAfter = false
            animation.repeatCount = 2
            animation.setAnimationListener(PointingHandAnimationListener(pointingHand!!))
            pointingHand!!.startAnimation(animation)
            blind!!.y = -blind!!.height.toFloat()
        }

        arrowDropDown?.let {
            arrowDropDown!!.setOnTouchListener { view, motionEvent ->
                val slidingDownThread = Thread(object: Runnable {
                    override fun run() {
                        actionDownDownFlag.set(true)
                        while (actionDownDownFlag.get()) {
                            if (blind!!.y + blind!!.height < blindRelativeLayout!!.height - 15)
                            blind!!.y = blind!!.y + 1
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
                            if (blind!!.y + blind!!.height > 0) {
                                blind!!.y = blind!!.y - 1
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

        tactileLayout?.let {
            tactileLayout!!.setOnTouchListener {view, motionEvent ->
                when(motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> {
                        testTextView!!.text = motionEvent.y.toString()
                        if (motionEvent.y < blindRelativeLayout!!.height - 10 && motionEvent.y > 0)
                            blind!!.y = motionEvent.y - blind!!.height
                    }
                }
                true
            }
        }
    }

    private fun initItems(): Unit {
        pointingHand = findViewById(R.id.pointing_hand)
        blind = findViewById(R.id.blind)
        arrowDropDown = findViewById(R.id.arrowDropDown)
        arrowDropUp = findViewById(R.id.arrowDropUp)
        setButton = findViewById(R.id.setButton)
        topRotarySomething = findViewById(R.id.topRotarySomething)
        tactileLayout = findViewById(R.id.tactileLayout)
        blindRelativeLayout = findViewById(R.id.blindRelativeLayout)

        testTextView = findViewById(R.id.textView)
    }
}

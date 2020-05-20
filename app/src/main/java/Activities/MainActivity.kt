package Activities

import Adapters.BlindsAdapter
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.blindsanimation.*
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    private var arrowDropDown: CircleImageView? = null // Arrow view to push blind in down direction
    private var arrowDropUp: CircleImageView? = null // Arrow view to push blind in up direction
    private var setButton: Button? = null // Button view to send GET request to all detected blinds
    private var optionsButton: Button? = null
    private val actionDownDownFlag = AtomicBoolean(true) // Variable to start/stop pushing blind in down direction, used in while() loop in SlidingDownThread
    private val actionDownUpFlag = AtomicBoolean(true) // variable to start/stop pushing blind in up direction, used in while() loop in SlidingUpThread

    companion object {
        @SuppressLint("StaticFieldLeak") // it's better to not place views like that (in object), but in our case it won't leak any field, so I will allow myself to do it.
        var progressBar: ProgressBar? = null // The progressBar (loading animation) used when program starts and it's detecting UDP packets
        var recyclerView: RecyclerView? = null // Adapter goes here. Used in BlindsAdapter in order to suppress layout when user touches blind (so recycler view won't scroll horizontally when blind is being moved up/down (vertically))
        var databaseHelper: DatabaseHelper? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Handler.blindsList.add(
//            Blind(1, "1", 1, "1")
//        )
//        Handler.blindsList.add(
//            Blind(2, "2", 1, "2")
//        )
//        Handler.blindsList.add(
//            Blind(3, "3", 1, "3")
//        )
//        Handler.blindsList.add(
//            Blind(4, "4", 1, "4")
//        )
        initViews()
        initViewsListeners()
        initDatabase()
        Thread(UDP(this)).start() // Start listening for UDP packets
    }

    private fun initDatabase() {
        databaseHelper =
            DatabaseHelper(this)
        val cursor = databaseHelper!!.data
        if (cursor.count > 0) {
            while(cursor.moveToNext()) {
                val blind = Blind(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    itemProgression = cursor.getInt(2),
                    ip = cursor.getString(3)
                )
                runOnUiThread {
                    Handler.checkAndAdd(
                        blind,
                        "rola;",
                        true
                    )
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility") // Need to set on touch listener
    private fun initViewsListeners(): Unit {
        arrowDropDown?.let {
            arrowDropDown!!.setOnTouchListener { view, motionEvent ->
                val slidingDownThread = Thread(object: Runnable {
                    override fun run() {
                        actionDownDownFlag.set(true)
                        if (Handler.blindsList.size > 0) {
                            while (actionDownDownFlag.get()) {
                                if (Handler.blindsList[Handler.activeBlind].blind!!.y + Handler.blindsList[Handler.activeBlind].blind!!.height < Handler.blindsList[Handler.activeBlind].blindRelativeLayout!!.height) {
                                    Handler.blindsList[Handler.activeBlind].blind!!.y =
                                        Handler.blindsList[Handler.activeBlind].blind!!.y + 1
                                    Handler.blindsList[Handler.activeBlind].blindCoverPercentage =
                                        floor(((Handler.blindsList[Handler.activeBlind].blind!!.y + Handler.blindsList[Handler.activeBlind].blind!!.height) / Handler.blindsList[Handler.activeBlind].blindRelativeLayout!!.height * 100).toDouble()).toInt()
                                    Thread.sleep(3)
                                }
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
                        if (Handler.blindsList.size > 0) {
                            while (actionDownUpFlag.get()) {
                                if (Handler.blindsList[Handler.activeBlind].blind!!.y + Handler.blindsList[Handler.activeBlind].blind!!.height > 1) {
                                    Handler.blindsList[Handler.activeBlind].blind!!.y = Handler.blindsList[Handler.activeBlind].blind!!.y - 1
                                    Handler.blindsList[Handler.activeBlind].blindCoverPercentage = floor(((Handler.blindsList[Handler.activeBlind].blind!!.y + Handler.blindsList[Handler.activeBlind].blind!!.height) / Handler.blindsList[Handler.activeBlind].blindRelativeLayout!!.height * 100).toDouble()).toInt()
                                    Thread.sleep(3)
                                }
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
                if (Handler.blindsList.size > 0) {
                    SendShouldBe(this).execute()
                }
            }
        }

        optionsButton?.let {
            optionsButton!!.setOnClickListener {
                val intent = Intent(this, OptionsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
        }
    }

    private fun initViews(): Unit {
        progressBar = findViewById(
            R.id.progressBar
        )
        arrowDropDown = findViewById(R.id.arrowDropDown)
        arrowDropUp = findViewById(R.id.arrowDropUp)
        setButton = findViewById(R.id.setButton)
        optionsButton = findViewById(R.id.optionsButton)
        recyclerView = findViewById(
            R.id.recyclerView
        )
        recyclerView?.adapter = BlindsAdapter(
            Handler.blindsList
        )
    }
}



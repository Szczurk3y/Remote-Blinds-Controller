package Activities

import Adapters.BlindsAdapter
import Adapters.OptionsAdapter
import AsyncTask.UDP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.blindsanimation.Handler
import com.szczurk3y.blindsanimation.R
import java.util.*

class OptionsActivity : AppCompatActivity() {

    companion object {
        var optionsRecyclerView: RecyclerView? = null
    }

    private var backButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        initItems()
        initItemsListener()
    }

    private fun initItems(): Unit {
        backButton = findViewById(R.id.backButton)
        optionsRecyclerView = findViewById(R.id.optionsRecyclerView)
        optionsRecyclerView?.adapter = OptionsAdapter(Handler.blindsList)
        val touchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(UP or DOWN, RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                dragged: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Handler.onOptionMoved(dragged.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Handler.onOptionSwiped(viewHolder.adapterPosition, Handler.blindsList[viewHolder.adapterPosition].id!!)
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
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

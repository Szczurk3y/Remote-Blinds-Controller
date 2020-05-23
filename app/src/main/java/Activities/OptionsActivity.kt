package Activities

import Adapters.OptionsAdapter
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
        optionsRecyclerView?.adapter =
            OptionsAdapter(Handler.blindsList)
        val touchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(UP or DOWN, RIGHT) {
            var dragFrom = -1
            var dragTo = -1

            override fun onMove(
                recyclerView: RecyclerView,
                dragged: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val positionDragged = dragged.adapterPosition
                val positionTarget = target.adapterPosition
                if (dragFrom == -1) {
                    dragFrom = positionDragged
                }
                dragTo = positionTarget
                Collections.swap(Handler.blindsList, positionDragged, positionTarget)
                optionsRecyclerView?.adapter?.notifyItemMoved(positionDragged, positionTarget)
                MainActivity.recyclerView?.adapter?.notifyItemMoved(positionDragged, positionTarget)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Handler.onSwiped(viewHolder.adapterPosition, Handler.blindsList[viewHolder.adapterPosition].id!!)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    Handler.replaceBlinds(dragFrom, dragTo)
                    Log.i("Drag From:", dragFrom.toString())
                    Log.i("Drag To:", dragTo.toString())
                }
                dragTo = -1
                dragFrom = -1
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

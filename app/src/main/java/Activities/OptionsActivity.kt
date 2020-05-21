package Activities

import Adapters.OptionsAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.blindsanimation.Handler
import com.szczurk3y.blindsanimation.R
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
        optionsRecyclerView?.adapter =
            OptionsAdapter(Handler.blindsList)
        val touchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                dragged: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val positionDragged = dragged.adapterPosition
                val positionTarget = target.adapterPosition

                Collections.swap(Handler.blindsList, positionDragged, positionTarget)
                optionsRecyclerView?.adapter?.notifyItemMoved(positionDragged, positionTarget)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Handler.remove(Handler.blindsList.get(viewHolder.adapterPosition))
                Log.i("POSITION:", viewHolder.adapterPosition.toString())
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return ItemTouchHelper.Callback.makeMovementFlags(0, LEFT or RIGHT )
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

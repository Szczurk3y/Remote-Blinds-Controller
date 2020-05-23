package Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.blindsanimation.Blind
import com.szczurk3y.blindsanimation.Handler
import Activities.MainActivity
import com.szczurk3y.blindsanimation.R
import kotlinx.android.synthetic.main.item_blind.view.*
import kotlin.math.floor

class BlindsAdapter(var blindsList: MutableList<Blind>) : RecyclerView.Adapter<BlindsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blind, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return blindsList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        blindsList[position].blind = holder.blind
        blindsList[position].blindRelativeLayout = holder.blindRelativeLayout
        holder.blind_name.text = blindsList[position].name
        holder.tactileLayout.setOnTouchListener {view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (motionEvent.y < holder.blindRelativeLayout!!.height + 5 && motionEvent.y > 0) {
                        holder.blind!!.y = motionEvent.y - holder.blind.height
                        blindsList[position].blindCoverPercentage = floor(((holder.blind.y + holder.blind.height)/ holder.blindRelativeLayout.height * 100).toDouble()).toInt()
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    MainActivity.recyclerView?.suppressLayout(true)
                }
                MotionEvent.ACTION_UP -> {
                    MainActivity.recyclerView?.suppressLayout(false)
                }
            }
            true
        }
    }


    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        Handler.activeBlind = holder.layoutPosition
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tactileLayout = itemView.tactileLayout
        val blind = itemView.blind
        val blindRelativeLayout = itemView.blindRelativeLayout
        val blind_name = itemView.blind_name
    }
}
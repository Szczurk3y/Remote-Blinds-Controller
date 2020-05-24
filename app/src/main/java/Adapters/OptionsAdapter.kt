package Adapters

import Activities.MainActivity
import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.szczurk3y.blindsanimation.Blind
import com.szczurk3y.blindsanimation.Handler
import com.szczurk3y.blindsanimation.R
import kotlinx.android.synthetic.main.item_option.view.*

class OptionsAdapter(var optionsList: MutableList<Blind>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.blindName.hint = optionsList[position].name
        holder.blindName.isCursorVisible = false
        holder.blindName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                holder.confirmButton.isEnabled = true
                holder.blindName.isCursorVisible = true
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                holder.blindName.isCursorVisible = false
            }
        })
        holder.confirmButton.setOnClickListener {
            Handler.renameBlind(position, holder.blindName.text.toString())
            holder.blindName.hint = holder.blindName.text
            holder.blindName.setText("")
            holder.blindName.clearFocus()
            holder.blindName.isCursorVisible = false
            holder.confirmButton.isEnabled = false
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blindName = view.option_blind_name
        val confirmButton = view.confirm_button
    }
}
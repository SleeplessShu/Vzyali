package ru.practicum.android.diploma.presentation.filters.location.areachoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.AreaFilter

class AreaAdapter(private val clickListener: (AreaFilter) -> Unit) :
    RecyclerView.Adapter<AreaAdapter.AreaViewHolder>() {

    var areasList: List<AreaFilter>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.area_choice_item, parent, false)
        return AreaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return areasList!!.size
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.bind(areasList!![position])
        holder.itemView.setOnClickListener {
            clickListener.invoke(areasList!![position])
        }
    }

    inner class AreaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: AreaFilter) {
            itemView.findViewById<TextView>(R.id.country_name).text = item.name
        }
    }
}

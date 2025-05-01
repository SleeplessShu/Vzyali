package ru.practicum.android.diploma.presentation.filters.industry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.FilterIndustryItemBinding
import ru.practicum.android.diploma.domain.models.main.Industry

class IndustryAdapter : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {
    private var industries: List<Industry> = emptyList()
    var onIndustrySelected: ((Industry) -> Unit)? = null

    // выбранный элемент
    var checkedIndustry: Industry? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val industryItemBinding = FilterIndustryItemBinding.inflate(layoutInflater, parent, false)
        return IndustryViewHolder(industryItemBinding)
    }

    override fun getItemCount(): Int = industries.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = industries[position]
        holder.bind(industry, position)
    }

    fun onItemClick(industry: Industry, position: Int) {
        if (industry != checkedIndustry) {
            val checkedPosition = industries.indexOf(checkedIndustry)
            notifyItemChanged(checkedPosition) // снимаем выделение у предыдущего выбранного элемента
            checkedIndustry = industry // запоминаем новый выбранный элемент
        }
        notifyItemChanged(position) // отмечаем текущий выбранный элемент
        onIndustrySelected?.invoke(industry)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(industries: List<Industry>) {
        this.industries = industries
        notifyDataSetChanged()
    }

    inner class IndustryViewHolder(
        private val binding: FilterIndustryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(industry: Industry, position: Int) {
            binding.itemName.text = industry.name
            binding.roundBtn.isChecked = industry == checkedIndustry

            binding.roundBtn.setOnClickListener {
                onItemClick(industry, position)
            }
        }
    }
}

package ru.practicum.android.diploma.presentation.filters.industry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.FilterIndustryItemBinding
import ru.practicum.android.diploma.domain.models.main.Industry

class IndustryAdapter : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {
    private var industries: List<Industry> = emptyList()

    // позиция выбранного элемента
    var checkedPosition = -1
        private set

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

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(industries: List<Industry>) {
        this.industries = industries
        notifyDataSetChanged()
    }

    inner class IndustryViewHolder(
        private val binding: FilterIndustryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.roundBtn.setOnClickListener {
                if (adapterPosition != checkedPosition) {
                    notifyItemChanged(checkedPosition) // снимаем выделение у предыдущего выбранного элемента
                    checkedPosition = adapterPosition // запоминаем новую позицию выбранного элемента
                }
                notifyItemChanged(checkedPosition) // отмечаем текущий выбранный элемент
            }
        }

        fun bind(industry: Industry, position: Int) {
            binding.itemName.text = industry.name
            binding.roundBtn.isChecked = position == checkedPosition
        }
    }
}

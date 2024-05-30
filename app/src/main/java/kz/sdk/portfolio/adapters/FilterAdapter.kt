package kz.sdk.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kz.sdk.portfolio.base.BaseFilterViewHolder
import kz.sdk.portfolio.databinding.ItemFilterBinding
import kz.sdk.portfolio.models.Filter

class FilterAdapter:ListAdapter<Filter, BaseFilterViewHolder<*>>(FilterDiffUtils()) {

    var itemClick:((Filter)-> Unit)? = null
    class FilterDiffUtils: DiffUtil.ItemCallback<Filter>(){
        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFilterViewHolder<*> {
        return FilterViewHolder(
            ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        )
    }

    override fun onBindViewHolder(holder: BaseFilterViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
    inner class FilterViewHolder(binding:ItemFilterBinding):BaseFilterViewHolder<ItemFilterBinding>(binding){
        override fun bindView(item: Filter) {
            with(binding){
                title.text = item.title
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }
}
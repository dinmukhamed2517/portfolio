package kz.sdk.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseEventViewHolder
import kz.sdk.portfolio.databinding.ItemEventBinding
import kz.sdk.portfolio.models.Event

class EventAdapter:ListAdapter<Event, BaseEventViewHolder<*>>(EventDiffUtils()) {


    var itemClick:((Event)->Unit)? = null
    class EventDiffUtils:DiffUtil.ItemCallback<Event>(){
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEventViewHolder<*> {
        return EventViewHolder(
            ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseEventViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
    inner class EventViewHolder(binding:ItemEventBinding): BaseEventViewHolder<ItemEventBinding>(binding){
        override fun bindView(item: Event) {
            with(binding){
                title.text = item.title
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder_event)
                    .into(imageView)
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }
}
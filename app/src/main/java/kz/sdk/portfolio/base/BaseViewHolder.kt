package kz.sdk.portfolio.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.sdk.portfolio.models.Event

abstract class BaseViewHolder<VB : ViewBinding, T>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bindView(item: T)
}

abstract class BaseEventViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Event>(binding)


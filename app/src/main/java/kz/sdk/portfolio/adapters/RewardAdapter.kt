package kz.sdk.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseRewardViewHolder
import kz.sdk.portfolio.databinding.ItemRewardBinding
import kz.sdk.portfolio.models.Education
import kz.sdk.portfolio.models.Reward

class RewardAdapter:androidx.recyclerview.widget.ListAdapter<Reward, BaseRewardViewHolder<*>>(RewardDiffUtils()) {
    var itemClick:((Reward) -> Unit)? = null
    class RewardDiffUtils:DiffUtil.ItemCallback<Reward>(){
        override fun areItemsTheSame(oldItem: Reward, newItem: Reward): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reward, newItem: Reward): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRewardViewHolder<*> {
        return RewardViewHolder(
            ItemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseRewardViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
    inner class RewardViewHolder(binding:ItemRewardBinding):BaseRewardViewHolder<ItemRewardBinding>(binding){
        override fun bindView(item: Reward) {
            with(binding){
                title.text = item.title
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder_event)
                    .into(img)
                issued.text = item.issues
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }

}